package com.zhzx.server.dto.annotation;

import com.zhzx.server.enums.TirAuthTypeEnum;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TirAuth {

    /**
     * 分隔符
     */
    String separator() default "&";

    /**
     * 标题
     */
    TirAuthTypeEnum type() default TirAuthTypeEnum.BASE64;


}
