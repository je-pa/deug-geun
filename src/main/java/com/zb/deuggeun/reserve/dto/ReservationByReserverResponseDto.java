package com.zb.deuggeun.reserve.dto;

import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationByReserverResponseDto(
    Long reservationId,

    LocalDate reserveDate,

    LocalTime startTime,

    LocalTime endTime,

    String content,

    Long programId,

    String programName,

    ReservationStatus status
) {

  public static ReservationByReserverResponseDto fromEntity(Reservation reservation) {
    return new ReservationByReserverResponseDto(
        reservation.getId(),
        reservation.getReserveDate(),
        reservation.getTimeSlot().getStartTime(),
        reservation.getTimeSlot().getEndTime(),
        reservation.getContent(),
        reservation.getProgram().getId(),
        reservation.getProgram().getName(),
        reservation.getStatus()
    );
  }
}
