package com.zb.deuggeun.reserve.status;

import static com.zb.deuggeun.reserve.type.ReservationStatus.APPROVED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.REJECTED;

import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public class RejectStatusUpdater extends ReservationStatusUpdater {

  private static final ReservationStatus newStatus = REJECTED;
  @Getter
  private final List<ReservationStatus> availableStatus = Arrays.asList(CREATED, APPROVED);

  public RejectStatusUpdater(Reservation reservation) {
    super(reservation);
  }

  @Override
  boolean isAvailableTime() {
    return true;
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
