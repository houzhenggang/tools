package com.xl.tool.redis;

import com.xl.tool.util.FastJsonKit;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by Administrator on 2015/8/20.
 */
public class RedisKit {
    public static final JedisResource jedisResource = JedisResource.getInstance();
    public static void setex(Object key,int expired,Object value){
        Jedis jedis = jedisResource.get(false);
        try {
            jedis.setex(String.valueOf(key),expired,FastJsonKit.javaToJsonWithClass(value) );
        } finally {
            jedisResource.release();
        }
    }
    public static void set(Object key, Object value) {
        Jedis jedis = jedisResource.get(false);
        try {
            jedis.set(String.valueOf(key), FastJsonKit.javaToJsonWithClass(value));
        } finally {
            jedisResource.release();
        }

    }

    public static <T> T get(Object key) {
        Jedis jedis = jedisResource.get(false);
        try {
            String json = jedis.get(String.valueOf(key));
            if (json == null) {
                return null;
            }
            return (T) FastJsonKit.jsonToJava(json);
        } finally {
            jedisResource.release();
        }

    }

    public static void hset(Object key, Object fieldKey, Object value) {
        Jedis jedis = jedisResource.get(false);
        try {
            jedis.hset(String.valueOf(key), String.valueOf(fieldKey), FastJsonKit.javaToJsonWithClass(value));
        } finally {
            jedisResource.release();
        }
    }

    public static <T> T hget(Object key, Object fieldKey) {
        Jedis jedis = jedisResource.get(false);
        try {
            String json = jedis.hget(String.valueOf(key), String.valueOf(fieldKey));
            if (json == null) {
                return null;
            }
            return (T) FastJsonKit.jsonToJava(json);
        } finally {
            jedisResource.release();
        }
    }

    public static long incr(String key) {
        Jedis jedis = jedisResource.get(false);
        try {
            return jedis.incr(key);
        } finally {
            jedisResource.release();
        }
    }

    public static long decr(String key) {
        Jedis jedis = jedisResource.get(false);
        try {
            return jedis.decr(key);
        } finally {
            jedisResource.release();
        }
    }

    public static long hdel(Object key, Object fieldKey) {
        Jedis jedis = jedisResource.get(false);
        try {
            return jedis.hdel(String.valueOf(key), String.valueOf(fieldKey));
        }finally {
            jedisResource.release();
        }

    }

    public static long del(Object key) {
        Jedis jedis = jedisResource.get(false);
        try {
            return jedis.del(String.valueOf(key));
        } finally {
            jedisResource.release();
        }
    }
    public static long push(Object key,Object... values){
        Jedis jedis=jedisResource.get(false);
        try {
            String[] list=new String[values.length];
            int i=0;
            for(Object value:values){
                list[i]=FastJsonKit.javaToJsonWithClass(value);
            }
            return jedis.lpush(String.valueOf(key), list);
        }finally {
            jedisResource.release();
        }
    }
    /**
     * push并且修剪列表
     * */
    public static String lpushAndLtrim(Object key, Object[] values, int start, int end){
        Jedis jedis=jedisResource.get(false);
        try {
            String[] list=new String[values.length];
            int i=0;
            for(Object value:values){
                list[i]=FastJsonKit.javaToJsonWithClass(value);
                i++;
            }
            jedis.lpush(String.valueOf(key),list);
            return jedis.ltrim(String.valueOf(key), start, end);
        }finally {
            jedisResource.release();
        }
    }
    public static List lrange(Object key,int start,int end){
        Jedis jedis=jedisResource.get(false);
        try {
            List<String> listString=jedis.lrange(String.valueOf(key),start,end);
            List<Object> result=new ArrayList<>(listString.size());
            for(String value:listString){
               result.add(FastJsonKit.jsonToJava(value));
            }
            return result;
        }finally {
            jedisResource.release();
        }
    }
}
