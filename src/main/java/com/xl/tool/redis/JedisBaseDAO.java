package com.xl.tool.redis;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author  living.li
 * date    2015/7/3.
 */
public class JedisBaseDAO {
    protected JedisResource jedisResource=JedisResource.getInstance();
    protected <T> T  queryForObject(String key,JedisReader<T> readHandler){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        Map<String,String> ret=jedis.hgetAll(key);
        if(ret==null||ret.isEmpty()){
            return  null;
        }
        T t= readHandler.read(jedis.hgetAll(key));
        jedisResource.release();
        return  t;
    }

    protected <T> List<T> queryObjectFromList(String listKey,JedisReader<T> readHandler){
        List<T> list=new ArrayList<>();
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        Set<String> memberkeys=jedis.smembers(listKey);
        for(String key:memberkeys){
            list.add(queryForObject(key,readHandler));
        }
        jedisResource.release();
        return  list;
    }

    protected <T> void  saveOrUpdateOject(String objectKey,T t,JedisWriter<T> writeHandler){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        jedis.hmset(objectKey,writeHandler.write(t));
        jedisResource.release();
    }
    protected <T> void  insertObjectToSet(String listKey,String objectKey,T oject,JedisWriter<T> writeHandler){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        jedis.sadd(listKey, objectKey);
        jedis.hmset(objectKey,writeHandler.write(oject));
        jedisResource.release();
    }
    protected <T> void  insertStringToSet(String listKey,String value){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        jedis.sadd(listKey, value);
        jedisResource.release();
    }
    public Set<String> queryStringFromSet(String setKey){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        Set<String> set=jedis.smembers(setKey);
        jedisResource.release();
        return set;
    }
    protected <T> void  insertObject(String objectKey,T oject,JedisWriter<T> writeHandler){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        jedis.hmset(objectKey,writeHandler.write(oject));
        jedisResource.release();
    }

    protected <T> void  updateFilelds(String objectKey,Map<String,String> keyValues){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        for(Map.Entry<String,String> entry:keyValues.entrySet()){
            jedis.hset(objectKey,entry.getKey(),entry.getValue());
        }
        jedisResource.release();
    }

    protected <T> void  deleteFromSet(String listKey,String... objectKeys){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        jedis.srem(listKey, objectKeys);
        jedis.del(objectKeys);
        jedisResource.release();
    }

    protected <T> void  deleteObjects(String... objectKeys){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        jedis.del(objectKeys);
        jedisResource.release();
    }



    protected void setBytes(byte[] kye,byte[] value){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        jedis.set(kye,value);
        jedisResource.release();
    }
    protected byte[] getBytesValue(byte[] key){
        Jedis jedis= jedisResource.get(false);
        isNull(jedis);
        byte[] bytes= jedis.get(key);
        jedis.get(key);
        return bytes;
    }
    private void  isNull(Jedis jedis){
        if(jedis==null){
            throw new RuntimeException("can't find jedis");
        }
    }
}
