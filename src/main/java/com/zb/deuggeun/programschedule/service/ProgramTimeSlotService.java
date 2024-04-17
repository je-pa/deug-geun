package com.zb.deuggeun.programschedule.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.PROGRAM_TIME_CONFLICT;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.programschedule.dto.CreateTimeSlotListDto;
import com.zb.deuggeun.programschedule.dto.UpdateTimeSlotListDto;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.repository.ProgramDurationSlotRepository;
import com.zb.deuggeun.programschedule.repository.ProgramTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgramTimeSlotService {

  private final ProgramDurationSlotRepository durationSlotRepository;
  private final ProgramTimeSlotRepository timeSlotRepository;

  @Transactional
  public CreateTimeSlotListDto.Response create(CreateTimeSlotListDto.Request request) {
    if (request.hasOverlap()) {
      throw new CustomException(PROGRAM_TIME_CONFLICT.getStatus(),
          PROGRAM_TIME_CONFLICT.getMessage());
    }

    ProgramDurationSlot durationSlot = durationSlotRepository.findByIdWithThrow(
        request.durationId());
    request.timeSlotDtoList().forEach(
        e -> {
          if (timeSlotRepository.existsOverlappingTime(
              request.durationId(), e.startTime(), e.endTime())) {
            throw new CustomException(PROGRAM_TIME_CONFLICT.getStatus(),
                PROGRAM_TIME_CONFLICT.getMessage());
          }
        }
    );

    return CreateTimeSlotListDto.Response.fromEntityList(durationSlot,
        timeSlotRepository.saveAll(request.toEntityList(durationSlot)));
  }

  @Transactional
  public UpdateTimeSlotListDto.Response update(UpdateTimeSlotListDto.Request request) {
    if (request.hasOverlap()) {
      throw new CustomException(PROGRAM_TIME_CONFLICT.getStatus(),
          PROGRAM_TIME_CONFLICT.getMessage());
    }
    request.timeSlotDtoList().forEach(
        e -> {
          if (timeSlotRepository.existsOverlappingTime(
              e.timeSlotId(), request.durationId(), e.startTime(), e.endTime())) {
            throw new CustomException(PROGRAM_TIME_CONFLICT.getStatus(),
                PROGRAM_TIME_CONFLICT.getMessage());
          }
        }
    );

    return UpdateTimeSlotListDto.Response.fromEntityList(request.timeSlotDtoList().stream().map(
        timeSlotDto -> timeSlotRepository.findByIdWithThrow(timeSlotDto.timeSlotId())
            .update(request.durationId(), timeSlotDto)).toList());
  }

  @Transactional
  public void delete(Long timSlotId) {
    timeSlotRepository.findByIdWithThrow(timSlotId).delete();

  }
}
