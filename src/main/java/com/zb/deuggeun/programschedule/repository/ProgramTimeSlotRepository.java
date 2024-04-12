package com.zb.deuggeun.programschedule.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.programschedule.entity.QProgramTimeSlot;
import java.time.LocalTime;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramTimeSlotRepository
    extends CustomJpaRepository<ProgramTimeSlot, Long>, QuerydslPredicateExecutor<ProgramTimeSlot> {

  void deleteAllByDurationSlot(ProgramDurationSlot durationSlot);

  default boolean existsOverlappingTime(
      Long timeId, Long durationId, LocalTime startTime, LocalTime endTime
  ) {
    return this.exists(createOverlapExpression(timeId, durationId, startTime, endTime));
  }

  default boolean existsOverlappingTime(
      Long durationId, LocalTime startTime, LocalTime endTime
  ) {
    return this.exists(createOverlapExpression(durationId, startTime, endTime));
  }

  /**
   * 겹치는 기간을 확인하기 위한 BooleanExpression 생성
   *
   * @param timeId     조회에서 제외할 기간 ID
   * @param durationId 기간 ID
   * @param startTime  시작시각
   * @param endTime    종료시각
   * @return 겹치는 기간을 확인하는 BooleanExpression
   */
  private static BooleanExpression createOverlapExpression(
      Long timeId, Long durationId, LocalTime startTime, LocalTime endTime) {
    return QProgramTimeSlot.programTimeSlot.durationSlot.id.eq(durationId)
        .and(QProgramTimeSlot.programTimeSlot.startTime.before(endTime))
        .and(QProgramTimeSlot.programTimeSlot.endTime.after(startTime))
        .and(QProgramTimeSlot.programTimeSlot.id.ne(timeId));
  }

  /**
   * 겹치는 기간을 확인하기 위한 BooleanExpression 생성
   *
   * @param durationId 기간 ID
   * @param startTime  시작시각
   * @param endTime    종료시각
   * @return 겹치는 기간을 확인하는 BooleanExpression
   */
  private static BooleanExpression createOverlapExpression(
      Long durationId, LocalTime startTime, LocalTime endTime) {
    return QProgramTimeSlot.programTimeSlot.durationSlot.id.eq(durationId)
        .and(QProgramTimeSlot.programTimeSlot.startTime.before(endTime))
        .and(QProgramTimeSlot.programTimeSlot.endTime.after(startTime));
  }
}
