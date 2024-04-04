package com.zb.deuggeun.member.service;

import com.zb.deuggeun.common.service.EntityServiceInterface;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEntityService implements EntityServiceInterface<Member, Long> {

  private final MemberRepository memberRepository;

  @Override
  public JpaRepository<Member, Long> getRepository() {
    return memberRepository;
  }
}
