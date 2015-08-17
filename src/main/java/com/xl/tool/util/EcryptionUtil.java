package com.xl.tool.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * author  living.li
 * date    2015/6/26.
 */
public class EcryptionUtil {
    //AES
    public static class AES {
        /**
         * 密钥算法
         */
        private static final String KEY_ALGORITHM = "AES";

        private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

        /**
         * 转换密钥
         *
         * @param key 二进制密钥
         * @return 密钥
         */
        private static Key toKey(byte[] key) {
            //生成密钥
            return new SecretKeySpec(key, KEY_ALGORITHM);
        }

        /**
         * 加密
         *
         * @param data 待加密数据
         * @param key  密钥
         * @return byte[]   加密数据
         * @throws Exception
         */
        public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
            Key k = toKey(key);
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为加密模式
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, k, ivSpec);
            //执行操作
            byte[] encode = cipher.doFinal(data);
            return encode;
        }

        public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
            return decrypt(data, 0, key, iv);
        }

        /**
         * 解密
         *
         * @param data 待解密数据
         * @param key  密钥
         * @return byte[]   解密数据
         * @throws Exception
         */
        public static byte[] decrypt(byte[] data, int offset, byte[] key, byte[] iv) throws Exception {
            if (data == null || data.length < 16) {
                throw new Exception("data illegal");
            }
            Key k = toKey(key);
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, k, ivSpec);
            //执行操作
            return cipher.doFinal(data, offset, data.length - offset);
        }
    }
}
