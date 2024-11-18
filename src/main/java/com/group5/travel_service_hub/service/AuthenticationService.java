package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service class for handling authentication-related operations.
 * Supports authentication for customers, providers, and sysadmins.
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticate a customer by verifying their username and password.
     *
     * @param username The username of the customer.
     * @param password The password of the customer.
     * @return The authenticated User object if credentials are valid, null otherwise.
     */
    public User authenticate(String username, String password) {
        // Fetch the user from the repository by username
        User user = userRepository.findByUsername(username);

        // Verify if the user exists and has the role CUSTOMER
        if (user != null && user.getRole() == Role.CUSTOMER) {
            // Hash the input password
            String hashedPassword = hashPassword(password);

            // Compare the hashed input password with the stored password
            if (user.getPassword().equals(hashedPassword)) {
                return user; // Authentication successful
            }
        }

        return null; // Authentication failed
    }

    /**
     * Authenticate a provider by verifying their username and password.
     *
     * @param username The username of the provider.
     * @param password The password of the provider.
     * @return The authenticated User object if credentials are valid, null otherwise.
     */
    public User authenticate2(String username, String password) {
        // Fetch the user from the repository by username
        User user = userRepository.findByUsername(username);

        // Verify if the user exists and has the role PROVIDER
        if (user != null && user.getRole() == Role.PROVIDER) {
            // Hash the input password
            String hashedPassword = hashPassword(password);

            // Compare the hashed input password with the stored password
            if (user.getPassword().equals(hashedPassword)) {
                return user; // Authentication successful
            }
        }

        return null; // Authentication failed
    }

    /**
     * Authenticate a sysadmin by verifying their username and password.
     *
     * @param username The username of the sysadmin.
     * @param password The password of the sysadmin.
     * @return The authenticated User object if credentials are valid, null otherwise.
     */
    public User authenticate3(String username, String password) {
        // Fetch the user from the repository by username
        User user = userRepository.findByUsername(username);

        // Verify if the user exists and has the role SYSADMIN
        if (user != null && user.getRole() == Role.SYSADMIN) {
            // Hash the input password
            String hashedPassword = hashPassword(password);

            // Compare the hashed input password with the stored password
            if (user.getPassword().equals(hashedPassword)) {
                return user; // Authentication successful
            }
        }

        return null; // Authentication failed
    }

    /**
     * Hashes the password using the SHA-256 hashing algorithm.
     *
     * @param password The plain text password to be hashed.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            // Initialize the SHA-256 MessageDigest
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Generate the hashed bytes
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // Convert each byte to hexadecimal
            }
            return sb.toString(); // Return the hashed password
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception or rethrow it as a runtime exception
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
