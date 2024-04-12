package com.zb.deuggeun.programschedule.dto;

import com.zb.deuggeun.common.validation.anotaion.TimeOrder;
import com.zb.deuggeun.common.validation.type.TimeOrderValidatable;
import com.zb.deuggeun.programschedule.dto.CreateTimeSlotListDto.Request.TimeSlotDto;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateTimeSlotListDto {

  public record Request(
      @NotNull
      Long durationId,
      @NotNull
      List<TimeSlotDto> timeSlotDtoList
  ) implements TimeOverlapCheckable<TimeSlotDto> {

    public List<ProgramTimeSlot> toEntityList(ProgramDurationSlot durationSlot) {
      return timeSlotDtoList.stream().map(
          e -> e.toEntity(durationSlot)
      ).toList();
    }

    @Override
    public List<TimeSlotDto> getTimeOrderValidatableList() {
      return this.timeSlotDtoList;
    }

    @TimeOrder
    public record TimeSlotDto(
        @NotNull
        @Getter
        LocalTime startTime,

        @NotNull
        @Getter
        LocalTime endTime

    ) implements TimeOrderValidatable {

      private ProgramTimeSlot toEntity(ProgramDurationSlot durationSlot) {
        return ProgramTimeSlot.builder()
            .startTime(this.startTime)
            .endTime(this.endTime)
            .durationSlot(durationSlot)
            .build();
      }
    }
  }

  public record Response(
      String programName,
      LocalDate startDate,
      LocalDate endDate,
      List<TimeSlotDto> timeSlotDtoList
  ) {

    public static Response fromEntityList(ProgramDurationSlot durationSlot,
        List<ProgramTimeSlot> programTimeSlotList) {
      return new Response(
          durationSlot.getProgram().getName(),
          durationSlot.getStartDate(),
          durationSlot.getEndDate(),
          programTimeSlotList.stream().map(TimeSlotDto::fromEntity).toList()
      );
    }

    public record TimeSlotDto(
        Long timeSlotId,
        LocalTime startTime,
        LocalTime endTime

    ) {

      public static TimeSlotDto fromEntity(ProgramTimeSlot timeSlot) {
        return new TimeSlotDto(
            timeSlot.getId(),
            timeSlot.getStartTime(),
            timeSlot.getEndTime()
        );
      }
    }
  }


}
