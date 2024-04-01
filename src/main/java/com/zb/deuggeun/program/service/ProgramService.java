package com.zb.deuggeun.program.service;

import com.zb.deuggeun.common.util.EntityServiceUtil;
import com.zb.deuggeun.member.repository.MemberRepository;
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

  private final ProgramRepository programRepository;
  private final MemberRepository memberRepository;

  public CreateProgramDto.Response create(CreateProgramDto.Request request) {

    return CreateProgramDto.Response.fromEntity(programRepository.save(request.toEntity(
        EntityServiceUtil.findById(
            memberRepository,
            MySecurityUtil.getCustomUserDetails().getMemberId())
    )));
  }

  @Transactional
  public UpdateProgramDto.Response update(UpdateProgramDto.Request request) {
    Program program = EntityServiceUtil.findById(programRepository, request.programId());
    return UpdateProgramDto.Response.fromEntity(program.update(request));
  }
}
