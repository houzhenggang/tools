package com.xl.tool.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.Properties;

/**
 * author  living.li
 * date    2015/7/3.
 */
public  class JedisResource {

    private Logger logger= LoggerFactory.getLogger(JedisResource.class);
    private ThreadLocal<JedisTransaction> localTransaction=new ThreadLocal<JedisTransaction>();

    /***最大jedis实列数*/
    private  int maxTotal=1000;
    /**最大闲置数*/
    private int maxIdle=50;
    /**获取连接超时时间*/
    private long maxWait=30000l;
    /**是否进行validate*/
    private boolean validate=true;
    /**在获取连接的时候检查有效性**/
    private boolean borrowCheck=true;

    private boolean createCheck=true;

    /**示一个对象至少停留在idle状态的最短时间，然后才能被idle*/
    private long keepAliveTime=60000l;

    private long idlePeriod=30000;

    private String host;

    private int port;

    private JedisPool realPool;
    private static final JedisResource instance=new JedisResource();
    private JedisResource(){
        
    }
    public static JedisResource getInstance(){
        return instance;
    }
    private void loadProperties(Properties properties){
        this.host=properties.getProperty("redis.host");
        this.port=Integer.parseInt(properties.getProperty("redis.port"));
        this.maxTotal=Integer.parseInt(properties.getProperty("redis.maxTotal"));
        this.maxIdle=Integer.parseInt(properties.getProperty("redis.maxIdle"));
        this.maxWait=Long.parseLong(properties.getProperty("redis.maxWait"));
        this.validate=Boolean.valueOf(properties.getProperty("redis.validate"));
        this.borrowCheck=Boolean.valueOf(properties.getProperty("redis.borrowCheck"));
        this.createCheck=Boolean.valueOf(properties.getProperty("redis.createCheck"));
        this.keepAliveTime=Long.valueOf(properties.getProperty("redis.keepAliveTime"));
        this.idlePeriod=Long.valueOf(properties.getProperty("redis.idlePeriod"));
    }
    public synchronized JedisResource init(Properties properties) {
        if(realPool!=null){
            realPool.destroy();
        }
        loadProperties(properties);
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestWhileIdle(validate);
        config.setTestOnBorrow(borrowCheck);
        realPool=new JedisPool(config,host,port);
        logger.info("jedis pool init on "+host+":"+port);
        return instance;
    }
    public Jedis get(boolean isTransaction){
        if(localTransaction.get()==null){
            Jedis jedis=realPool.getResource();
            JedisTransaction redisTransaction=new JedisTransaction(jedis);
            if(isTransaction){
                Transaction transaction=jedis.multi();
                redisTransaction.setTransaction(transaction);
            }
            localTransaction.set(redisTransaction);
            return  jedis;
        }else{
            return localTransaction.get().getJedis();
        }
    }

    public void  release(){
        if(localTransaction.get()!=null){
            if(localTransaction.get().getJedis()!=null){
                realPool.returnResource(localTransaction.get().getJedis());
                localTransaction.remove();
            }
        }
    }
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }
    public void setValidate(boolean validate) {
        this.validate = validate;
    }
    public void setBorrowCheck(boolean borrowCheck) {
        this.borrowCheck = borrowCheck;
    }
    public void setCreateCheck(boolean createCheck) {
        this.createCheck = createCheck;
    }
    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
    public void setIdlePeriod(long idlePeriod) {
        this.idlePeriod = idlePeriod;
    }

    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(int port) {
        this.port = port;
    }
}
