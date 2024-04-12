package com.zb.deuggeun.programschedule.dto;

import com.zb.deuggeun.common.validation.type.DateOrderValidatable;
import com.zb.deuggeun.common.validation.anotaion.DateOrder;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.type.ProgramDurationSlotStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.format.annotation.DateTimeFormat;

@UtilityClass
public class CreateProgramDurationSlotDto {

  @DateOrder
  public record Request(
      @Getter
      @NotNull
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate startDate,
      @Getter
      @NotNull
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate endDate,
      @NotNull
      Long programId
  ) implements DateOrderValidatable {

    public ProgramDurationSlot toEntity(Program program) {
      return ProgramDurationSlot.builder()
          .startDate(this.startDate)
          .endDate(this.endDate)
          .status(ProgramDurationSlotStatus.CREATED)
          .program(program)
          .build();
    }
  }

  public record Response(
      Long id,
      LocalDate startDate,
      LocalDate endDate,
      ProgramDurationSlotStatus status,
      String programName
  ) {

    public static Response fromEntity(ProgramDurationSlot programDurationSlot) {
      return new Response(
          programDurationSlot.getId(),
          programDurationSlot.getStartDate(),
          programDurationSlot.getEndDate(),
          programDurationSlot.getStatus(),
          programDurationSlot.getProgram().getName()
      );
    }
  }
}
