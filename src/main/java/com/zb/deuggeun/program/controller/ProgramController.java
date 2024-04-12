package com.zb.deuggeun.program.controller;

import com.zb.deuggeun.program.dto.CreateProgramDto;
import com.zb.deuggeun.program.dto.UpdateProgramDto;
import com.zb.deuggeun.program.service.ProgramService;
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
@RequestMapping("/program")
public class ProgramController {

  private final ProgramService programService;

  @PostMapping
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<CreateProgramDto.Response> create(
      @Valid @RequestBody CreateProgramDto.Request request) {
    return ResponseEntity.ok(programService.create(request));
  }

  @PutMapping
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateProgramDto.Response> update(
      @Valid @RequestBody UpdateProgramDto.Request request
  ) {
    return ResponseEntity.ok(programService.update(request));
  }

  @PatchMapping("/activate")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateProgramDto.Response> activate(
      @RequestParam Long programId
  ) {
    return ResponseEntity.ok(programService.activate(programId));
  }

  @PatchMapping("/inactivate")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<UpdateProgramDto.Response> inactivate(@RequestParam Long programId) {
    return ResponseEntity.ok(programService.inactivate(programId));
  }

  @DeleteMapping("{programId}")
  @PreAuthorize("hasRole('TRAINER')")
  public ResponseEntity<Long> delete(@PathVariable Long programId) {
    programService.delete(programId);
    return ResponseEntity.ok(programId);
  }
}
