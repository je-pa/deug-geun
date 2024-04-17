package com.zb.deuggeun.programschedule.entity;

import static com.zb.deuggeun.common.exception.ExceptionCode.DURATION_MISMATCH;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.programschedule.dto.UpdateTimeSlotListDto.Request.TimeSlotDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE program_time_slot SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ProgramTimeSlot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalTime endTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private ProgramDurationSlot durationSlot;

  private boolean isDeleted;

  public ProgramTimeSlot update(Long durationId, TimeSlotDto timeSlotDto) {
    validateDurationSlot(durationId);
    durationSlot.getProgram().validateTrainerMatchLoginUser();
    this.startTime = timeSlotDto.startTime();
    this.endTime = timeSlotDto.endTime();
    return this;
  }

  public void delete() {
    durationSlot.getProgram().validateTrainerMatchLoginUser();
    this.isDeleted = true;
  }

  private void validateDurationSlot(Long durationId) {
    if (this.durationSlot.getId().equals(durationId)) {
      throw new CustomException(DURATION_MISMATCH.getStatus(), DURATION_MISMATCH.getMessage());
    }
  }
}
