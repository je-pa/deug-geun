package com.zb.deuggeun.programschedule.repository;

import static com.zb.deuggeun.programschedule.entity.QProgramDurationSlot.programDurationSlot;
import static com.zb.deuggeun.programschedule.type.ProgramDurationSlotStatus.ACTIVE;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
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
    return programDurationSlot.program.id.eq(programId)
        .and(programDurationSlot.startDate.before(endDate)
            .or(programDurationSlot.startDate.eq(endDate)))
        .and(programDurationSlot.endDate.after(startDate)
            .or(programDurationSlot.endDate.eq(startDate)))
        .and(programDurationSlot.id.ne(durationId));
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
    return programDurationSlot.program.id.eq(programId)
        .and(programDurationSlot.startDate.before(endDate)
            .or(programDurationSlot.startDate.eq(endDate)))
        .and(programDurationSlot.endDate.after(startDate)
            .or(programDurationSlot.endDate.eq(startDate)));
  }

  default boolean existsByProgramInActive(Program program) {
    return this.exists(
        programDurationSlot.program.eq(program)
            .and(programDurationSlot.status.eq(ACTIVE))
    );
  }
}
