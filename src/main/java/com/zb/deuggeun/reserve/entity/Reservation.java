package com.zb.deuggeun.reserve.entity;

import com.zb.deuggeun.common.entity.BaseEntity;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import com.zb.deuggeun.security.util.MySecurityUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 예약 정보를 나타내는 엔티티 클래스입니다.
 */
@Entity
@NoArgsConstructor(force = true)
@Getter
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  @Column(nullable = false)
  private String content;

  @Temporal(TemporalType.DATE)
  private LocalDate reserveDate;

  @Setter
  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Member reserver;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private ProgramTimeSlot timeSlot;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Program program;

  public boolean isLoggedInUserReserver() {
    return MySecurityUtil.getCustomUserDetails().getMemberId().equals(this.reserver.getId());
  }

  public Duration getDurationByReserveDate() {
    return Duration.ofDays(ChronoUnit.DAYS.between(LocalDate.now(), this.reserveDate));
  }
}

