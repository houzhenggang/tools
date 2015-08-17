package com.xl.tool.redis;

import java.util.Map;

/**
 * author  living.li
 * date    2015/7/3.
 */
public abstract class JedisWriter<T>  {

    public  abstract Map<String,String> write(T obj);

}
