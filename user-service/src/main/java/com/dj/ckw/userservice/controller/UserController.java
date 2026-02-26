package com.dj.ckw.userservice.controller;

import com.dj.ckw.userservice.dto.CreateUserRequestDto;
import com.dj.ckw.userservice.dto.UpdateUserRequestDto;
import com.dj.ckw.userservice.dto.UserResponseDto;
import com.dj.ckw.userservice.dto.VerifyUserRequestDto;
import com.dj.ckw.userservice.model.RequestInfo;
import com.dj.ckw.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

  private final UserService userService;
  private final RequestInfo requestInfo;

  public UserController(UserService userService, RequestInfo requestInfo) {
    this.userService = userService;
    this.requestInfo = requestInfo;
  }

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestBody @Validated CreateUserRequestDto userRequestDto) {
    userService.createUser(userRequestDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping(produces = "application/json")
  public UserResponseDto getUser() {
    String email = requestInfo.getEmail();
    return userService.getUser(email);
  }

  @PatchMapping(produces = "application/json")
  public UserResponseDto updateUser(@RequestBody @Validated UpdateUserRequestDto updateUserRequestDto) {
    return userService.updateUser(updateUserRequestDto);
  }

  @PostMapping("/verify-email")
  public ResponseEntity<Void> verifyEmail(@RequestBody VerifyUserRequestDto verifyUserRequestDto) {
    userService.verifyEmail(verifyUserRequestDto.getEmail(), verifyUserRequestDto.getCode());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/resend-verification")
  public ResponseEntity<Void> resendVerification(@RequestBody VerifyUserRequestDto verifyUserRequestDto) {
    userService.resendVerification(verifyUserRequestDto.getEmail());
    return ResponseEntity.ok().build();
  }
}
