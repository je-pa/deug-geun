package com.zb.deuggeun.reserve.status;

import static com.zb.deuggeun.common.exception.ExceptionCode.INVALID_TIME;
import static com.zb.deuggeun.common.exception.ExceptionCode.RESERVATION_STATUS_NOT_MODIFIABLE;
import static com.zb.deuggeun.common.exception.ExceptionCode.RESERVATION_STATUS_PERMISSION_DENIED;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.util.List;


public abstract class ReservationStatusUpdater {

  private final Reservation reservation;

  protected ReservationStatusUpdater(Reservation reservation) {
    this.reservation = reservation;
  }

  public final Reservation updateStateTemplateMethod() {
    // 가능한 시간인가?
    checkIfAvailableTimeAndExceptionIfNot();

    // 가능한 유저인가?
    checkIfAvailableUserAndExceptionIfNot();

    // 가능한 상태인가?
    checkIfAvailableStatusAndExceptionIfNot();

    // 상태 업데이트
    updateReservationStatus();

    return this.reservation;
  }

  private void checkIfAvailableTimeAndExceptionIfNot() {
    if (!isAvailableTime()) {
      throw new CustomException(INVALID_TIME.getStatus(), INVALID_TIME.getMessage());
    }
  }

  private void checkIfAvailableUserAndExceptionIfNot() {
    if (!isAvailableUser()) {
      throw new CustomException(
          RESERVATION_STATUS_PERMISSION_DENIED.getStatus(),
          RESERVATION_STATUS_PERMISSION_DENIED.getMessage());
    }
  }

  private void checkIfAvailableStatusAndExceptionIfNot() {
    if (!isAvailableStatus()) {
      throw new CustomException(
          RESERVATION_STATUS_NOT_MODIFIABLE.getStatus(),
          RESERVATION_STATUS_NOT_MODIFIABLE.getMessage()
      );
    }
  }

  private void updateReservationStatus() {
    this.reservation.setStatus(getNewStatus());
  }

  private boolean isAvailableStatus() {
    ReservationStatus currentStatus = getReservation().getStatus();
    return getAvailableStatus().stream()
        .anyMatch(status -> status == currentStatus);
  }

  abstract boolean isAvailableTime();

  abstract boolean isAvailableUser();

  abstract List<ReservationStatus> getAvailableStatus();

  abstract ReservationStatus getNewStatus();

  protected boolean isLoggedInUserTrainer() {
    return this.reservation.getProgram().isLoggedInUserTrainer();
  }

  protected boolean isLoggedInUserReserver() {
    return this.reservation.isLoggedInUserReserver();
  }

  protected Reservation getReservation() {
    return this.reservation;
  }
}
