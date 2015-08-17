package com.xl.tool.redis;

import java.util.Map;

/**
 * author  living.li
 * date    2015/7/3.
 */
public abstract class JedisReadHandler<T> {
    public abstract   T read(Map<String,String> objMap);
}
