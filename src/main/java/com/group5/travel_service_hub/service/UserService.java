package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Report;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Service class for managing users.
 * Includes user registration, account management, profile updates, and administrative actions.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    /**
     * Registers a new user in the system.
     * Ensures unique username and email, and hashes the user's password before saving.
     *
     * @param user The user to register.
     * @return The registered User object.
     * @throws IllegalArgumentException if the username or email already exists.
     */
    @Transactional
    public User registerUser(User user) {
        if (usernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        user.setPassword(hashPassword(user.getPassword()));
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
     * Checks if a username already exists in the system.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if an email already exists in the system.
     *
     * @param email The email to check.
     * @return True if the email exists, false otherwise.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Activates a user's account by setting the active flag to true.
     *
     * @param userId The ID of the user to activate.
     * @return The updated User object.
     */
    @Transactional
    public User activateUser(Long userId) {
        User user = findById(userId);
        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Deactivates a user's account by setting the active flag to false.
     *
     * @param userId The ID of the user to deactivate.
     * @return The updated User object.
     */
    @Transactional
    public User deactivateUser(Long userId) {
        User user = findById(userId);
        user.setActive(false);
        return userRepository.save(user);
    }

    /**
     * Bans a user by setting the banned flag to true and deactivates the account.
     *
     * @param userId The ID of the user to ban.
     * @return The updated User object.
     */
    @Transactional
    public User banUser(Long userId) {
        User user = findById(userId);
        user.setBanned(true);
        user.setActive(false);
        return userRepository.save(user);
    }

    /**
     * Sends a warning to a user based on a report.
     *
     * @param user   The User object to warn.
     * @param report The Report object containing the warning details.
     */
    public void sendWarning(User user, Report report) {
        System.out.println("Warning issued to user: " + user.getUsername() +
                " for reason: " + report.getReason());
    }

    /**
     * Unbans a user by setting the banned flag to false and activates the account.
     *
     * @param userId The ID of the user to unban.
     * @return The updated User object.
     */
    @Transactional
    public User unbanUser(Long userId) {
        User user = findById(userId);
        user.setBanned(false);
        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return The User object if found.
     * @throws IllegalArgumentException if the user is not found.
     */
    public User findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId The ID of the user to search for.
     * @return The User object if found.
     * @throws IllegalArgumentException if the user is not found.
     */
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all User objects.
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
     * @throws IllegalArgumentException If the user is not found or the old password does not match.
     */
    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        // Find the user by ID
        User user = findById(userId);

        // Hash the old password provided by the user
        String oldPasswordHash = hashPassword(oldPassword);

        // Check if the hashed old password matches the stored password
        if (!user.getPassword().equals(oldPasswordHash)) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // Hash the new password
        String newPasswordHash = hashPassword(newPassword);

        // Check if the new password is different from the current password
        if (user.getPassword().equals(newPasswordHash)) {
            throw new IllegalArgumentException("New password must be different from the current password.");
        }

        // Update the password
        user.setPassword(newPasswordHash);

        // Save the updated user
        userRepository.save(user);
    }

    /**
     * Updates a user's profile information such as username and email.
     *
     * @param userId      The ID of the user.
     * @param updatedUser The User object containing the updated details.
     * @return The updated User object.
     */
    @Transactional
    public User updateProfile(Long userId, User updatedUser) {
        User existingUser = findById(userId);

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            if (!existingUser.getUsername().equals(updatedUser.getUsername()) && usernameExists(updatedUser.getUsername())) {
                throw new IllegalArgumentException("Username already exists.");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            if (!existingUser.getEmail().equals(updatedUser.getEmail()) && emailExists(updatedUser.getEmail())) {
                throw new IllegalArgumentException("Email already exists.");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        return userRepository.save(existingUser);
    }

    /**
     * Updates a user's profile picture by uploading a new file.
     *
     * @param userId The ID of the user.
     * @param file   The new profile picture file.
     * @return The updated User object.
     */
    @Transactional
    public User updateProfilePic(Long userId, MultipartFile file) {
        User user = findById(userId);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isImage(contentType)) {
            throw new IllegalArgumentException("Only image files (JPEG, PNG) are allowed.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID() + "." + fileExtension;
        String uploadDir = env.getProperty("file.upload-dir");

        try {
            if (uploadDir == null) {
                throw new IllegalArgumentException("Upload directory not configured.");
            }

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfilePic("/uploads/" + fileName);
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
     * Extracts the file extension from a filename.
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


