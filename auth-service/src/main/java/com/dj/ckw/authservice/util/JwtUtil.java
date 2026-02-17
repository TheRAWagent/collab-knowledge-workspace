package com.dj.ckw.authservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
  private final Key secretKey;
  private final JsonMapper objectMapper;

  public JwtUtil(@Value("${jwt.secret}") String secretKey, JsonMapper objectMapper) {
    if (secretKey == null || secretKey.isBlank()) {
      throw new IllegalArgumentException("JWT secret key must not be null or empty");
    }
    byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    this.objectMapper = objectMapper;
  }

  public String generateToken(String email) {
    return Jwts.builder()
        .subject(email)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 3600 * 1000 * 10))
        .signWith(secretKey)
        .compact();
  }

  public String validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(token);
      String json = objectMapper.writeValueAsString(Map.of(
          "id", claims.getPayload().getSubject(),
          "iat", claims.getPayload().getIssuedAt().getTime(),
          "exp", claims.getPayload().getExpiration().getTime()));

      return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    } catch (JwtException e) {
      throw new JwtException("Invalid JWT token");
    } catch (JacksonException e) {
      throw new RuntimeException(e);
    }
  }
}
