package com.zb.deuggeun.programschedule.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.zb.deuggeun.common.exception.ExceptionCode.PROGRAM_DURATION_CONFLICT;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.repository.ProgramRepository;
import com.zb.deuggeun.program.type.ProgramStatus;
import com.zb.deuggeun.programschedule.dto.CreateProgramDurationSlotDto;
import com.zb.deuggeun.programschedule.dto.UpdateProgramDurationSlotDto;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.repository.ProgramDurationSlotRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgramDurationSlotService {

  private final ProgramDurationSlotRepository durationSlotRepository;
  private final ProgramRepository programRepository;

  /**
   * 일정을 추가하는 서비스
   *
   * @param request 일정 추가에 필요한 값들을 담은 dto
   * @return 추가된 일정의 정보를 담은 dto
   * @ 하나의 프로그램에 겹치는 일정이 있으면 안된다.
   * @ 프로그램의 트레이너만이 설정할 수 있다.
   */
  public CreateProgramDurationSlotDto.Response create(
      CreateProgramDurationSlotDto.Request request) {
    Program program = programRepository.findByIdAndStatusNot(
        request.programId(), ProgramStatus.DELETED).orElseThrow(
        () -> new CustomException(ENTITY_NOT_FOUND.getStatus(), ENTITY_NOT_FOUND.getMessage()));
    program.validateTrainerMatchLoginUser();

    this.validateDuration(request.programId(), request.startDate(), request.endDate());

    return CreateProgramDurationSlotDto.Response.fromEntity(
        durationSlotRepository.save(request.toEntity(program)));
  }

  /**
   * 일정을 수정하는 서비스
   *
   * @param request 일정 수정에 필요한 값들을 담은 dto
   * @return 수정된 일정의 정보를 담은 dto
   * @ 하나의 프로그램에 겹치는 일정이 있으면 안된다.
   * @ 프로그램의 트레이너만이 수정할 수 있다.
   * @ 생성완료인 상태에서만 수정할 수 있다.
   */
  @Transactional
  public UpdateProgramDurationSlotDto.Response update(
      UpdateProgramDurationSlotDto.Request request) {
    ProgramDurationSlot durationSlot = durationSlotRepository.findByIdWithThrow(
        request.durationId());
    this.validateDuration(durationSlot.getId(), durationSlot.getProgram().getId(),
        request.startDate(),
        request.endDate());

    return UpdateProgramDurationSlotDto.Response.fromEntity(durationSlot.update(request));
  }

  private void validateDuration(Long programId, LocalDate startDate, LocalDate endDate) {
    this.validateDuration(null, programId, startDate, endDate);
  }

  private void validateDuration(Long durationId, Long programId, LocalDate startDate,
      LocalDate endDate) {
    if (durationSlotRepository.existsOverlappingDuration(durationId, programId, startDate,
        endDate)) {
      throw new CustomException(PROGRAM_DURATION_CONFLICT.getStatus(),
          PROGRAM_DURATION_CONFLICT.getMessage());
    }
  }

  @Transactional
  public UpdateProgramDurationSlotDto.Response activate(Long durationSlotId) {
    return UpdateProgramDurationSlotDto.Response.fromEntity(
        durationSlotRepository.findByIdWithThrow(durationSlotId).activate());
  }

  @Transactional
  public UpdateProgramDurationSlotDto.Response inactivate(Long durationSlotId) {
    // TODO: REQUESTED, APPROVED 상태인 예약이 있는지 확인 필요
    return UpdateProgramDurationSlotDto.Response.fromEntity(
        durationSlotRepository.findByIdWithThrow(durationSlotId).inactivate());
  }

  @Transactional
  public boolean delete(Long durationSlotId) {
    return durationSlotRepository.findByIdWithThrow(durationSlotId).delete();
  }
}
