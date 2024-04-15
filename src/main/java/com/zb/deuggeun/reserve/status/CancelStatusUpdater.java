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
  private static final Long LIMIT_TIME_MINUTES = -10L;


  public CancelStatusUpdater(Reservation reservation) {
    super(reservation);
  }

  @Override
  protected boolean isAvailableTime() {
    return LocalTime.now()
        .isBefore(getReservation().getTimeSlot().getStartTime().plusMinutes(LIMIT_TIME_MINUTES));
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