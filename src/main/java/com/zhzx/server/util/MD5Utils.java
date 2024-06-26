package com.zhzx.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5进行加密处理
 */
public class MD5Utils {

    public static String getMD5Hash(String input) {

        try {
            // 获取MD5 MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 更新需要加密的数据
            md.update(input.getBytes());
            // 完成加密计算
            byte[] digest = md.digest();
            // 将得到的字节数组转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            // 返回32位哈希值
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

}
