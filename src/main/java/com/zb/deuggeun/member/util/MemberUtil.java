package com.zb.deuggeun.member.util;

import com.zb.deuggeun.security.util.MySecurityUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MemberUtil {

  public static boolean isMatchLoginUser(Long memberId) {
    return MySecurityUtil.getCustomUserDetails().getMemberId().equals(memberId);
  }
}
