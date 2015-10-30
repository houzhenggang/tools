package com.xl.tool.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by Administrator on 2015/8/20.
 */
public class FastJsonKit {
    private static final SerializerFeature[] WRITE_CLASS_FEATURES =new SerializerFeature[]{SerializerFeature.WriteClassName,SerializerFeature.PrettyFormat};

    public static String javaToJsonWithClass(Object o){
        return JSON.toJSONString(o, WRITE_CLASS_FEATURES);
    }
    public static String javaToJson(Object o){
        return JSON.toJSONString(o,true);
    }
    public static Object jsonToJava(String json){
        return JSON.parse(json);
    }
    public static <T> T  jsonToJava(String json,Class<T> type){
        return JSON.parseObject(json,type);
    }
}
