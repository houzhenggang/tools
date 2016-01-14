package com.xl.tool.redis;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.Properties;

/**
 * author  living.li
 * date    2015/7/3.
 */
public  class JedisResource {

    private Logger logger= LoggerFactory.getLogger(JedisResource.class);
    private ThreadLocal<Jedis> localTransaction=new ThreadLocal<Jedis>();

    /***最大jedis实列数*/
    private  int maxTotal=10;
    /**最大闲置数*/
    private int maxIdle=5;
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

    private int database;
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
        String database=properties.getProperty("redis.database");
        if(database!=null){
            this.database=Integer.parseInt(database);
        }

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
        if(this.database>0){
            this.realPool=new JedisPool(config,this.host,this.port, Protocol.DEFAULT_TIMEOUT,null,this.database);
        }else{
            realPool=new JedisPool(config,host,port);
        }

        logger.info("jedis pool init on "+host+":"+port);
        return instance;
    }
    public Jedis get(boolean isTransaction){
        if(localTransaction.get()==null){
            Jedis jedis=realPool.getResource();
            localTransaction.set(jedis);
            return  jedis;
        }else{
            return localTransaction.get();
        }
    }
    public Jedis get(){
        return get(false);
    }

    public void  release(){
        if(localTransaction.get()!=null){
            if(localTransaction.get()!=null){
                realPool.returnResource(localTransaction.get());
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
