package com.zb.deuggeun.member.service;

import static com.zb.deuggeun.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.zb.deuggeun.common.exception.ExceptionCode.PASSWORD_MISMATCH;
import static com.zb.deuggeun.common.exception.ExceptionCode.USER_ID_CONFLICT;

import com.zb.deuggeun.common.exception.CustomException;
import com.zb.deuggeun.member.dto.SigninDto;
import com.zb.deuggeun.member.dto.SignupDto;
import com.zb.deuggeun.member.entity.Member;
import com.zb.deuggeun.member.repository.MemberRepository;
import com.zb.deuggeun.security.jwt.provider.TokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  public SignupDto.Response signup(SignupDto.Request request) {
    if (memberRepository.existsByEmail(request.email())) {
      throw new CustomException(USER_ID_CONFLICT.getStatus(),
          USER_ID_CONFLICT.getMessage());
    }

    return SignupDto.Response.fromEntity(memberRepository.save(request.toEntity(passwordEncoder)));
  }

  public SigninDto.Response signin(SigninDto.Request request) {
    Member member = memberRepository.findByEmail(request.email())
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND.getStatus(),
            ENTITY_NOT_FOUND.getMessage()));

    if (!passwordEncoder.matches(request.password(), member.getPassword())) {
      throw new CustomException(PASSWORD_MISMATCH.getStatus(), PASSWORD_MISMATCH.getMessage());
    }

    return new SigninDto.Response(
        tokenProvider.generateToken(member.getEmail(), List.of(member.getRole().name())));
  }
}
