package com.zb.deuggeun.program.service;

import static com.zb.deuggeun.program.type.ProgramStatus.ACTIVE;
import static com.zb.deuggeun.program.type.ProgramStatus.DELETED;
import static org.junit.jupiter.api.Assertions.*;

import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.repository.MemberRepository;
import com.zb.deuggeun.member.type.ProfileVisibility;
import com.zb.deuggeun.member.type.Role;
import com.zb.deuggeun.program.dto.CreateProgramDto;
import com.zb.deuggeun.program.dto.CreateProgramDto.Response;
import com.zb.deuggeun.program.dto.UpdateProgramDto;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.repository.ProgramRepository;
import com.zb.deuggeun.program.type.ProgramStatus;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
//@TestPropertySource("classpath:application-test.yml") //test용 properties 파일 설정
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {

  @Mock
  ProgramRepository programRepository;
  @Mock
  MemberRepository memberRepository;
  @Mock
  ProgramService programService;

//  @BeforeEach
//  public void insert() {
//    Member member = getMember();
//    Member member1 = memberRepository.save(member);
//    CreateProgramDto.Request program = getProgramDto(getProgram(member1));
//    for (int i = 0; i < 20; i++) {
//      CreateProgramDto.Response program1 = programService.create(program);
//    }
//  }
//
//  @AfterEach
//  public void delete() {
//    programRepository.deleteAll();
//    memberRepository.deleteAll();
//  }

  private static Member getMember() {
    return Member.builder()
        .name("트")
        .email("ji@gmail.com")
        .password("d")
        .role(Role.ROLE_TRAINER)
        .visibility(ProfileVisibility.INACTIVE)
        .build();
  }

//  private static CreateProgramDto.Request getProgramDto(Program program) {
//    return new CreateProgramDto.Request(
//        program.getName(),
//        program.getDescription(),
//        program.getCapacity(),
//        program.getLocation()
//    );
//  }

//  private static Program getProgram(Member member) {
//    return Program.builder()
//        .name("프로그램")
//        .description("설")
//        .capacity(2)
//        .location("POINT(1.1,2.2)")
//        .status(DELETED)
//        .trainer(member)
//        .build();
//  }

  @Test
  public void 동시에_20개Activate() throws InterruptedException {
    int threadCount = 20;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i < 20; i++) {
      int finalI = i;
      executorService.submit(() -> {
        try {
          given(memberRepository.findByIdWithThrow(anyLong()))
              .willReturn(getMember());
          given(programRepository.countByTrainerAndStatus(any(), ACTIVE))
              .willReturn(any());
          given(programRepository.findByIdWithThrow(anyLong()))
              .willReturn(any());
          UpdateProgramDto.Response activate = programService.activate(Long.valueOf(finalI));
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

//    assertEquals(3, programRepository.countByTrainerAndStatus(
//        memberRepository.findByIdWithThrow(Long.valueOf(1)),
//        ProgramStatus.ACTIVE));
  }
}