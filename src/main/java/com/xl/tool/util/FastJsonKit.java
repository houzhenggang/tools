package com.xl.tool.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by Administrator on 2015/8/20.
 */
public class FastJsonKit {
    private static final SerializerFeature[] WRITE_CLASS_FEATURES =new SerializerFeature[]{SerializerFeature.WriteClassName};
    public static String javaToJsonWithClass(Object o){
        return JSON.toJSONString(o, WRITE_CLASS_FEATURES);
    }
    public static String javaToJson(Object o){
        return JSON.toJSONString(o);
    }
    public static Object jsonToJava(String json){
        return JSON.parse(json);
    }
}
