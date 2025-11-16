package com.dj.ckw.userservice.controller;

import com.dj.ckw.userservice.dto.UserRequestDto;
import com.dj.ckw.userservice.dto.UserResponseDto;
import com.dj.ckw.userservice.dto.validation.CreateUserValidationGroup;
import com.dj.ckw.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Validated({Default.class, CreateUserValidationGroup.class}) UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser() {
        return ResponseEntity.ok().body(userService.getUser());
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok().body(userService.updateUser(userRequestDto));
    }
}
