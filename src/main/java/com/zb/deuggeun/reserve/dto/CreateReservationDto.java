package com.zb.deuggeun.reserve.dto;

import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;

import com.zb.deuggeun.common.aop.TimeSlotLockIdInterface;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record CreateReservationDto() {

  public record Request(
      @NotNull
      String content,

      @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate reserveDate,

      @NotNull
      Long timeSlotId
  ) implements TimeSlotLockIdInterface {

    public Reservation toEntity(Member member, ProgramTimeSlot timeSlot) {
      return Reservation.builder()
          .content(this.content)
          .reserveDate(this.reserveDate)
          .status(CREATED)
          .reserver(member)
          .timeSlot(timeSlot)
          .program(timeSlot.getDurationSlot().getProgram())
          .build();
    }

    @Override
    public String getTimeSlotId() {
      return String.valueOf(this.timeSlotId);
    }
  }

  public record Response(
      Long id,

      String content,

      LocalDate reservaeDate,

      ReservationStatus status,

      Long reserverId,

      Long timeSlotId,

      Long programId
  ) {

    public static Response fromEntity(Reservation reservation) {
      return new Response(
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
