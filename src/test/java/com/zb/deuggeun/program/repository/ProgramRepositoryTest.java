package com.zb.deuggeun.program.repository;

import static com.zb.deuggeun.program.type.ProgramStatus.DELETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zb.deuggeun.common.FilterManager;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.repository.MemberRepository;
import com.zb.deuggeun.member.type.ProfileVisibility;
import com.zb.deuggeun.member.type.Role;
import com.zb.deuggeun.program.entity.Program;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml") //test용 properties 파일 설정
@SpringBootTest
class ProgramRepositoryTest {

  @Autowired
  ProgramRepository programRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  FilterManager filterManager;

  @BeforeEach
  public void insert() {
    Member member = getMember();
    Member member1 = memberRepository.save(member);
    Program program = getProgram(member1);
    Program program1 = programRepository.save(program);
  }

  @AfterEach
  public void delete() {
    programRepository.deleteAll();
    memberRepository.deleteAll();
  }

  private static Member getMember() {
    return Member.builder()
        .name("트")
        .email("ji@gmail.com")
        .password("d")
        .role(Role.ROLE_TRAINER)
        .visibility(ProfileVisibility.INACTIVE)
        .build();
  }

  private static Program getProgram(Member member) {
    return Program.builder()
        .name("프로그램")
        .description("설")
        .capacity(2)
        .location("POINT(1.1,2.2)")
        .status(DELETED)
        .trainer(member)
        .build();
  }

  @Test
  @Transactional
  void programFilter() {
    filterManager.enableFilter("deletedProgramFilter", "deleted", true);
//    programRepository.findById(Long.valueOf(1));
    // 필터 적용 안됨
    Program program = programRepository.findByIdWithThrow(Long.valueOf(1));
    Member member = program.getTrainer();
    //필터 적용됨
    int count = programRepository.countByTrainerAndStatus(member, DELETED);
    filterManager.disableFilter("deletedProgramFilter");
    assertEquals(1, program.getId());
    assertEquals(0, count);
  }
}