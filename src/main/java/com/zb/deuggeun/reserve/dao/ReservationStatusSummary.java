package com.zb.deuggeun.reserve.dao;

import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.reserve.entity.ReservationStatistics;

public interface ReservationStatusSummary {

  ProgramTimeSlot getTimeSlot();

  int getTotalCount();

  int getCreatedCount();

  int getApprovedCount();

  int getRejectedCount();

  int getCompletedCount();

  int getCanceledCount();

  default ReservationStatistics toEntity() {
    return ReservationStatistics.builder()
        .totalCount(this.getTotalCount())
        .createdCount(this.getCreatedCount())
        .approvedCount(this.getApprovedCount())
        .rejectedCount(this.getRejectedCount())
        .completedCount(this.getCompletedCount())
        .canceledCount(this.getCanceledCount())
        .timeSlot(this.getTimeSlot())
        .program(this.getTimeSlot().getDurationSlot().getProgram())
        .build();
  }
}
