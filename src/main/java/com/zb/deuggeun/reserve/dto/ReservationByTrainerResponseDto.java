package com.zb.deuggeun.reserve.dto;

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


}
