package com.example.user.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private static final String SECRET = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi10aGlzLW11c3QtYmUtbG9uZy1lbm91Z2g=";
    private static final long ACCESS_TOKEN_EXPIRY = 3600000L;
    private static final long REFRESH_TOKEN_EXPIRY = 604800000L;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(SECRET, ACCESS_TOKEN_EXPIRY, REFRESH_TOKEN_EXPIRY);
    }

    @Test
    void generateAccessToken_유효한_토큰_생성() {
        String uuid = UUID.randomUUID().toString();

        String token = jwtTokenProvider.generateAccessToken(uuid);

        assertThat(token).isNotBlank();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void generateRefreshToken_유효한_토큰_생성() {
        String uuid = UUID.randomUUID().toString();

        String token = jwtTokenProvider.generateRefreshToken(uuid);

        assertThat(token).isNotBlank();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void getUuidFromToken_토큰에서_UUID_추출() {
        String uuid = UUID.randomUUID().toString();
        String token = jwtTokenProvider.generateAccessToken(uuid);

        String extracted = jwtTokenProvider.getUuidFromToken(token);

        assertThat(extracted).isEqualTo(uuid);
    }

    @Test
    void validateToken_만료된_토큰_검증_실패() {
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(SECRET, -1000L, -1000L);
        String uuid = UUID.randomUUID().toString();
        String token = shortLivedProvider.generateAccessToken(uuid);

        assertThat(jwtTokenProvider.validateToken(token)).isFalse();
    }

    @Test
    void validateToken_잘못된_토큰_검증_실패() {
        assertThat(jwtTokenProvider.validateToken("invalid.token.here")).isFalse();
    }
}
