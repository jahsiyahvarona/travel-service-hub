package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.AccountStatus;
import com.group5.travel_service_hub.entity.Report;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Inject the upload directory from application.properties
    @Autowired
    private Environment env;

    /**
     * Registers a new user.
     *
     * @param user The user to register.
     * @return The registered user.
     */
    @Transactional
    public User registerUser(User user) {
        // Check if username already exists
        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        // Check if email already exists
        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // Hash the password before saving
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Ensure the user is active upon registration
        user.setActive(true);
        user.setBanned(false);

        return userRepository.save(user);
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password The plain text password.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array.
     * @return The hexadecimal string.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Checks if a username already exists.
     *
     * @param username The username to check.
     * @return True if exists, false otherwise.
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if an email already exists.
     *
     * @param email The email to check.
     * @return True if exists, false otherwise.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Activates a user's account.
     *
     * @param userId The ID of the user to activate.
     * @return The activated user.
     */
    @Transactional
    public User activateUser(Long userId) {
        User user = findById(userId);
        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Deactivates a user's account.
     *
     * @param userId The ID of the user to deactivate.
     * @return The deactivated user.
     */
    @Transactional
    public User deactivateUser(Long userId) {
        User user = findById(userId);
        user.setActive(false);
        return userRepository.save(user);
    }

    /**
     * Bans a user.
     *
     * @param userId The ID of the user to ban.
     * @return The banned user.
     */
    @Transactional
    public User banUser(Long userId) {
        User user = findById(userId);
        user.setBanned(true);
        user.setActive(false); // Optionally deactivate when banning
        return userRepository.save(user);
    }

    /**
     * Sends a warning to the user.
     *
     * @param user   The user to warn.
     * @param report The associated report.
     */
    public void sendWarning(User user, Report report) {
        // Log the warning action or send an email notification
        System.out.println("Warning issued to user: " + user.getUsername() +
                " for reason: " + report.getReason());
    }

    /**
     * Unbans a user.
     *
     * @param userId The ID of the user to unban.
     * @return The unbanned user.
     */
    @Transactional
    public User unbanUser(Long userId) {
        User user = findById(userId);
        user.setBanned(false);
        user.setActive(true); // Optionally activate when unbanning
        return userRepository.save(user);
    }

    /**
     * Finds a user by username.
     *
     * @param username The username.
     * @return The user.
     */
    public User findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Finds a user by ID.
     *
     * @param userId The user ID.
     * @return The user.
     */
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Retrieves all users.
     *
     * @return List of all users.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates the authenticated user's password.
     *
     * @param userId      The ID of the user.
     * @param oldPassword The current password.
     * @param newPassword The new desired password.
     * @return The updated user.
     * @throws IllegalArgumentException If the user is not found or old password does not match.
     */
    @Transactional
    public User updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = findById(userId);

        // Verify that the old password matches
        String oldPasswordHash = hashPassword(oldPassword);
        if (!user.getPassword().equals(oldPasswordHash)) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        // Update to the new password
        String newHashedPassword = hashPassword(newPassword);
        user.setPassword(newHashedPassword);
        return userRepository.save(user);
    }

    /**
     * Updates the authenticated user's profile.
     *
     * @param userId      The ID of the user.
     * @param updatedUser The user entity containing updated profile details.
     * @return The updated user.
     */
    @Transactional
    public User updateProfile(Long userId, User updatedUser) {
        User existingUser = findById(userId);

        // Update only allowed fields (e.g., username, email)
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            // Check if the new username is already taken by another user
            if (!existingUser.getUsername().equals(updatedUser.getUsername()) && usernameExists(updatedUser.getUsername())) {
                throw new IllegalArgumentException("Username already exists.");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            // Check if the new email is already taken by another user
            if (!existingUser.getEmail().equals(updatedUser.getEmail()) && emailExists(updatedUser.getEmail())) {
                throw new IllegalArgumentException("Email already exists.");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        // Add other fields that can be updated as needed, excluding role, active, banned, etc.

        return userRepository.save(existingUser);
    }

    /**
     * Updates the authenticated user's profile picture by handling file upload.
     *
     * @param userId The ID of the user.
     * @param file   The profile picture file.
     * @return The updated user.
     */
    @Transactional
    public User updateProfilePic(Long userId, MultipartFile file) {
        User user = findById(userId);

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        // Optionally, validate file type (e.g., only allow JPEG and PNG)
        String contentType = file.getContentType();
        if (contentType == null || !isImage(contentType)) {
            throw new IllegalArgumentException("Only image files (JPEG, PNG) are allowed.");
        }

        // Normalize file name
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Generate a unique file name to prevent collisions
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;

        // Get the upload directory from properties
        String uploadDir = env.getProperty("file.upload-dir");

        try {
            if (uploadDir == null) {
                throw new IllegalArgumentException("Upload directory not configured.");
            }

            Path uploadPath = Paths.get(uploadDir);

            // Create directories if not exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set the profilePic field (URL)
            String fileUrl = "/uploads/" + fileName;
            user.setProfilePic(fileUrl);

            return userRepository.save(user);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not store file. Please try again!", ex);
        }
    }

    /**
     * Checks if the given content type is an image.
     *
     * @param contentType The content type to check.
     * @return True if it's an image; otherwise, false.
     */
    private boolean isImage(String contentType) {
        return contentType.equalsIgnoreCase("image/jpeg") ||
                contentType.equalsIgnoreCase("image/png") ||
                contentType.equalsIgnoreCase("image/jpg");
    }

    /**
     * Extracts the file extension from the filename.
     *
     * @param filename The filename.
     * @return The file extension.
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        String[] parts = filename.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }

    /**
     * Retrieves a customer User by their ID.
     *
     * @param userId The ID of the user.
     * @return The User if found.
     * @throws IllegalArgumentException if the user is not found.
     */
    public User getCustomerUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }




    //SysAdmin
    /**
     * Deletes a user by ID.
     *
     * @param userId The ID of the user to delete.
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Updates the account status of a user.
     *
     * @param userId The ID of the user.
     * @param status The new account status (e.g., "active", "banned").
     * @return The updated user.
     */
    @Transactional
    public User updateAccountStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

       // user.setAccountStatus(status);
        return userRepository.save(user);
    }


    public List<User> getAllBannedUsers(){
        return userRepository.findByBanned(true);
    }

    public List<User> getUsersByAccountStats(String status){
      //  return userRepository.findByAccountStatus(AccountStatus.valueOf(status));
        return List.of();
    }

}


