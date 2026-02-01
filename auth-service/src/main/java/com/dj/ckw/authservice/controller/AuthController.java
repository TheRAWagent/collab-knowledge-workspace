package com.dj.ckw.authservice.controller;

import com.dj.ckw.authservice.dto.IntrospectionResponse;
import com.dj.ckw.authservice.dto.AuthRequestDto;
import com.dj.ckw.authservice.dto.validators.CreateUserValidationGroup;
import com.dj.ckw.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate Token on user login")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Validated AuthRequestDto authRequestDto) {
        Optional<String> tokenOptional = authService.authenticate(authRequestDto);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String cookie = ResponseCookie.from("token", tokenOptional.get()).httpOnly(true).secure(true).path("/").maxAge(Duration.ofDays(7).toSeconds()) // 7 days
                .sameSite("None").build().toString();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    @Operation(summary = "Register user using email/password")
    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Validated({Default.class, CreateUserValidationGroup.class}) AuthRequestDto authRequestDto) {
        authService.createUser(authRequestDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<IntrospectionResponse> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String encodedContext = authService.validateToken(authHeader);

        return ResponseEntity.ok(new IntrospectionResponse(encodedContext));
    }

    @Operation(
            summary = "Logout",
            description = "Invalidates the session or token and logs the user out"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/logout")
    public void logout() {
        // Intentionally left blank
        // Spring Security will handle the logout by clearing the cookie
    }
}
