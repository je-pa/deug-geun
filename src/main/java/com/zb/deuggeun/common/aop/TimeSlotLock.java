package com.zb.deuggeun.common.aop;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeSlotLock {

}
