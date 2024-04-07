package com.zb.deuggeun.programschedule.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.entity.QProgramDurationSlot;
import java.time.LocalDate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramDurationSlotRepository
    extends CustomJpaRepository<ProgramDurationSlot, Long>
    , QuerydslPredicateExecutor<ProgramDurationSlot> {

  default boolean existsOverlappingDuration(
      Long durationId, Long programId, LocalDate startDate, LocalDate endDate) {
    if (durationId == null) {
      return this.exists(getBooleanExpression(programId, endDate, startDate));
    }
    return this.exists(getBooleanExpression(durationId, programId, endDate, startDate));
  }

  private static BooleanExpression getBooleanExpression(
      Long durationId, Long programId, LocalDate endDate, LocalDate startDate) {
    return QProgramDurationSlot.programDurationSlot.program.id.eq(programId)
        .and(QProgramDurationSlot.programDurationSlot.startDate.before(endDate)
            .or(QProgramDurationSlot.programDurationSlot.startDate.eq(endDate)))
        .and(QProgramDurationSlot.programDurationSlot.endDate.after(startDate)
            .or(QProgramDurationSlot.programDurationSlot.endDate.eq(startDate)))
        .and(QProgramDurationSlot.programDurationSlot.id.ne(durationId));
  }

  private static BooleanExpression getBooleanExpression(
      Long programId, LocalDate endDate, LocalDate startDate) {
    return QProgramDurationSlot.programDurationSlot.program.id.eq(programId)
        .and(QProgramDurationSlot.programDurationSlot.startDate.before(endDate)
            .or(QProgramDurationSlot.programDurationSlot.startDate.eq(endDate)))
        .and(QProgramDurationSlot.programDurationSlot.endDate.after(startDate)
            .or(QProgramDurationSlot.programDurationSlot.endDate.eq(startDate)));
  }
}
