package com.zb.deuggeun.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SigninDto {

  public record Request(
      @NotBlank
      String email,
      @NotBlank
      String password
  ) {

  }

  public record Response(
      String token
  ) {

  }
}
