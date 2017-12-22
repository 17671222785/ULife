package com.andywang.ulife.util.image;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by parting_soul on 2016/10/7.
 * MD5加密的工具类
 */

public class MD5Utils {
    /**
     * 进行MD5加密算法
     *
     * @param key
     * @return
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] data = key.getBytes();
            digest.update(data);
            cacheKey = byteToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 将字节数组转化为16进制字符串
     *
     * @param data 字节数组
     * @return String 16进制字符串
     */
    public static String byteToHexString(byte[] data) {
        final String HexString = "0123456789abcdef";
        StringBuilder builder = new StringBuilder();
        for (byte b : data) {
            //高四位转化为十六进制字符
            builder.append(HexString.charAt((b >> 4) & 0x0f));
            //低四位转化为十六进制字符
            builder.append(HexString.charAt(b & 0x0f));
        }
        return builder.toString();
    }
}
