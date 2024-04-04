package com.zb.deuggeun.program.service;

import com.zb.deuggeun.common.service.EntityServiceInterface;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramEntityService implements EntityServiceInterface<Program, Long> {

  private final ProgramRepository programRepository;

  @Override
  public JpaRepository<Program, Long> getRepository() {
    return programRepository;
  }
}
