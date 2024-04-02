package com.zb.deuggeun.security.util;

import static com.zb.deuggeun.common.exception.ExceptionCode.AUTHENTICATION_ISSUE;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.security.domain.CustomUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class MySecurityUtil {

  public static CustomUserDetails getCustomUserDetails() {
    Object principal = getPrincipal();
    if (!(principal instanceof CustomUserDetails)) {
      throw new CustomException(AUTHENTICATION_ISSUE.getStatus(),
          AUTHENTICATION_ISSUE.getMessage());
    }
    return (CustomUserDetails) principal;
  }

  private static Object getPrincipal() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
