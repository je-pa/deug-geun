package com.zb.deuggeun.programschedule.dto;

import com.zb.deuggeun.common.validation.DateOrderValidatable;
import com.zb.deuggeun.common.validation.anotaion.DateOrder;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.type.ProgramDurationSlotStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.format.annotation.DateTimeFormat;

@UtilityClass
public class UpdateProgramDurationSlotDto {

  @DateOrder
  public record Request(
      @NotNull
      Long durationId,
      @Getter
      @NotNull
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate startDate,
      @Getter
      @NotNull
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate endDate
  ) implements DateOrderValidatable {

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
