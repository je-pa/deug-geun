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

  Page<Reservation> findByReserverAndReserveDateIsBetween(
      Member reserver, LocalDate startDate, LocalDate endDate, Pageable pageable);

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

  @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
      "WHERE r.reserver = :reserver " +
      "AND r.reserveDate = :reserveDate " +
      "AND r.status IN :statuses " +
      "AND :startTime < r.timeSlot.endTime " +
      "AND :endTime > r.timeSlot.startTime")
  boolean existsByReserverAndTimeAndReserveDate(
      Member reserver, ProgramTimeSlot timeSlot, LocalDate reserveDate,
      List<ReservationStatus> statuses);

  @Query("SELECT r FROM Reservation r " +
      "WHERE r.program.trainer = :trainer " +
      "AND r.reserveDate BETWEEN :startDate AND :endDate")
  Page<Reservation> findByTrainerAndReserveDateIsBetween(
      Member trainer, LocalDate startDate, LocalDate endDate, Pageable pageable);

  @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
      "FROM Reservation r " +
      "WHERE r.timeSlot.durationSlot = :durationSlot " +
      "AND r.status IN (:statusList)")
  boolean existsByProgramDurationSlotInProgress(ProgramDurationSlot durationSlot,
      List<ReservationStatus> statusList);
}
