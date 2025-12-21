package org.example.backend.Repository;

import org.example.backend.model.entity.User;
import org.example.backend.model.enums.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public boolean existsByUsername(String username){
        String query = " select count(*) from users where username = ?";
        Integer count = jdbcTemplate.queryForObject(query,Integer.class,username);
        if(count == null||count ==0) return false;
        return true;
    }
    public boolean existsByEmail (String email){
        String query = " select count(*) from users where email = ?";
        Integer count = jdbcTemplate.queryForObject(query,Integer.class,email);
        if(count == null||count ==0) return false;
        return true;
    }
    public User save(User user){
        String query = "insert into users (username, password, first_name, last_name, email, phone, shipping_address, role)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?::role_enum) returning user_id, created_at";
        User returned = jdbcTemplate.queryForObject(query, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                user.setUserId(rs.getInt("user_id"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return user;
            }
        }, user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getShippingAddress(),
                user.getRole().name());
        return returned;
    }
    // update, delete but when we work on the crud
    public int update(User user){
        String sql = "update users set username = ?, password = ?, first_name = ?, last_name = ?, "+
                "role = ?,email = ?, phone = ?, shipping_address = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql,user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getEmail(),
                user.getPhone(),
                user.getShippingAddress(),
                user.getUserId()
        );
    }

    /**
     * Finds a user by username.
     *
     * @param username The username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> findByUsername(String username) {
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, shipping_address, role, created_at " +
                "FROM Users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(query, userRowMapper(), username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Finds a user by user ID.
     *
     * @param userId The user ID to search for
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> findById(Integer userId) {
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, shipping_address, role, created_at " +
                "FROM Users WHERE user_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(query, userRowMapper(), userId);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * RowMapper for mapping ResultSet to User entity.
     */
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            Integer userId = rs.getInt("user_id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String shippingAddress = rs.getString("shipping_address");
            Role role = Role.valueOf(rs.getString("role"));
            Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
            java.time.LocalDateTime createdAt = createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null;

            return new User(userId, username, password, firstName, lastName, email, shippingAddress, phone, createdAt, role);
        };
    }
}
