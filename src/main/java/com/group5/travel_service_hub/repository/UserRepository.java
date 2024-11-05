package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return The User entity if found, else null.
     */
    User findByUsername(String username);

    /**
     * Finds a user by their email.
     *
     * @param email The email to search for.
     * @return The User entity if found, else null.
     */
    User findByEmail(String email);

    /**
     * Checks if a user exists by username.
     *
     * @param username The username to check.
     * @return True if a user with the given username exists, else false.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists by email.
     *
     * @param email The email to check.
     * @return True if a user with the given email exists, else false.
     */
    boolean existsByEmail(String email);
}
