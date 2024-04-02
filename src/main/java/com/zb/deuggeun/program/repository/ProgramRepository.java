package com.zb.deuggeun.program.repository;

import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.type.ProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

  int countByTrainerAndAndStatus(Member trainer, ProgramStatus status);
}
