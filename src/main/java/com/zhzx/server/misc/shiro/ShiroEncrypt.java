/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.misc.shiro;

import org.apache.shiro.crypto.hash.Sha512Hash;

public class ShiroEncrypt {

    private static final String SALT = "^&*AS^D*&ASD*";

    public static String encrypt(String password) {
        return new Sha512Hash(password, SALT, 5).toHex();
    }

}
