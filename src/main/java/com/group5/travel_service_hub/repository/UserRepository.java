package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.AccountStatus;
import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for performing CRUD operations on the User entity.
 * Extends JpaRepository to provide built-in methods for database interaction.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user entity by their unique username.
     *
     * @param username The username of the user to retrieve.
     * @return The User entity if found; otherwise, returns null.
     */
    User findByUsername(String username);

    /**
     * Retrieves a user entity by their unique email.
     *
     * @param email The email of the user to retrieve.
     * @return The User entity if found; otherwise, returns null.
     */
    User findByEmail(String email);

    /**
     * Checks if a user exists with the specified username.
     *
     * @param username The username to check.
     * @return True if a user with the given username exists, else false.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the specified email.
     *
     * @param email The email to check.
     * @return True if a user with the given email exists, else false.
     */
    boolean existsByEmail(String email);

    // Methods for SysAdmin Functionality

    /**
     * Retrieves all users with a specified role.
     *
     * @param role The role to filter users by (e.g., CUSTOMER, PROVIDER, SYSADMIN).
     * @return A list of users with the specified role.
     */
    List<User> findByRole(Role role);

    /**
     * Retrieves users based on their banned status.
     *
     * @param banned True if retrieving banned users; false for non-banned users.
     * @return A list of users matching the specified banned status.
     */
    List<User> findByBanned(boolean banned);

    /**
     * Retrieves users by their active status.
     *
     * @param active True to retrieve active users; false to retrieve inactive users.
     * @return A list of users with the specified active status.
     */
    List<User> findByActive(boolean active);
}
