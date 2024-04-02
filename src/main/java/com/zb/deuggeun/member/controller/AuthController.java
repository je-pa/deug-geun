package com.zb.deuggeun.member.controller;

import com.zb.deuggeun.member.dto.SigninDto;
import com.zb.deuggeun.member.dto.SignupDto;
import com.zb.deuggeun.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<SignupDto.Response> signup(@Valid @RequestBody SignupDto.Request request) {
    SignupDto.Response response = authService.signup(request);

    String uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUriString();

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header("location", uri)
        .body(response);
  }

  @PostMapping("/signin")
  public ResponseEntity<SigninDto.Response> signin(@Valid @RequestBody SigninDto.Request request) {
    return ResponseEntity.ok(authService.signin(request));
  }
}
