package com.xl.tool.codec;

import com.xl.tool.util.EcryptionUtil;
import com.xl.tool.util.HexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Caedmon on 2016/2/25.
 */
public class TokenKit {
    public static final String VERSION = "v1";
    private static final Logger log= LoggerFactory.getLogger(TokenKit.class);
    public static String encodeToken(long uid, String secretKey, String iv)  throws Exception {
        String uidStr = uid+"_"+System.currentTimeMillis();
        //aes加密
        byte[] bytes = EcryptionUtil.AES.encrypt(uidStr.getBytes(),secretKey.getBytes(),iv.getBytes());
        //编码为16进制字符串
        String uidentry = HexUtil.bytesToHex(bytes);

        //格式version_ase(uid_timestamp)
        String token = VERSION+"_"+uidentry;
        log.info("craete token :{}", token);
        return token;
    }

    public static String decodeToken(String token, String secretKey, String iv) throws Exception{
            if(StringUtils.isEmpty(token))return null;

            String[] strs = token.split("_");
            if(strs != null && strs.length>1){
                String key = strs[1];
                byte[] bytes = HexUtil.hexStringToByteArray(key);
                byte[] bt = EcryptionUtil.AES.decrypt(bytes, secretKey.getBytes(), iv.getBytes());
                String temp = new String(bt);
                log.info("parse token :"+temp);
                String[] tokens = temp.split("_");
                if(tokens != null && tokens.length>1){
                    String uid = tokens[0];
                    log.info("parse uid : "+uid);
                    return uid;
                }
            }
        return null;
    }
}
