package com.zhzx.server.aspect;

import com.zhzx.server.dto.annotation.MessageInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

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

    @AfterReturning(value = "aspect()")
    public void validationAfterPoint(JoinPoint  pjp) throws Throwable {
        Method method = currentMethod(pjp, pjp.getSignature().getName());
        //是否需要记录日志
        if (method.isAnnotationPresent(MessageInfo.class)) {
            new MessageInfoAspect().handle(pjp, method);
        }
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
