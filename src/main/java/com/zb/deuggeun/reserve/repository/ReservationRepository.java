package com.zb.deuggeun.reserve.repository;

import static com.zb.deuggeun.reserve.entity.QReservation.reservation;
import static com.zb.deuggeun.reserve.type.ReservationStatus.APPROVED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;

import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.reserve.entity.Reservation;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository
    extends CustomJpaRepository<Reservation, Long>, QuerydslPredicateExecutor<Reservation> {

  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.timeSlot = :timeSlot AND r.reserveDate = :reserveDate AND (r.status = 'CREATED' OR r.status = 'APPROVED')")
  int countByTimeSlotAndReserveDate(ProgramTimeSlot timeSlot, LocalDate reserveDate);

  default boolean existsByReserverAndTimeAndReserveDate(
      Member reserver, ProgramTimeSlot timeSlot, LocalDate reserveDate) {
    return this.exists(
        reservation.reserver.eq(reserver)
            .and(reservation.reserveDate.eq(reserveDate))
            .and(reservation.status.in(CREATED, APPROVED))
            .and(reservation.timeSlot.startTime.before(timeSlot.getEndTime()))
            .and(reservation.timeSlot.endTime.after(timeSlot.getStartTime()))
    );
  }

  Page<Reservation> findByReserverAndReserveDateIsBetween(
      Member reserver, LocalDate startDate, LocalDate endDate, Pageable pageable);

  default Page<Reservation> findByTrainerAndReserveDateIsBetween(
      Member trainer, LocalDate startDate, LocalDate endDate, Pageable pageable) {
    return this.findAll(
        reservation.program.trainer.eq(trainer)
            .and(reservation.reserveDate.between(startDate, endDate)),
        pageable
    );
  }
}
