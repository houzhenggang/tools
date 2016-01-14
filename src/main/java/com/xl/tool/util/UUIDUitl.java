package com.xl.tool.util;

import com.alibaba.fastjson.serializer.UUIDCodec;
import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

/**
 * Created by Administrator on 2015/11/19.
 */
public class UUIDUitl {
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return toBase64UUID(uuid);
    }

    public static String UUID2Base64UUID(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);
        return toBase64UUID(uuid);
    }
    public static String genBase64UUID(){
        return UUID2Base64UUID(uuid());
    }
    public static String base64UUID2UUID(String base64uuid) {
        if (base64uuid.length() != 22) {
            throw new IllegalArgumentException("Invalid base64uuid!");
        }
        byte[] byUuid = Base64.decodeBase64(base64uuid + "==");
        long most = bytes2long(byUuid, 0);
        long least = bytes2long(byUuid, 8);
        UUID uuid = new UUID(most, least);
        return uuid.toString().toUpperCase();
    }
    private static String toBase64UUID(UUID uuid) {
        byte[] byUuid = new byte[16];
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();
        long2bytes(most, byUuid, 0);
        long2bytes(least, byUuid, 8);
        String compressUUID = Base64.encodeBase64URLSafeString(byUuid);
        return compressUUID;
    }
    private static void long2bytes(long value, byte[] bytes, int offset) {
        for (int i = 7; i > -1; i--) {
            bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
        }
    }
    private static long bytes2long(byte[] bytes, int offset) {
        long value = 0;
        for (int i = 7; i > -1; i--) {
            value |= (((long) bytes[offset++]) & 0xFF) << 8 * i;
        }
        return value;
    }
}
