package com.zb.deuggeun.reserve.entity;

import com.zb.deuggeun.common.entity.BaseEntity;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(force = true)
@Getter
@AllArgsConstructor
@Builder
public class ReservationStatistics extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  @Column(nullable = false)
  private LocalDate reserveDate;

  @Column(nullable = false)
  private int totalCount;

  @Column(nullable = false)
  private int createdCount;

  @Column(nullable = false)
  private int approvedCount;

  @Column(nullable = false)
  private int rejectedCount;

  @Column(nullable = false)
  private int completedCount;

  @Column(nullable = false)
  private int canceledCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private ProgramTimeSlot timeSlot;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Program program;
}
