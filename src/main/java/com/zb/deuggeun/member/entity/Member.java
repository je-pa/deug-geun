package com.zb.deuggeun.member.entity;

import com.zb.deuggeun.common.entity.BaseEntity;
import com.zb.deuggeun.member.type.ProfileVisibility;
import com.zb.deuggeun.member.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  // 사용자 이름
  private String name;

  // 사용자 이메일
  @Column(nullable = false)
  @Email
  private final String email;

  // 비밀번호
  @Column(nullable = false)
  private final String password;

  // 프로필
  private String profileImgUrl;

  // 전화번호
  @Pattern(regexp = "^01[016789]-(?:\\d{3}|\\d{4})-\\d{4}$"
      , message = "올바른 휴대폰 번호 형식이 아닙니다.")
  private String phoneNumber;

  // 권한
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  // 프로필 공개 타입
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProfileVisibility visibility;
}
