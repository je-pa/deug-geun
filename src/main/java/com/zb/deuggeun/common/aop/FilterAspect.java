package com.zb.deuggeun.common.aop;

import com.zb.deuggeun.common.FilterManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FilterAspect {

  private final FilterManager filterManager;

  public FilterAspect(FilterManager filterManager) {
    this.filterManager = filterManager;
  }

  @Pointcut("execution(* com.zb.deuggeun.programschedule.service.ProgramDurationSlotService.create(..))")
  private void isProgramDurationSlotServiceCreate() {
  }

  @Pointcut("execution(* com.zb.deuggeun.programschedule.service.ProgramDurationSlotService.update(..))")
  private void isProgramDurationSlotServiceUpdate() {
  }

  //  @Around("")
  public Object deletedProgramFilter(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("[Aspect][Filter][start]deletedProgramFilter:{}:{}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName());
    filterManager.enableFilter("deletedProgramFilter", "deleted", true);
    try {
      return joinPoint.proceed();
    } finally {
      filterManager.disableFilter("deletedProgramFilter");
      log.info("[Aspect][Filter][end]deletedProgramFilter:{}:{}",
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName());
    }
  }

  @Around("isProgramDurationSlotServiceCreate()"
      + " || isProgramDurationSlotServiceUpdate()")
  public Object deletedProgramDurationSlotFilter(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("[Aspect][Filter][start]deletedProgramDurationSlotFilter:{}:{}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName());
    filterManager.enableFilter("deletedProgramDurationSlotFilter", "deleted", true);
    try {
      return joinPoint.proceed();
    } finally {
      filterManager.disableFilter("deletedProgramDurationSlotFilter");
      log.info("[Aspect][Filter][end]deletedProgramDurationSlotFilter:{}:{}",
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName());
    }
  }
}