package com.dj.ckw.authservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key secretKey;
    private final ObjectMapper jacksonObjectMapper;

    public JwtUtil(@Value("${jwt.secret}") String secretKey, ObjectMapper jacksonObjectMapper) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.jacksonObjectMapper = jacksonObjectMapper;
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
            String json = jacksonObjectMapper.writeValueAsString(Map.of(
                    "id", claims.getPayload().getSubject(),
                    "iat", claims.getPayload().getIssuedAt().getTime(),
                    "exp", claims.getPayload().getExpiration().getTime()
            ));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
