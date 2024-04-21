package com.zb.deuggeun.reserve.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.reserve.dao.ReservationStatusSummary;
import com.zb.deuggeun.reserve.entity.ReservationStatistics;
import com.zb.deuggeun.reserve.repository.ReservationRepository;
import com.zb.deuggeun.reserve.repository.ReservationStatisticsRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationStatisticsServiceTest {

  @Mock
  private ReservationStatisticsRepository reservationStatisticsRepository;
  @Mock
  private ReservationRepository reservationRepository;

  @InjectMocks
  private ReservationStatisticsService reservationStatisticsService;

  @Test
  void create() {
    // given
    Program program = Program
        .builder()
        .build();
    ProgramDurationSlot durationSlot = ProgramDurationSlot.builder()
        .program(program)
        .build();
    ProgramTimeSlot timeSlot = ProgramTimeSlot.builder()
        .durationSlot(durationSlot)
        .build();
    ReservationStatistics statistics = ReservationStatistics.builder()
        .id(1L)
        .reserveDate(LocalDate.now())
        .totalCount(3)
        .createdCount(3)
        .approvedCount(0)
        .rejectedCount(0)
        .completedCount(0)
        .canceledCount(0)
        .timeSlot(timeSlot)
        .program(program)
        .build();
    given(reservationRepository.countAllByReserveDateAndStatus(
        any()
    )).willReturn(List.of(get(statistics)));
    ArgumentCaptor<List<ReservationStatistics>> captor = ArgumentCaptor.forClass(List.class);

    //when
    reservationStatisticsService.create();

    // then
    verify(reservationStatisticsRepository, times(1)).saveAll(captor.capture());
    assertEquals(3, captor.getValue().get(0).getTotalCount());
  }

  ReservationStatusSummary get(ReservationStatistics statistics) {
    return new ReservationStatusSummary() {
      @Override
      public ProgramTimeSlot getTimeSlot() {
        return statistics.getTimeSlot();
      }

      @Override
      public int getTotalCount() {
        return statistics.getTotalCount();
      }

      @Override
      public int getCreatedCount() {
        return statistics.getCreatedCount();
      }

      @Override
      public int getApprovedCount() {
        return statistics.getApprovedCount();
      }

      @Override
      public int getRejectedCount() {
        return statistics.getRejectedCount();
      }

      @Override
      public int getCompletedCount() {
        return statistics.getCompletedCount();
      }

      @Override
      public int getCanceledCount() {
        return statistics.getCanceledCount();
      }
    };
  }
}