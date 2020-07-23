package io.github.fengyueqiao.marsnode.service.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2020/5/20 0020.
 */

@Aspect
@Component
public class LoggerAspect {
    private final static Logger logger = LoggerFactory.getLogger(io.github.fengyueqiao.marsnode.service.aop.LoggerAspect.class);

    /** 以 service 包下定义的所有请求为切入点 */
    @Pointcut("execution(public * io.github.fengyueqiao.marsnode.service.*.*(..))")
    public void service() {}

    @Around("service()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Start processing " + joinPoint.getSignature().getName() + " Request:" + JSON.toJSONString(joinPoint.getArgs()[0]));
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeMs = System.currentTimeMillis() - startTime;
        logger.info("End processing " + joinPoint.getSignature().getName() + " Response:"+JSON.toJSONString(result) + " time: " + timeMs);
        return result;
    }
}
