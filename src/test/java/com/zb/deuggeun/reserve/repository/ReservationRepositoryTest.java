package com.zb.deuggeun.reserve.repository;

import static com.zb.deuggeun.program.type.ProgramStatus.DELETED;
import static com.zb.deuggeun.reserve.type.ReservationStatus.CREATED;

import com.zb.deuggeun.common.FilterManager;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.repository.MemberRepository;
import com.zb.deuggeun.member.type.ProfileVisibility;
import com.zb.deuggeun.member.type.Role;
import com.zb.deuggeun.program.entity.Program;
import com.zb.deuggeun.program.repository.ProgramRepository;
import com.zb.deuggeun.programschedule.entity.ProgramDurationSlot;
import com.zb.deuggeun.programschedule.entity.ProgramTimeSlot;
import com.zb.deuggeun.programschedule.repository.ProgramDurationSlotRepository;
import com.zb.deuggeun.programschedule.repository.ProgramTimeSlotRepository;
import com.zb.deuggeun.programschedule.type.ProgramDurationSlotStatus;
import com.zb.deuggeun.reserve.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;
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
  ReservationRepository reservationRepository;
  @Autowired
  ProgramTimeSlotRepository timeSlotRepository;
  @Autowired
  ProgramDurationSlotRepository durationSlotRepository;
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
    ProgramDurationSlot durationSlot = getDurationSlot(program1);
    ProgramDurationSlot durationSlot1 = durationSlotRepository.save(durationSlot);
    ProgramTimeSlot programTimeSlot = ProgramTimeSlot.builder()
        .startTime(LocalTime.of(4, 1, 1))
        .endTime(LocalTime.of(6, 1, 1))
        .durationSlot(durationSlot)
        .build();
    timeSlotRepository.save(programTimeSlot);
    reservationRepository.save(
        Reservation.builder()
            .content("")
            .reserveDate(LocalDate.of(2020, 1, 1))
            .status(CREATED)
            .reserver(member)
            .timeSlot(programTimeSlot)
            .program(programTimeSlot.getDurationSlot().getProgram())
            .build()
    );
  }

  private static ProgramDurationSlot getDurationSlot(Program program) {
    return ProgramDurationSlot.builder()
        .startDate(LocalDate.of(2020, 1, 1))
        .endDate(LocalDate.of(2020, 1, 1))
        .status(ProgramDurationSlotStatus.CREATED)
        .program(program)
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
        .status(DELETED)
        .trainer(member)
        .build();
  }

  @Test
  @Transactional
  void programFilter() {
    reservationRepository.countByTimeSlotAndReserveDate(
        timeSlotRepository.findByIdWithThrow(Long.valueOf(1)),
        LocalDate.of(2024, 1, 1)
    );
  }
}