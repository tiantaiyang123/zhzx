package com.zhzx.server.aspect;

import com.zhzx.server.dto.annotation.TirAuth;
import com.zhzx.server.rest.res.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.util.Base64Utils;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TirAuthAspect {

    private static final String INNER_AUTH = "K_Iw5VM92vdqoFN7vV8q";

    public void handle(JoinPoint pjp, Method method) throws Throwable {
        TirAuth tirAuth = method.getAnnotation(TirAuth.class);
        auth(tirAuth,pjp.getArgs());
    }

    public void auth(TirAuth tirAuth,Object[] args) throws Throwable {
        String code = args[0].toString();
        String codeDecode = new String(Base64Utils.decodeFromString(code));
        String[] codeDecodeArray = codeDecode.split(tirAuth.separator());

        if (codeDecodeArray.length != 2 || !codeDecodeArray[0].equals(INNER_AUTH)
                || !codeDecodeArray[1].equals(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
            throw new ApiCode.ApiException(-1, "认证失败");
        }
    }
}
