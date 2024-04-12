package com.zb.deuggeun.programschedule.dto;

import com.zb.deuggeun.common.validation.anotaion.TimeOrder;
import com.zb.deuggeun.common.validation.type.TimeOrderValidatable;
import com.zb.deuggeun.programschedule.dto.UpdateTimeSlotListDto.Request.TimeSlotDto;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;

public class UpdateTimeSlotListDto {

  public record Request(
      @NotNull
      Long durationId,
      @NotNull
      List<TimeSlotDto> timeSlotDtoList
  ) implements TimeOverlapCheckable<TimeSlotDto> {

    @Override
    public List<TimeSlotDto> getTimeOrderValidatableList() {
      return this.timeSlotDtoList;
    }

    @TimeOrder
    public record TimeSlotDto(
        @NotNull
        Long timeSlotId,
        @NotNull
        @Getter
        LocalTime startTime,

        @NotNull
        @Getter
        LocalTime endTime

    ) implements TimeOrderValidatable {

    }
  }

  public record Response(
      List<TimeSlotDto> timeSlotDtoList
  ) {

    public static Response fromEntityList(List<ProgramTimeSlot> programTimeSlotList) {
      return new Response(
          programTimeSlotList.stream().map(TimeSlotDto::fromEntity)
              .toList()
      );
    }

    private record TimeSlotDto(
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
