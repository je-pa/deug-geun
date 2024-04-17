package com.zb.deuggeun.reserve.dto;

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

}
