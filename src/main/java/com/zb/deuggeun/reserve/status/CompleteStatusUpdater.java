package com.zb.deuggeun.reserve.status;

import static com.zb.deuggeun.reserve.type.ReservationStatus.APPROVED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.COMPLETED;

import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public class CompleteStatusUpdater extends ReservationStatusUpdater {

  private static final ReservationStatus newStatus = COMPLETED;
  @Getter
  private final List<ReservationStatus> availableStatus = Arrays.asList(APPROVED);


  public CompleteStatusUpdater(Reservation reservation) {
    super(reservation);
  }

  @Override
  boolean isAvailableTime() {
    return LocalDateTime.now().isAfter(getReservationEndDateTime());
  }

  @Override
  boolean isAvailableUser() {
    return isLoggedInUserTrainer();
  }

  @Override
  ReservationStatus getNewStatus() {
    return newStatus;
  }
}
