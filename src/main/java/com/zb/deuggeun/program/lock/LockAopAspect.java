package com.zb.deuggeun.program.lock;


import static com.zb.deuggeun.common.exception.ExceptionCode.LOCK_ACQUISITION_EXCEPTION;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.security.util.MySecurityUtil;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LockAopAspect {

  private final RedissonClient redissonClient;
  private static final String PREFIX = "ProgramActivate::";

  @Around("@annotation(com.zb.deuggeun.program.lock.ProgramLock)")
  public Object aroundMethod(
      ProceedingJoinPoint pjp
  ) throws Throwable {
    RLock lock = redissonClient.getLock(
        PREFIX + MySecurityUtil.getCustomUserDetails().getMemberId());
    try {
      boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(LOCK_ACQUISITION_EXCEPTION.getStatus(),
            LOCK_ACQUISITION_EXCEPTION.getMessage());
      }
      return pjp.proceed();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new CustomException(LOCK_ACQUISITION_EXCEPTION.getStatus(),
          LOCK_ACQUISITION_EXCEPTION.getMessage());
    } finally {
      lock.unlock();
    }
  }
}
