package com.zb.deuggeun.programschedule.dto;

import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.type.ProgramDurationSlotStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateProgramDurationSlotDto {

  public record Request(
      @NotNull
      Long durationId,
      @NotNull
      LocalDate startDate,
      @NotNull
      LocalDate endDate
  ) {

  }

  public record Response(
      Long id,
      LocalDate startDate,
      LocalDate endDate,
      ProgramDurationSlotStatus status,
      String programName
  ) {

    public static UpdateProgramDurationSlotDto.Response fromEntity(
        ProgramDurationSlot programDurationSlot) {
      return new UpdateProgramDurationSlotDto.Response(
          programDurationSlot.getId(),
          programDurationSlot.getStartDate(),
          programDurationSlot.getEndDate(),
          programDurationSlot.getStatus(),
          programDurationSlot.getProgram().getName()
      );
    }
  }
}
