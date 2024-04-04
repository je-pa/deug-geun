package com.zb.deuggeun.program.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED;
import static com.zb.deuggeun.program.type.ProgramStatus.ACTIVE;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.member.service.MemberEntityService;
import com.zb.deuggeun.program.dto.CreateProgramDto;
import com.zb.deuggeun.program.dto.UpdateProgramDto;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.repository.ProgramRepository;
import com.zb.deuggeun.security.util.MySecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgramService {

  private static final int MAX_ACTIVE_PROGRAM_LIMIT = 3;
  private final ProgramRepository programRepository;
  private final ProgramEntityService programEntityService;
  private final MemberEntityService memberEntityService;

  public CreateProgramDto.Response create(CreateProgramDto.Request request) {

    return CreateProgramDto.Response.fromEntity(programRepository.save(request.toEntity(
        memberEntityService.findById(
            MySecurityUtil.getCustomUserDetails().getMemberId())
    )));
  }

  @Transactional
  public UpdateProgramDto.Response update(UpdateProgramDto.Request request) {
    Program program = getProgramById(request.programId());
    return UpdateProgramDto.Response.fromEntity(program.update(request));
  }

  @Transactional
  public UpdateProgramDto.Response activate(Long programId) {
    int activeProgramCount = programRepository.countByTrainerAndAndStatus(
        memberEntityService.findById(MySecurityUtil.getCustomUserDetails().getMemberId()),
        ACTIVE);
    // TODO: 동시성 처리 필요
    if (activeProgramCount >= MAX_ACTIVE_PROGRAM_LIMIT) {
      throw new CustomException(MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED.getStatus(),
          MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED.getMessage());
    }

    Program program = getProgramById(programId);

    return UpdateProgramDto.Response.fromEntity(program.activate());
  }

  @Transactional
  public UpdateProgramDto.Response inactivate(Long programId) {
    Program program = getProgramById(programId);
    // TODO: 설정한 해당 program의 program slot이 모두 기간 종료 또는 비활성화 상태인지 확인
    return UpdateProgramDto.Response.fromEntity(program.inactivate());
  }

  @Transactional
  public boolean delete(Long programId) {
    Program program = getProgramById(programId);
    return program.delete();
  }

  private Program getProgramById(Long programId) {
    return programEntityService.findById(programId);
  }
}
