package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.UserService;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * REST Controller for managing user-related operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user.
     *
     * @param user The user entity containing registration details.
     * @return The registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Retrieves all users.
     * Accessible only by users with the SYSADMIN role.
     *
     * @return List of all users.
     */

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their ID.
     * Accessible by SYSADMIN or the user themselves.
     *
     * @param userId The ID of the user.
     * @return The user entity.
     */

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Activates a user's account.
     * Accessible only by users with the SYSADMIN role.
     *
     * @param userId The ID of the user to activate.
     * @return The activated user.
     */

    @PostMapping("/{userId}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long userId) {
        User activatedUser = userService.activateUser(userId);
        return ResponseEntity.ok(activatedUser);
    }

    /**
     * Deactivates a user's account.
     * Accessible only by users with the SYSADMIN role.
     *
     * @param userId The ID of the user to deactivate.
     * @return The deactivated user.
     */

    @PostMapping("/{userId}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long userId) {
        User deactivatedUser = userService.deactivateUser(userId);
        return ResponseEntity.ok(deactivatedUser);
    }

    /**
     * Bans a user.
     * Accessible only by users with the SYSADMIN role.
     *
     * @param userId The ID of the user to ban.
     * @return The banned user.
     */

    @PostMapping("/{userId}/ban")
    public ResponseEntity<User> banUser(@PathVariable Long userId) {
        User bannedUser = userService.banUser(userId);
        return ResponseEntity.ok(bannedUser);
    }

    /**
     * Unbans a user.
     * Accessible only by users with the SYSADMIN role.
     *
     * @param userId The ID of the user to unban.
     * @return The unbanned user.
     */

    @PostMapping("/{userId}/unban")
    public ResponseEntity<User> unbanUser(@PathVariable Long userId) {
        User unbannedUser = userService.unbanUser(userId);
        return ResponseEntity.ok(unbannedUser);
    }

    /**
     * Retrieves the authenticated user's details.
     * Accessible by authenticated users.
     *
     * @param principal The security principal of the authenticated user.
     * @return The user entity.
     */

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser(Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Updates the authenticated user's profile.
     * Accessible by authenticated users.
     *
     * @param principal   The security principal of the authenticated user.
     * @param updatedUser The user entity containing updated profile details.
     * @return The updated user entity.
     */

    @PutMapping("/me/profile")
    public ResponseEntity<User> updateProfile(Principal principal, @RequestBody User updatedUser) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        User updated = userService.updateProfile(user.getId(), updatedUser);
        return ResponseEntity.ok(updated);
    }

    /**
     * Updates the authenticated user's password.
     * Accessible by authenticated users.
     *
     * @param principal    The security principal of the authenticated user.
     * @param oldPassword  The current password.
     * @param newPassword  The new desired password.
     * @return The updated user entity.
     */

    @PutMapping("/me/password")
    public ResponseEntity<User> updatePassword(Principal principal,
                                               @RequestParam String oldPassword,
                                               @RequestParam String newPassword) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        User updatedUser = userService.updatePassword(user.getId(), oldPassword, newPassword);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Updates the authenticated user's email.
     * Accessible by authenticated users.
     *
     * @param principal The security principal of the authenticated user.
     * @param newEmail  The new email address.
     * @return The updated user entity.
     */

    @PutMapping("/me/email")
    public ResponseEntity<User> updateEmail(Principal principal,
                                            @RequestParam String newEmail) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        user.setEmail(newEmail);
        User updatedUser = userService.updateProfile(user.getId(), user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Updates the authenticated user's profile picture by handling file upload.
     * Accessible by authenticated users.
     *
     * @param principal The security principal of the authenticated user.
     * @param file      The profile picture file.
     * @return The updated user entity.
     */

    @PutMapping(value = "/me/profile-pic", consumes = {"multipart/form-data"})
    public ResponseEntity<User> updateProfilePic(Principal principal,
                                                 @RequestParam("file") MultipartFile file) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        User updatedUser = userService.updateProfilePic(user.getId(), file);
        return ResponseEntity.ok(updatedUser);
    }

    // Additional endpoints can be added here as needed
}