package com.dj.ckw.authservice.service;

import com.dj.ckw.authservice.dto.AuthRequestDto;
import com.dj.ckw.authservice.dto.UserVerificationRequestDto;
import com.dj.ckw.authservice.model.User;
import com.dj.ckw.authservice.repository.UserRepository;
import com.dj.ckw.authservice.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public Optional<String> authenticate(AuthRequestDto authRequestDto) {
        return userService.findByEmail(authRequestDto.getEmail())
                .filter(user -> passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword()))
                .map(user -> jwtUtil.generateToken(user.getEmail()));
    }

    @Cacheable(value = "users", key = "#token")
    public String validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public void createUser(@Valid AuthRequestDto authRequestDto) {
        User user = new User();
        user.setEmail(authRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
        userRepository.save(user);
    }

    public void verifyUser(@Valid UserVerificationRequestDto userVerificationRequestDto) {
        userService.markUserAsVerified(userVerificationRequestDto.getEmail());
//        Add mechanism to verify code later
    }
}
