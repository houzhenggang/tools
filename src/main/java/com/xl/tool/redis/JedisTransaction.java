package com.xl.tool.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * author  living.li
 * date    2015/7/3.
 */
public class JedisTransaction {

    private Jedis jedis;
    private Transaction transaction;

    public JedisTransaction(Jedis jedis){
        this.jedis=jedis;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public Jedis  getJedis(){
        return this.jedis;
    }
    public Transaction getTransaction(){
        return  this.transaction;
    }

    public void commit(){
        this.transaction.exec();
    }
    public void rollback(){
        this.transaction.discard();
    }
}
