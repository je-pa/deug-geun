package com.zb.deuggeun.program.dto;

import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.type.ProgramStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateProgramDto {

  public record Request(
      @NotNull
      Long programId,

      @NotBlank
      String name,

      @NotBlank
      String description,

      @NotNull
      int capacity,

      @NotBlank
      @Pattern(regexp = "^POINT\\((-?\\d+(?:\\.\\d+)?),\\s*(-?\\d+(?:\\.\\d+)?)\\)$", message = "올바른 위치 형식이 아닙니다.")
      String location

  ) {

  }

  public record Response(
      Long programId,

      String name,

      String description,

      int capacity,

      String location,

      ProgramStatus status,

      String trainerName
  ) {

    public static Response fromEntity(Program program) {
      return new Response(
          program.getId(),
          program.getName(),
          program.getDescription(),
          program.getCapacity(),
          program.getLocation(),
          program.getStatus(),
          program.getTrainer().getName()
      );
    }
  }
}
