package com.zb.deuggeun.reserve.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.DATE_NOT_WITHIN_DURATION;
import static com.zb.deuggeun.common.exception.ExceptionCode.RESERVATION_DATETIME_CONFLICT;
import static com.zb.deuggeun.common.exception.ExceptionCode.RESERVATION_FULL;

import com.zb.deuggeun.common.aop.TimeSlotLock;
import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.common.redis.RedisService;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.repository.MemberRepository;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.programschedule.repository.ProgramTimeSlotRepository;
import com.zb.deuggeun.reserve.dto.CreateReservationDto;
import com.zb.deuggeun.reserve.dto.ReservationByReserverResponseDto;
import com.zb.deuggeun.reserve.dto.ReservationByTrainerResponseDto;
import com.zb.deuggeun.reserve.dto.UpdateReservationDto;
import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.repository.ReservationRepository;
import com.zb.deuggeun.reserve.status.ApproveStatusUpdater;
import com.zb.deuggeun.reserve.status.CancelStatusUpdater;
import com.zb.deuggeun.reserve.status.CompleteStatusUpdater;
import com.zb.deuggeun.reserve.status.CreateStatusUpdater;
import com.zb.deuggeun.reserve.status.RejectStatusUpdater;
import com.zb.deuggeun.security.util.MySecurityUtil;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final ProgramTimeSlotRepository timeSlotRepository;
  private final MemberRepository memberRepository;
  private final RedisService redisService;
  private static final String REMAIN_RESERVATION_COUNT_PREFIX = "reserve-remain:";

  @Transactional
  @TimeSlotLock
  public CreateReservationDto.Response create(CreateReservationDto.Request request) {
    ProgramTimeSlot timeSlot = timeSlotRepository.findByIdWithThrow(request.timeSlotId());
    Member member = getCurrentMember();

    // 기간안에 포함되는 날짜인지 확인
    validateReservationDateInProgramDurationSlot(
        request.reserveDate()
        , timeSlot.getDurationSlot().getStartDate(), timeSlot.getDurationSlot().getEndDate());

    // 동일한 예약 시간이 있는지 확인
    if (reservationRepository.existsByReserverAndTimeAndReserveDate(
        member, timeSlot, request.reserveDate())) {
      throw new CustomException(RESERVATION_DATETIME_CONFLICT.getStatus(),
          RESERVATION_DATETIME_CONFLICT.getMessage());
    }

    Reservation reservation = request.toEntity(member, timeSlot);

    updateRemainReservationCount(reservation, -1);

    return CreateReservationDto.Response.fromEntity(new CreateStatusUpdater(
        reservationRepository.save(reservation)).updateStateTemplateMethod());
  }

  private void updateRemainReservationCount(Reservation reservation, int calculate) {
    // 예약가능수 확인
    String key =
        REMAIN_RESERVATION_COUNT_PREFIX
            + reservation.getReserveDate() + ":" + reservation.getTimeSlot().getId();
    int maxCapacity = reservation.getProgram().getCapacity();
    String currentReservationRemainCount = getCurrentReservationRemainCount(key, reservation,
        maxCapacity);

    if (calculate == -1 && Integer.parseInt(currentReservationRemainCount) == 0) {
      throw new CustomException(RESERVATION_FULL.getStatus(), RESERVATION_FULL.getMessage());
    }

    // 예약가능수 업데이트
    int updateValue =
        Math.min(maxCapacity, Integer.parseInt(currentReservationRemainCount) + calculate);
    redisService.setValues(key, String.valueOf(updateValue)
        , reservation.getDurationByReserveDate());
  }

  private String getCurrentReservationRemainCount(String key, Reservation reservation,
      int maxCapacity) {
    String values = redisService.getValues(key);

    if (values != null) {
      return values;
    }

    return String.valueOf(maxCapacity
        - reservationRepository.countByTimeSlotAndReserveDate(
        reservation.getTimeSlot(), reservation.getReserveDate()));
  }

  private void validateReservationDateInProgramDurationSlot(
      LocalDate reservationDate, LocalDate startDate, LocalDate endDate
  ) {
    if (!isReservationDateInProgramDurationSlot(reservationDate, startDate, endDate)) {
      throw new CustomException(DATE_NOT_WITHIN_DURATION.getStatus(),
          DATE_NOT_WITHIN_DURATION.getMessage());
    }
  }

  private boolean isReservationDateInProgramDurationSlot(
      LocalDate reservationDate, LocalDate startDate, LocalDate endDate) {
    return !reservationDate.isBefore(startDate) && !reservationDate.isAfter(endDate);
  }

  @Transactional(readOnly = true)
  public Page<ReservationByTrainerResponseDto> readListByTrainer(
      Pageable pageable, LocalDate startDate, LocalDate endDate) {

    return reservationRepository.findByTrainerAndReserveDateIsBetween(
            getCurrentMember(), startDate, endDate, pageable)
        .map(ReservationByTrainerResponseDto::fromEntity);
  }

  @Transactional(readOnly = true)
  public Page<ReservationByReserverResponseDto> readListByMember(
      Pageable pageable, LocalDate startDate, LocalDate endDate) {
    return reservationRepository.findByReserverAndReserveDateIsBetween(
            getCurrentMember(), startDate, endDate, pageable)
        .map(ReservationByReserverResponseDto::fromEntity);
  }

  private Member getCurrentMember() {
    return memberRepository.getReferenceById(
        MySecurityUtil.getCustomUserDetails().getMemberId());
  }

  @Transactional
  public UpdateReservationDto.Response approve(Long reservationId) {
    Reservation reservation = new ApproveStatusUpdater(
        reservationRepository.findByIdWithThrow(reservationId)).updateStateTemplateMethod();
    return UpdateReservationDto.Response.fromEntity(reservation);
  }

  @Transactional
  public UpdateReservationDto.Response reject(Long reservationId) {
    Reservation reservation = new RejectStatusUpdater(
        reservationRepository.findByIdWithThrow(reservationId)).updateStateTemplateMethod();
    updateRemainReservationCount(reservation, 1);
    return UpdateReservationDto.Response.fromEntity(reservation);
  }

  @Transactional
  public UpdateReservationDto.Response complete(Long reservationId) {
    Reservation reservation = new CompleteStatusUpdater(
        reservationRepository.findByIdWithThrow(reservationId)).updateStateTemplateMethod();
    updateRemainReservationCount(reservation, 1);
    return UpdateReservationDto.Response.fromEntity(reservation);
  }

  @Transactional
  public UpdateReservationDto.Response cancel(Long reservationId) {
    Reservation reservation = new CancelStatusUpdater(
        reservationRepository.findByIdWithThrow(reservationId)).updateStateTemplateMethod();
    updateRemainReservationCount(reservation, 1);
    return UpdateReservationDto.Response.fromEntity(reservation);
  }
}
