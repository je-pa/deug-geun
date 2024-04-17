package com.zb.deuggeun.reserve.status;

import static com.zb.deuggeun.reserve.type.ReservationStatus.CANCELED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;

import com.zb.deuggeun.reserve.entity.Reservation;
import com.zb.deuggeun.reserve.type.ReservationStatus;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public class CancelStatusUpdater extends ReservationStatusUpdater {

  private static final ReservationStatus newStatus = CANCELED;
  @Getter
  private final List<ReservationStatus> availableStatus = Arrays.asList(CREATED);


  public CancelStatusUpdater(Reservation reservation) {
    super(reservation);
  }

  @Override
  protected boolean isAvailableTime() {
    return true;
  }

  @Override
  protected boolean isAvailableUser() {
    return isLoggedInUserReserver();
  }

  @Override
  ReservationStatus getNewStatus() {
    return newStatus;
  }
}