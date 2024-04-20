package com.zb.deuggeun.reserve.repository;

import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.reserve.dao.ReservationStatusSummary;
import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

  @Query("SELECT r.timeSlot AS timeSlot, " +
      "COUNT(r) AS totalCount, " +
      "SUM(CASE WHEN r.status = 'CREATED' THEN 1 ELSE 0 END) AS createdCount, " +
      "SUM(CASE WHEN r.status = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount, " +
      "SUM(CASE WHEN r.status = 'REJECTED' THEN 1 ELSE 0 END) AS rejectedCount, " +
      "SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedCount, " +
      "SUM(CASE WHEN r.status = 'CANCELED' THEN 1 ELSE 0 END) AS canceledCount " +
      "FROM Reservation r " +
      "WHERE r.reserveDate = :reserveDate " +
      "GROUP BY r.timeSlot"
  )
  List<ReservationStatusSummary> countAllByReserveDateAndStatus(LocalDate reserveDate);

  @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
      "WHERE r.reserver = :reserver " +
      "AND r.reserveDate = :reserveDate " +
      "AND r.status IN :statuses " +
      "AND :startTime < :endTime " +
      "AND :endTime > :startTime")
  boolean existsByReserverAndTimeAndReserveDate(
      Member reserver, LocalTime startTime, LocalTime endTime, LocalDate reserveDate,
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
