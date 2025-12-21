package org.example.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.backend.config.JwtProperties;
import org.example.backend.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private String secretKey;
    private static final long ACCESS_TOKEN_EXPIRATION_MINUTES = 15;
    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    @BeforeEach
    void setUp() {
        secretKey = "test-secret-key-that-is-at-least-256-bits-long-for-hmac-sha-algorithm-test";
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(secretKey);
        jwtProperties.setAccessTokenExpirationMinutes(ACCESS_TOKEN_EXPIRATION_MINUTES);
        jwtProperties.setRefreshTokenExpirationDays(REFRESH_TOKEN_EXPIRATION_DAYS);
        jwtService = new JwtService(jwtProperties);
    }

    @Test
    void testGenerateAccessToken_ShouldContainCorrectClaims() {
        // Given
        Integer userId = 1;
        String username = "testuser";
        Role role = Role.Customer;

        // When
        String token = jwtService.generateAccessToken(userId, username, role);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(userId, claims.get("userId", Integer.class));
        assertEquals(username, claims.getSubject());
        assertEquals(role.name(), claims.get("role", String.class));
        assertEquals("access", claims.get("type", String.class));
    }

    @Test
    void testGenerateAccessToken_ShouldExpireAfterConfiguredTime() {
        // Given
        Integer userId = 1;
        String username = "testuser";
        Role role = Role.Customer;

        // When
        String token = jwtService.generateAccessToken(userId, username, role);

        // Then
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Date expiration = claims.getExpiration();
        assertNotNull(expiration);
        
        long expirationMinutes = ChronoUnit.MINUTES.between(
                Instant.now(),
                expiration.toInstant()
        );
        
        assertTrue(expirationMinutes <= ACCESS_TOKEN_EXPIRATION_MINUTES);
        assertTrue(expirationMinutes >= ACCESS_TOKEN_EXPIRATION_MINUTES - 1);
    }

    @Test
    void testGenerateRefreshToken_ShouldContainCorrectClaims() {
        // Given
        Integer userId = 1;
        String username = "testuser";

        // When
        String token = jwtService.generateRefreshToken(userId, username);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(userId, claims.get("userId", Integer.class));
        assertEquals(username, claims.getSubject());
        assertEquals("refresh", claims.get("type", String.class));
        assertNull(claims.get("role"));
    }

    @Test
    void testGenerateRefreshToken_ShouldExpireAfterConfiguredTime() {
        // Given
        Integer userId = 1;
        String username = "testuser";

        // When
        String token = jwtService.generateRefreshToken(userId, username);

        // Then
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Date expiration = claims.getExpiration();
        assertNotNull(expiration);
        
        long expirationDays = ChronoUnit.DAYS.between(
                Instant.now(),
                expiration.toInstant()
        );
        
        assertTrue(expirationDays <= REFRESH_TOKEN_EXPIRATION_DAYS);
        assertTrue(expirationDays >= REFRESH_TOKEN_EXPIRATION_DAYS - 1);
    }

    @Test
    void testValidateToken_ShouldReturnTrueForValidToken() {
        // Given
        Integer userId = 1;
        String username = "testuser";
        Role role = Role.Customer;
        String token = jwtService.generateAccessToken(userId, username, role);

        // When
        boolean isValid = jwtService.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_ShouldReturnFalseForInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtService.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_ShouldReturnFalseForExpiredToken() {
        // Given
        String expiredToken = createExpiredToken();

        // When
        boolean isValid = jwtService.validateToken(expiredToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testExtractUsername_ShouldReturnCorrectUsername() {
        // Given
        String username = "testuser";
        String token = jwtService.generateAccessToken(1, username, Role.Customer);

        // When
        String extractedUsername = jwtService.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractUserId_ShouldReturnCorrectUserId() {
        // Given
        Integer userId = 123;
        String token = jwtService.generateAccessToken(userId, "testuser", Role.Customer);

        // When
        Integer extractedUserId = jwtService.extractUserId(token);

        // Then
        assertEquals(userId, extractedUserId);
    }

    @Test
    void testExtractRole_ShouldReturnCorrectRole() {
        // Given
        Role role = Role.Admin;
        String token = jwtService.generateAccessToken(1, "testuser", role);

        // When
        Role extractedRole = jwtService.extractRole(token);

        // Then
        assertEquals(role, extractedRole);
    }

    @Test
    void testExtractTokenType_ShouldReturnAccessForAccessToken() {
        // Given
        String token = jwtService.generateAccessToken(1, "testuser", Role.Customer);

        // When
        String type = jwtService.extractTokenType(token);

        // Then
        assertEquals("access", type);
    }

    @Test
    void testExtractTokenType_ShouldReturnRefreshForRefreshToken() {
        // Given
        String token = jwtService.generateRefreshToken(1, "testuser");

        // When
        String type = jwtService.extractTokenType(token);

        // Then
        assertEquals("refresh", type);
    }

    private String createExpiredToken() {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .subject("testuser")
                .claim("userId", 1)
                .claim("type", "access")
                .issuedAt(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)))
                .expiration(Date.from(Instant.now().minus(30, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }
}
