package com.michealyang.util.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by michealyang on 17/3/8.
 */
@Aspect
public class ExceptionLogAspect {
    Logger logger = LoggerFactory.getLogger(ExceptionLogAspect.class);

    @AfterThrowing(
            pointcut = "execution(* com.michealyang..*.*())",
            throwing = "e"
    )
    public void logException(JoinPoint joinPoint, Throwable e){
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        String methodName = methodSignature.getName();
        logger.error(methodName + ".java ", e);
    }
}
