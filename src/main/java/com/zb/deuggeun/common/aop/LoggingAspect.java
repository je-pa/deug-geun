package com.zb.deuggeun.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Pointcut("within(com.zb.deuggeun.*.controller..*)")
  private void isAllController() {
  }

  @Around("isAllController()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("[Aspect][Log][start]logAround:{}:{}"
        , joinPoint.getSignature().getDeclaringTypeName()
        , joinPoint.getSignature().getName());

    for (Object o : joinPoint.getArgs()) {
      log.info("args: {}", o);
    }
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      return joinPoint.proceed();
    } finally {
      stopWatch.stop();
      log.info("[Aspect][Log][end]logAround:{}:{}:{}ms"
          , joinPoint.getSignature().getDeclaringTypeName()
          , joinPoint.getSignature().getName(),
          stopWatch.getTotalTimeMillis());
    }
  }
}
