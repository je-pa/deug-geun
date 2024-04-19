package com.zb.deuggeun.reserve.dto;

import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationByTrainerResponseDto(
    Long reservationId,

    LocalDate reserveDate,

    LocalTime startTime,

    LocalTime endTime,

    String content,

    Long programId,

    String programName,

    Long reserverId,

    String reserverEmail,

    ReservationStatus status

) {

  public static ReservationByTrainerResponseDto fromEntity(Reservation reservation) {
    return new ReservationByTrainerResponseDto(
        reservation.getId(),
        reservation.getReserveDate(),
        reservation.getTimeSlot().getStartTime(),
        reservation.getTimeSlot().getEndTime(),
        reservation.getContent(),
        reservation.getProgram().getId(),
        reservation.getProgram().getName(),
        reservation.getReserver().getId(),
        reservation.getReserver().getEmail(),
        reservation.getStatus()
    );
  }
}
