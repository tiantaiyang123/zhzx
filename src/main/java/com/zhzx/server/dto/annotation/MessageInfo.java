package com.zhzx.server.dto.annotation;

import java.lang.annotation.*;

/**
 * Project: server <br>
 * Description:
 *
 * @author xiongwei
 * 2020/12/3 12:12
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageInfo {

    /**
     * 名称
     */
    String name()default "";

    /**
     * 标题
     */
    String title() default "";

    /**
     * 内容.
     */
    String content() default "";

}
