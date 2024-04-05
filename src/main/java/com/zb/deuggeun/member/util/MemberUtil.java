package com.zb.deuggeun.member.util;

import com.zb.deuggeun.security.util.MySecurityUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MemberUtil {

  public static boolean isMatchLoginUser(Long memberId) {
    Long loginUserId = MySecurityUtil.getCustomUserDetails().getMemberId();
    if (loginUserId == null) {
      return false;
    }
    return loginUserId.equals(memberId);
  }
}
