package com.zb.deuggeun.reserve.controller;

import com.zb.deuggeun.reserve.dto.CreateReservationDto;
import com.zb.deuggeun.reserve.dto.ReservationByReserverResponseDto;
import com.zb.deuggeun.reserve.dto.ReservationByTrainerResponseDto;
import com.zb.deuggeun.reserve.dto.UpdateReservationDto;
import com.zb.deuggeun.reserve.service.ReservationService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

  private final ReservationService reservationService;

  /**
   * 사용자가 예약을 요청한다.
   *
   * @param request 예약 정보를 받는다.
   * @return 생성된 예약 정보를 반환한다.
   */
  @PostMapping
  @PreAuthorize("hasRole('MEMBER')")
  public ResponseEntity<CreateReservationDto.Response> create(
      @RequestBody CreateReservationDto.Request request) {
    return ResponseEntity.ok(reservationService.create(request));
  }

  /**
   * 트레이너가 날짜에 해당하는 예약 받은 내역을 확인한다.
   *
   * @param pageable  페이지 정보
   * @param startDate 예약 조회 시작 날짜
   * @param endDate   예약 조회 끝 날짜
   * @return 해당하는 예약내역을 반환한다.
   */
  @GetMapping("/trainer")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<Page<ReservationByTrainerResponseDto>> readListByTrainer(
      @PageableDefault(size = 5, sort = "id") Pageable pageable,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate) {
    return ResponseEntity.ok(
        reservationService.readListByTrainer(pageable, startDate, endDate));
  }

  /**
   * 유저가 날짜에 해당하는 예약한 내역을 확인한다.
   *
   * @param pageable  페이지 정보
   * @param startDate 예약 조회 시작 날짜
   * @param endDate   예약 조회 끝 날짜
   * @return \해당하는 예약내역을 반환한다.
   */
  @GetMapping("/member")
  @PreAuthorize("hasRole('MEMBER')")
  public ResponseEntity<Page<ReservationByReserverResponseDto>> readListByMember(
      @PageableDefault(size = 5, sort = "id") Pageable pageable,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate) {
    return ResponseEntity.ok(
        reservationService.readListByMember(pageable, startDate, endDate));
  }

  /**
   * 트레이너가 예약을 승인한다.
   *
   * @param reservationId 예약 ID
   * @return 승인한 예약의 정보를 반환한다.
   */
  @PutMapping("/{reservationId}/approve")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateReservationDto.Response> approve(
      @PathVariable Long reservationId) {
    return ResponseEntity.ok(reservationService.approve(reservationId));
  }

  /**
   * 트레이너가 예약을 거절한다.
   *
   * @param reservationId 예약 ID
   * @return 거절한 예약의 정보를 반환한다.
   */
  @PutMapping("/{reservationId}/reject")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateReservationDto.Response> reject(
      @PathVariable Long reservationId) {

    return ResponseEntity.ok(reservationService.reject(reservationId));
  }

  /**
   * 트레이너가 사용완료로 체크한다.
   *
   * @param reservationId 예약 ID
   * @return 사용완료된 예약 정보를 반환한다.
   */
  @PutMapping("/{reservationId}/complete")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateReservationDto.Response> complete(
      @PathVariable Long reservationId) {
    return ResponseEntity.ok(reservationService.complete(reservationId));
  }

  /**
   * 사용자가 취소한다.
   *
   * @param reservationId 예약 ID
   * @return 사용완료된 예약 정보를 반환한다.
   */
  @PutMapping("/{reservationId}/cancel")
  @PreAuthorize("hasRole('MEMBER')")
  public ResponseEntity<UpdateReservationDto.Response> cancel(
      @PathVariable Long reservationId) {
    return ResponseEntity.ok(reservationService.cancel(reservationId));
  }
}
