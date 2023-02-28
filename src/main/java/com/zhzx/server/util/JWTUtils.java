/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhzx.server.rest.res.ApiCode;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class JWTUtils {

    // 过期时间30天
    private static final long EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token    密钥
     * @param username 登录名
     * @param password 密码
     * @return
     */
    public static boolean verify(String token, String username, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            verifier.verify(token);
            return true;

        }catch (TokenExpiredException e){
            throw new ApiCode.ApiException(-7,"token已过期");
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取登录名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param username
     * @param password
     * @return
     */
    public static String sign(String username, String password) throws UnsupportedEncodingException {
        // 指定过期时间
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date date = cal.getTime();
        log.info("sign ExpiresAt = {}", date);
        Algorithm algorithm = Algorithm.HMAC256(password);
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

}
