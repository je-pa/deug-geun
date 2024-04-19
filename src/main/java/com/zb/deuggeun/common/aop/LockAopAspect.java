package com.zb.deuggeun.common.aop;


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
  private static final String PROGRAM_ACTIVE_PREFIX = "ProgramActivate::";
  private static final String RESERVATION_PREFIX = "Reservation::";

  @Around("@annotation(com.zb.deuggeun.common.aop.ProgramActiveLock)")
  public Object aroundMethodWithProgramActiveLock(
      ProceedingJoinPoint pjp
  ) throws Throwable {
    return doLock(pjp, redissonClient.getLock(
        PROGRAM_ACTIVE_PREFIX + MySecurityUtil.getCustomUserDetails().getMemberId()));
  }

  @Around("@annotation(com.zb.deuggeun.common.aop.ProgramActiveLock) && args(request)")
  public Object aroundMethodWithReservationLock(
      ProceedingJoinPoint pjp,
      TimeSlotLockIdInterface request
  ) throws Throwable {
    return doLock(pjp, redissonClient.getLock(
        RESERVATION_PREFIX + request.getTimeSlotId()));
  }

  private static Object doLock(ProceedingJoinPoint pjp, RLock lock) throws Throwable {
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
