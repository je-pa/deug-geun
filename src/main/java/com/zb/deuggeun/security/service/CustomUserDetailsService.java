package com.zb.deuggeun.security.service;

import com.zb.deuggeun.common.exception.ExceptionCode;
import com.zb.deuggeun.member.repository.MemberRepository;
import com.zb.deuggeun.security.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new CustomUserDetails(memberRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            ExceptionCode.ENTITY_NOT_FOUND.getMessage()
        )));
  }
}
