package com.zb.deuggeun.reserve.repository;

import static com.zb.deuggeun.reserve.type.ReservationStatus.APPROVED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;

import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.reserve.entity.QReservation;
import com.zb.deuggeun.reserve.entity.Reservation;
import java.time.LocalDate;
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
        QReservation.reservation.reserver.eq(reserver)
            .and(QReservation.reservation.reserveDate.eq(reserveDate))
            .and(QReservation.reservation.status.in(CREATED, APPROVED))
            .and(QReservation.reservation.timeSlot.startTime.before(timeSlot.getEndTime()))
            .and(QReservation.reservation.timeSlot.endTime.after(timeSlot.getStartTime()))
    );
  }
}
