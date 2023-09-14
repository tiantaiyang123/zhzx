package com.zhzx.server.aspect;

import com.zhzx.server.dto.annotation.MessageInfo;
import com.zhzx.server.dto.annotation.TirAuth;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * Project: server <br>
 * Description:
 *
 * @author xiongwei
 * 2020/12/3 12:15
 */
@Aspect
@Configuration
@Slf4j
public class ControllerAspect {

    @Pointcut("execution(* com.zhzx.server..rest..*(..))")
    public void aspect() {
    }

    @Around(value = "aspect()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = currentMethod(pjp, pjp.getSignature().getName());
        //是否第三方认证
        if (method.isAnnotationPresent(TirAuth.class)) {
            new TirAuthAspect().handle(pjp, method);
        }
        Object result = pjp.proceed();
        //是否需要记录日志
        if (method.isAnnotationPresent(MessageInfo.class)) {
            new MessageInfoAspect().handle(pjp, method);
        }
        return result;
    }

    /**
     * 获取目标类的所有方法，找到当前要执行的方法
     */
    private Method currentMethod(JoinPoint joinPoint, String methodName) {
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }
}
