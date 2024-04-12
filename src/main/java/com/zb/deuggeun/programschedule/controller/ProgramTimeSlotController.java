package com.zb.deuggeun.programschedule.controller;

import com.zb.deuggeun.programschedule.dto.CreateTimeSlotListDto;
import com.zb.deuggeun.programschedule.dto.UpdateTimeSlotListDto;
import com.zb.deuggeun.programschedule.service.ProgramTimeSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/program-time-slots")
public class ProgramTimeSlotController {

  private final ProgramTimeSlotService timeSlotService;

  @PostMapping
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<CreateTimeSlotListDto.Response> create(
      @Valid @RequestBody CreateTimeSlotListDto.Request request) {
    return ResponseEntity.ok(timeSlotService.create(request));
  }

  @PutMapping
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateTimeSlotListDto.Response> update(
      @Valid @RequestBody UpdateTimeSlotListDto.Request request) {
    return ResponseEntity.ok(timeSlotService.update(request));
  }

  @DeleteMapping("/{timSlotId}")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<Long> delete(
      @PathVariable Long timSlotId) {
    timeSlotService.delete(timSlotId);
    return ResponseEntity.ok(timSlotId);
  }
}
