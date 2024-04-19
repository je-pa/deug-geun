package com.zb.deuggeun.reserve.service;

import com.zb.deuggeun.reserve.entity.ReservationStatistics;
import com.zb.deuggeun.reserve.repository.ReservationRepository;
import com.zb.deuggeun.reserve.repository.ReservationStatisticsRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationStatisticsService {

  private final ReservationStatisticsRepository statisticsRepository;
  private final ReservationRepository reservationRepository;

  @Scheduled(cron = "0 0 1 * * *")
  public void create() {
    List<ReservationStatistics> reservationStatusSummaries
        = reservationRepository.countAllByReserveDateAndStatus(
        LocalDate.now().minusDays(1)).stream().map(s -> s.toEntity()).toList();
    statisticsRepository.saveAll(reservationStatusSummaries);
  }
}
