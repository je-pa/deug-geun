package com.zb.deuggeun.member.dto;

import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.type.ProfileVisibility;
import com.zb.deuggeun.member.type.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class SignupDto {

  public record Request(
      @NotBlank
      String name,
      @NotBlank
      @Email
      String email,
      @NotBlank
      String password,
      @Pattern(regexp = "^01[016789]-(?:\\d{3}|\\d{4})-\\d{4}$"
          , message = "올바른 휴대폰 번호 형식이 아닙니다.")
      String phoneNumber,
      @NotNull
      Role role
  ) {

    public Member toEntity(PasswordEncoder passwordEncoder) {
      return Member.builder()
          .name(name)
          .email(email)
          .password(passwordEncoder.encode(password))
          .phoneNumber(phoneNumber)
          .role(role)
          .visibility(ProfileVisibility.ACTIVE)
          .build();
    }
  }

  public record Response(
      java.lang.Long id,
      String name,
      String email,
      String password,
      String phone_number,
      Role role
  ) {

    public static Response fromEntity(Member member) {
      return new Response(
          member.getId(),
          member.getName(),
          member.getEmail(),
          member.getPassword(),
          member.getPhoneNumber(),
          member.getRole());
    }
  }
}
