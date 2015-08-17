package com.xl.tool.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 */
public class ZipUtil {
    public static byte[] gzip(byte[] in) {
        if (in == null || in.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(out) {
                {
                    this.def.setLevel(Deflater.BEST_COMPRESSION);
                }
            };
            gzip.write(in);
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return out.toByteArray();
    }

    public static byte[] ungzip(byte[] in,int offset, int length) {
        ByteArrayInputStream bais = new ByteArrayInputStream(in,offset,length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            GZIPInputStream gis = new GZIPInputStream(bais);
            int count;
            int BUFFER = 4096;
            byte[] data = new byte[BUFFER];
            while ((count = gis.read(data, 0, BUFFER)) != -1) {
                baos.write(data, 0, count);
            }
            byte[] retData = baos.toByteArray();
            baos.close();
            bais.close();
            gis.close();
            return retData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
