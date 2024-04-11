package com.zb.deuggeun.programschedule.repository;

import com.querydsl.core.types.Predicate;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.repository.MemberRepository;
import com.zb.deuggeun.member.type.ProfileVisibility;
import com.zb.deuggeun.member.type.Role;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.repository.ProgramRepository;
import com.zb.deuggeun.program.type.ProgramStatus;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.entity.QProgramDurationSlot;
import com.zb.deuggeun.programschedule.type.ProgramDurationSlotStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml") //test용 properties 파일 설정
@SpringBootTest
class ProgramDurationSlotRepositoryTest {

  @Autowired
  ProgramDurationSlotRepository programDurationSlotRepository;
  @Autowired
  ProgramRepository programRepository;
  @Autowired
  MemberRepository memberRepository;

  private static ProgramDurationSlot getProgramDurationSlot(Program program1, LocalDate startDate,
      LocalDate endDate) {
    return ProgramDurationSlot.builder()
        .startDate(startDate)
        .endDate(endDate)
        .status(ProgramDurationSlotStatus.CREATED)
        .program(program1)
        .build();
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
        .status(ProgramStatus.CREATED)
        .trainer(member)
        .build();
  }

  @BeforeEach
  public void insert() {
    Member member = getMember();
    Member member1 = memberRepository.save(member);
    Program program = getProgram(member1);
    Program program1 = programRepository.save(program);
    ProgramDurationSlot durationSlot = programDurationSlotRepository.save(
        getProgramDurationSlot(program1,
            LocalDate.of(2024, 3, 7),
            LocalDate.of(2024, 3, 8)));
    ProgramDurationSlot durationSlot1 = programDurationSlotRepository.save(
        getProgramDurationSlot(program1,
            LocalDate.of(2024, 3, 10),
            LocalDate.of(2024, 3, 11)));
    ProgramDurationSlot durationSlot2 = programDurationSlotRepository.save(
        getProgramDurationSlot(program1,
            LocalDate.of(2024, 3, 7),
            LocalDate.of(2024, 3, 20)));
    ProgramDurationSlot durationSlot3 = programDurationSlotRepository.save(
        getProgramDurationSlot(program1,
            LocalDate.of(2024, 3, 5),
            LocalDate.of(2024, 3, 7)));

  }

  @AfterEach
  public void delete() {
    programDurationSlotRepository.deleteAll();
    programRepository.deleteAll();
    memberRepository.deleteAll();
  }

  @Test
  void test() {
    long count = programDurationSlotRepository.count();
    assertEquals(4, count);
  }

  @Test
  void queryDesTest2() {
    Long id = Long.valueOf(1);
    LocalDate requestStartDate = LocalDate.of(2024, 3, 8);
    LocalDate requestEndDate = LocalDate.of(2024, 3, 18);
    Predicate predicate = QProgramDurationSlot.programDurationSlot.program.id.eq(id)
        .and(QProgramDurationSlot.programDurationSlot.startDate.before(requestEndDate)
            .or(QProgramDurationSlot.programDurationSlot.startDate.eq(requestEndDate)))
        .and(QProgramDurationSlot.programDurationSlot.endDate.after(requestStartDate)
            .or(QProgramDurationSlot.programDurationSlot.endDate.eq(requestStartDate)));

    Iterable<ProgramDurationSlot> all = programDurationSlotRepository.findAll(predicate);
    long count = programDurationSlotRepository.count(predicate);
    assertEquals(3, count);
  }
}