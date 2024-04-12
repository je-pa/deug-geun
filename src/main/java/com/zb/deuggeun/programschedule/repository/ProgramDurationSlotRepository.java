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
      return this.exists(createOverlapExpression(programId, startDate, endDate));
    }
    return this.exists(createOverlapExpression(durationId, programId, startDate, endDate));
  }

  /**
   * 겹치는 기간을 확인하기 위한 BooleanExpression 생성
   *
   * @param durationId 조회에서 제외할 기간 ID
   * @param programId  프로그램 ID
   * @param startDate  시작일
   * @param endDate    종료일
   * @return 겹치는 기간을 확인하는 BooleanExpression
   */
  private static BooleanExpression createOverlapExpression(
      Long durationId, Long programId, LocalDate startDate, LocalDate endDate) {
    return QProgramDurationSlot.programDurationSlot.program.id.eq(programId)
        .and(QProgramDurationSlot.programDurationSlot.startDate.before(endDate)
            .or(QProgramDurationSlot.programDurationSlot.startDate.eq(endDate)))
        .and(QProgramDurationSlot.programDurationSlot.endDate.after(startDate)
            .or(QProgramDurationSlot.programDurationSlot.endDate.eq(startDate)))
        .and(QProgramDurationSlot.programDurationSlot.id.ne(durationId));
  }

  /**
   * 겹치는 기간을 확인하기 위한 BooleanExpression 생성
   *
   * @param programId 프로그램 ID
   * @param startDate 시작일
   * @param endDate   종료일
   * @return 겹치는 기간을 확인하는 BooleanExpression
   */
  private static BooleanExpression createOverlapExpression(
      Long programId, LocalDate startDate, LocalDate endDate) {
    return QProgramDurationSlot.programDurationSlot.program.id.eq(programId)
        .and(QProgramDurationSlot.programDurationSlot.startDate.before(endDate)
            .or(QProgramDurationSlot.programDurationSlot.startDate.eq(endDate)))
        .and(QProgramDurationSlot.programDurationSlot.endDate.after(startDate)
            .or(QProgramDurationSlot.programDurationSlot.endDate.eq(startDate)));
  }
}
