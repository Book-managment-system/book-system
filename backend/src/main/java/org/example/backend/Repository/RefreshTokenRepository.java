package org.example.backend.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository for managing refresh tokens in the database.
 * Refresh tokens are stored to enable stateless authentication with token rotation.
 */
@Repository
public class RefreshTokenRepository {
    private final JdbcTemplate jdbcTemplate;

    public RefreshTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Saves a refresh token for a user.
    public int save(Integer userId, String token, LocalDateTime expiresAt) {
        String query = "INSERT INTO RefreshTokens (user_id, token, expires_at) VALUES (?, ?, ?)";
        return jdbcTemplate.update(query, userId, token, expiresAt);
    }

    // Checks if a refresh token exists in the database.
    public boolean existsByToken(String token) {
        String query = "SELECT COUNT(*) FROM RefreshTokens WHERE token = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, token);
        return count != null && count > 0;
    }

    // Deletes a refresh token from the database.
    public int deleteByToken(String token) {
        String query = "DELETE FROM RefreshTokens WHERE token = ?";
        return jdbcTemplate.update(query, token);
    }

    // Deletes all expired refresh tokens.
    public int deleteExpiredTokens() {
        String query = "DELETE FROM RefreshTokens WHERE expires_at < CURRENT_TIMESTAMP";
        return jdbcTemplate.update(query);
    }

    // Deletes refresh token for a specific user.
    public int deleteByUserId(Integer userId) {
        String query = "DELETE FROM RefreshTokens WHERE user_id = ?";
        return jdbcTemplate.update(query, userId);
    }
}
