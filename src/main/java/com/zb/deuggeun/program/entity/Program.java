package com.zb.deuggeun.program.entity;

import static com.zb.deuggeun.common.exception.ExceptionCode.IMMUTABLE_STATUS;
import static com.zb.deuggeun.common.exception.ExceptionCode.LOGIN_USER_MISMATCH;

import com.zb.deuggeun.common.entity.BaseEntity;
import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.util.MemberUtil;
import com.zb.deuggeun.program.dto.UpdateProgramDto;
import com.zb.deuggeun.program.type.ProgramStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Program extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private int capacity;

  @Column(nullable = false)
  private String location;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProgramStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Member trainer;

  public Program update(UpdateProgramDto.Request request) {
    isAvailableUpdateWithThrow();
    this.name = request.name();
    this.description = request.description();
    this.capacity = request.capacity();
    this.location = request.location();
    return this;
  }

  private boolean isAvailableUpdateWithThrow() {
    if (!MemberUtil.isMatchLoginUser(this.trainer.getId())) {
      throw new CustomException(LOGIN_USER_MISMATCH.getStatus(), LOGIN_USER_MISMATCH.getMessage());
    }
    if (this.status != ProgramStatus.CREATED) {
      throw new CustomException(IMMUTABLE_STATUS.getStatus(), IMMUTABLE_STATUS.getMessage());
    }
    return true;
  }
}
