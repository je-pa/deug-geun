package com.zb.deuggeun.programschedule.entity;

import static com.zb.deuggeun.common.exception.ExceptionCode.IMMUTABLE_STATUS;

import com.zb.deuggeun.common.entity.BaseEntity;
import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.programschedule.dto.UpdateProgramDurationSlotDto.Request;
import com.zb.deuggeun.programschedule.type.ProgramDurationSlotStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
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
public class ProgramDurationSlot extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false)
  private ProgramDurationSlotStatus status;

  //  @Column(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private final Program program;

  public ProgramDurationSlot update(Request request) {
    program.validateTrainerMatchLoginUser();
    validateStatusIsCreated();
    this.startDate = request.startDate();
    this.endDate = request.endDate();
    return this;
  }

  private void validateStatusIsCreated() {
    if (this.status != ProgramDurationSlotStatus.CREATED) {
      throw new CustomException(IMMUTABLE_STATUS.getStatus(), IMMUTABLE_STATUS.getMessage());
    }
  }
}
