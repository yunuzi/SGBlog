package com.sangeng.asp;


import com.alibaba.fastjson.JSON;
import com.sangeng.annotation.SystemAsp;
import com.sangeng.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

@Component
@Aspect
@Slf4j
public class LogAsp {

    @Pointcut("@annotation(com.sangeng.annotation.SystemAsp)")
    public void pt(){

    }

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        ResponseResult proceed;
        try {
            before(joinPoint);
            proceed = (ResponseResult) joinPoint.proceed();
            after(proceed);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }

        return proceed;
    }

    private void after(ResponseResult result) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(result));
    }

    private void before(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        SystemAsp systemAsp = getSystemLog(joinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemAsp.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private SystemAsp getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemAsp annotation = methodSignature.getMethod().getAnnotation(SystemAsp.class);

        return annotation;
    }


}
