package com.zb.deuggeun.programschedule.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.zb.deuggeun.common.exception.ExceptionCode.PROGRAM_DURATION_CONFLICT;
import static com.zb.deuggeun.common.exception.ExceptionCode.RESERVATION_IN_PROGRESS;
import static com.zb.deuggeun.reserve.type.ReservationStatus.APPROVED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.repository.ProgramRepository;
import com.zb.deuggeun.program.type.ProgramStatus;
import com.zb.deuggeun.programschedule.dto.CreateProgramDurationSlotDto;
import com.zb.deuggeun.programschedule.dto.UpdateProgramDurationSlotDto;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.repository.ProgramDurationSlotRepository;
import com.zb.deuggeun.programschedule.repository.ProgramTimeSlotRepository;
import com.zb.deuggeun.reserve.repository.ReservationRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgramDurationSlotService {

  private final ProgramRepository programRepository;
  private final ProgramDurationSlotRepository durationSlotRepository;
  private final ProgramTimeSlotRepository timeSlotRepository;
  private final ReservationRepository reservationRepository;

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
    ProgramDurationSlot durationSlot = durationSlotRepository.findByIdWithThrow(durationSlotId);

    if (reservationRepository.existsByProgramDurationSlotInProgress(durationSlot,
        List.of(CREATED, APPROVED))) {
      throw new CustomException(RESERVATION_IN_PROGRESS.getStatus()
          , RESERVATION_IN_PROGRESS.getMessage());
    }

    return UpdateProgramDurationSlotDto.Response.fromEntity(
        durationSlot.inactivate());
  }

  @Transactional
  public Long delete(Long durationSlotId) {
    ProgramDurationSlot durationSlot = durationSlotRepository.findByIdWithThrow(durationSlotId);
    timeSlotRepository.deleteAllByDurationSlot(durationSlot);
    durationSlot.delete();
    return durationSlotId;
  }
}
