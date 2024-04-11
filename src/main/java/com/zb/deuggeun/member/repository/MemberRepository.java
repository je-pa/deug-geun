package com.zb.deuggeun.member.repository;

import com.zb.deuggeun.common.repository.CustomJpaRepository;
import com.zb.deuggeun.member.entity.Member;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository
    extends CustomJpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  boolean existsByEmail(String email);
}
