package com.zb.deuggeun.security.domain;

import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.type.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDetailsDomain(
    @NotNull
    Long id,

    @NotBlank
    String email,

    @NotBlank
    String password,

    @NotNull
    Role role
) {

  public static UserDetailsDomain fromEntity(Member member) {
    return new UserDetailsDomain(
        member.getId(),
        member.getEmail(),
        member.getPassword(),
        member.getRole()
    );
  }
}
