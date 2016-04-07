package com.xl.tool.current;

import com.xl.tool.redis.RedisKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Caedmon on 2016/4/7.
 * 分布式锁工具类
 */

public class DCSCASKit {
    private static final ThreadLocal<Map<String,AtomicInteger>> threadLocal=new ThreadLocal<>();
    private static final Logger log= LoggerFactory.getLogger(DCSCASKit.class);
    private boolean lock(String key, long timeout){
        Map<String,AtomicInteger> lockMap=threadLocal.get();
        if(lockMap==null){
            lockMap=new ConcurrentHashMap<>();
        }
        AtomicInteger value=lockMap.get(key);
        boolean canOp=false;
        //如果当前线程未获取到锁,则需要进行分布式锁定
        if(value==null){
            value=new AtomicInteger();
            log.info("Lock redis cache:key={}",key);
            long firstTryTime=System.currentTimeMillis();
            //获取分布式锁
            while (!canOp){
                long now=System.currentTimeMillis();
                if(now-firstTryTime>=timeout){
                    log.error("Get redis lock timeout:key={}",key);
                    return false;
                }
                canOp= RedisKit.tryLock(key,10);
            }
        }else{
            //如果当前线程已经锁定,表示已经取得过分布式锁
            value.incrementAndGet();
            canOp=true;
        }
        lockMap.put(key,value);
        threadLocal.set(lockMap);
        return canOp;
    }
    private void unlock(String key){
        Map<String,AtomicInteger> lockMap=threadLocal.get();
        if(lockMap==null){
            return;
        }
        AtomicInteger lock=lockMap.get(key);
        //为空表示为已释放
        if(lock==null){
            return;
        }
        int value=lock.decrementAndGet();
        if(value<=0){
            lockMap.remove(key);
            threadLocal.set(lockMap);
            RedisKit.releaseLock(key);
            log.info("Unlock success:key={}",key);
        }
    }
}
