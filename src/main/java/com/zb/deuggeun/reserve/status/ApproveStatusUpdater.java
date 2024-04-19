package com.zb.deuggeun.reserve.status;

import static com.zb.deuggeun.reserve.type.ReservationStatus.APPROVED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;

import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public class ApproveStatusUpdater extends ReservationStatusUpdater {

  private static final ReservationStatus newStatus = APPROVED;
  @Getter
  private final List<ReservationStatus> availableStatus = Arrays.asList(CREATED);
  private static final Long LIMIT_TIME_MINUTES = 10L;


  public ApproveStatusUpdater(Reservation reservation) {
    super(reservation);
  }

  @Override
  protected boolean isAvailableTime() {
    return LocalDateTime.now().isBefore(
        getReservationStartDateTime().minusMinutes(LIMIT_TIME_MINUTES));
  }

  @Override
  protected boolean isAvailableUser() {
    return isLoggedInUserTrainer();
  }

  @Override
  ReservationStatus getNewStatus() {
    return newStatus;
  }
}
