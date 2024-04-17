package com.zb.deuggeun.reserve.dto;


import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record UpdateReservationDto() {

  public record Request(
      @NotNull
      Long reservationId,

      @NotNull
      String content,

      @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate reserveDate,

      @NotNull
      Long timeSlotId
  ) {

  }

  public record Response(
      Long reservationId,

      String content,

      LocalDate reservaeDate,

      ReservationStatus status,

      Long reserverId,

      Long timeSlotId,

      Long programId
  ) {

    public static UpdateReservationDto.Response fromEntity(Reservation reservation) {
      return new UpdateReservationDto.Response(
          reservation.getId(),
          reservation.getContent(),
          reservation.getReserveDate(),
          reservation.getStatus(),
          reservation.getReserver().getId(),
          reservation.getTimeSlot().getId(),
          reservation.getProgram().getId()
      );
    }
  }
}
