package com.zb.deuggeun.program.repository;

import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.type.ProgramStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends CustomJpaRepository<Program, Long> {

  int countByTrainerAndAndStatus(Member trainer, ProgramStatus status);
}
