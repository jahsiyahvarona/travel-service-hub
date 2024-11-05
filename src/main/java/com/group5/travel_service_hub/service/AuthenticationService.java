package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticate a customer by username and password.
     *
     * @param username The username of the customer.
     * @param password The password of the customer.
     * @return The authenticated User object if successful, null otherwise.
     */
    public User authenticate(String username, String password) {
        // Find the user by username
        User user = userRepository.findByUsername(username);

        if (user != null && user.getRole() == Role.CUSTOMER) {
            // Hash the input password
            String hashedPassword = hashPassword(password);

            // Check if the hashed password matches the stored password
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Authenticate a provider by username and password.
     *
     * @param username The username of the provider.
     * @param password The password of the provider.
     * @return The authenticated User object if successful, null otherwise.
     */
    public User authenticate2(String username, String password) {
        // Find the user by username
        User user = userRepository.findByUsername(username);

        if (user != null && user.getRole() == Role.PROVIDER) {
            // Hash the input password
            String hashedPassword = hashPassword(password);

            // Check if the hashed password matches the stored password
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Authenticate a sysadmin by username and password.
     *
     * @param username The username of the sysadmin.
     * @param password The password of the sysadmin.
     * @return The authenticated User object if successful, null otherwise.
     */
    public User authenticate3(String username, String password) {
        // Find the user by username
        User user = userRepository.findByUsername(username);

        if (user != null && user.getRole() == Role.SYSADMIN) {
            // Hash the input password
            String hashedPassword = hashPassword(password);

            // Check if the hashed password matches the stored password
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Hashes the password using SHA-256 algorithm.
     *
     * @param password The password to be hashed.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into signum representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // Converts to hexadecimal
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // Handle the exception or rethrow it
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
