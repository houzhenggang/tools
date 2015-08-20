package com.xl.tool.redis;

import com.xl.tool.util.FastJsonKit;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/8/20.
 */
public class RedisKit {
    public static final JedisResource jedisResource=JedisResource.getInstance();
    public static void set(Object key,Object value){
        Jedis jedis= jedisResource.get(false);
        try{
            jedis.set(String.valueOf(key), FastJsonKit.javaToJsonWithClass(value));
        }finally {
            jedisResource.release();
        }

    }

    public  static  <T> T get(Object key){
        Jedis jedis= jedisResource.get(false);
        try{
            String json=jedis.get(String.valueOf(key));
            if(json==null){
                return null;
            }
            return (T)FastJsonKit.jsonToJava(json);
        }finally {
            jedisResource.release();
        }

    }

    public static void hset(Object key,Object fieldKey,Object value){
        Jedis jedis= jedisResource.get(false);
        try{
            jedis.hset(String.valueOf(key),String.valueOf(fieldKey), FastJsonKit.javaToJsonWithClass(value));
        }finally {
            jedisResource.release();
        }
    }
    public static <T> T hget(Object key,Object fieldKey){
        Jedis jedis= jedisResource.get(false);
        try{
            String json=jedis.hget(String.valueOf(key),String.valueOf(fieldKey));
            if(json==null){
                return null;
            }
            return (T)FastJsonKit.jsonToJava(json);
        }finally {
            jedisResource.release();
        }
    }
    public static long incr(String key){
        Jedis jedis= jedisResource.get(false);
        try{
            return jedis.incr(key);
        }finally {
            jedisResource.release();
        }
    }
    public static long decr(String key){
        Jedis jedis= jedisResource.get(false);
        try{
            return jedis.decr(key);
        }finally {
            jedisResource.release();
        }
    }
}
