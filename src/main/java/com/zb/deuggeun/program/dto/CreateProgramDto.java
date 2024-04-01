package com.zb.deuggeun.program.dto;

import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.entity.Trainer;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.type.ProgramStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateProgramDto {

  public record Request(
      @NotBlank
      String name,

      @NotBlank
      String description,

      @NotBlank
      int capacity,

      @NotBlank
      String location

  ) {

    public Program toEntity(Member loginUser) {
      return Program.builder()
          .name(name)
          .description(description)
          .capacity(capacity)
          .location(location)
          .status(ProgramStatus.CREATED)
          .trainer(loginUser)
          .build();
    }
  }

  public record Response(
      Long id,

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
