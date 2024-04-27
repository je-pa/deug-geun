package com.zb.deuggeun.notification.entity;


import com.zb.deuggeun.common.entity.BaseEntity;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.notification.type.NotificationType;
import com.zb.deuggeun.reserve.entity.Reservation;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Notification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  private String message;

  private LocalDateTime readDateTime;

  private NotificationType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Member member;

  public static Notification createRemindNotificationFrom(Reservation reservation) {
    return Notification.builder()
        .message(getMessage(
            reservation.getReserveDate()
            , reservation.getTimeSlot().getStartTime()
            , reservation.getTimeSlot().getEndTime()))
        .type(NotificationType.REMINDER)
        .member(reservation.getReserver())
        .build();
  }

  private static String getMessage(LocalDate date, LocalTime startTime, LocalTime endTime) {
    String dateFormat = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String startTimeFormat = startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    String endTimeFormat = endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    return String.format("%s, %s ~ %s 예약이 있습니다.", dateFormat, startTimeFormat, endTimeFormat);
  }
}
