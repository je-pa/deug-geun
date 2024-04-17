package com.zb.deuggeun.programschedule.controller;

import com.zb.deuggeun.programschedule.dto.CreateProgramDurationSlotDto;
import com.zb.deuggeun.programschedule.dto.UpdateProgramDurationSlotDto;
import com.zb.deuggeun.programschedule.service.ProgramDurationSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/program-duration-slots")
public class ProgramDurationSlotController {

  private final ProgramDurationSlotService durationSlotService;

  @PostMapping
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<CreateProgramDurationSlotDto.Response> create(
      @Valid @RequestBody CreateProgramDurationSlotDto.Request request) {
    return ResponseEntity.ok(durationSlotService.create(request));
  }

  @PutMapping
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateProgramDurationSlotDto.Response> update(
      @Valid @RequestBody UpdateProgramDurationSlotDto.Request request) {
    return ResponseEntity.ok(durationSlotService.update(request));
  }

  @PatchMapping("/activate")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateProgramDurationSlotDto.Response> activate(
      @RequestParam Long durationSlotId
  ) {
    return ResponseEntity.ok(durationSlotService.activate(durationSlotId));
  }

  @PatchMapping("/inactivate")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateProgramDurationSlotDto.Response> inactivate(
      @RequestParam Long durationSlotId
  ) {
    return ResponseEntity.ok(durationSlotService.inactivate(durationSlotId));
  }

  @DeleteMapping("{durationSlotId}")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<Long> delete(
      @PathVariable Long durationSlotId
  ) {
    return ResponseEntity.ok(durationSlotService.delete(durationSlotId));
  }
}
