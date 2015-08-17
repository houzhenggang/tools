package com.xl.tool.util;

/**
 * author  living.li
 * date    2015/6/26.
 */
public class RandomUtil {

    public static byte[] genInteger16(int max){
        byte[] value=new byte[16];
        for(int i=0;i<value.length;i=i+4){
            int gen=(int)(Math.random()*max);
            value[i]=(byte)(gen>>24);
            value[i+1]=(byte)(gen>>16);
            value[i+2]=(byte)(gen>>8);
            value[i+3]=(byte)(gen);
        }
        return value;
    }
}
