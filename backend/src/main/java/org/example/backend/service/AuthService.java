package org.example.backend.service;

import org.example.backend.Repository.RefreshTokenRepository;
import org.example.backend.Repository.UserRepository;
import org.example.backend.model.dto.LoginRequest;
import org.example.backend.model.dto.LoginResponse;
import org.example.backend.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Service for handling authentication operations including login, token refresh, and logout.
 * Implements JWT-based stateless authentication with refresh token rotation.
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Authenticates a user and generates access and refresh tokens.
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getUserId(), user.getUsername(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getUserId(), user.getUsername());

        // Calculate expiration time for refresh token (7 days from now)
        LocalDateTime expiresAt = LocalDateTime.now().plus(7, ChronoUnit.DAYS);
        refreshTokenRepository.save(user.getUserId(), refreshToken, expiresAt);

        return new LoginResponse(accessToken, refreshToken);
    }


    // Refreshes an access token using a valid refresh token.
    // Implements token rotation: old refresh token is deleted and a new one is issued.
    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String tokenType = jwtService.extractTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Integer userId = jwtService.extractUserId(refreshToken);
        String username = jwtService.extractUsername(refreshToken);

        // Verify the refresh token exists in the database
        if (!refreshTokenRepository.existsByToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token not found or already used");
        }

        // Get user to retrieve role
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Extract expiration date from the old refresh token
        Date oldTokenExpiration = jwtService.extractExpiration(refreshToken);
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                oldTokenExpiration.toInstant(),
                ZoneId.systemDefault()
        );

        // Token rotation: delete old refresh token and issue new ones
        refreshTokenRepository.deleteByToken(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(userId, username, user.getRole());
        String newRefreshToken = jwtService.generateRefreshToken(userId, username, oldTokenExpiration);

        refreshTokenRepository.save(userId, newRefreshToken, expiresAt);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    // Logs out a user by deleting their refresh token from the database.
    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            refreshTokenRepository.deleteByToken(refreshToken);
        }
    }
}
