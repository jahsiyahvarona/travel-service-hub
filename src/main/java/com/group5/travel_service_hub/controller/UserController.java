package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Notification;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.NotificationRepository;
import com.group5.travel_service_hub.service.NotificationService;
import com.group5.travel_service_hub.service.UserService;
import com.group5.travel_service_hub.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

/**
 * REST Controller for managing user-related operations.
 */
@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService; // Service layer for user-related operations

    @Autowired
    private UserRepository userRepository; // Repository for direct database interaction

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

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
     * Handles profile updates for logged-in users via form submission.
     *
     * @param username   Updated username.
     * @param email      Updated email.
     * @param profilePic Updated profile picture.
     * @param session    HTTP session for retrieving the logged-in user.
     * @param redirectAttributes Redirect attributes for status messages.
     * @return Redirect to the provider's profile page.
     */
    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String loggedInUsername = loggedInUser.getUsername();
        User user = userService.findByUsername(loggedInUsername);

        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can update packages.");
            return "redirect:/provider/profile";
        }

        try {
            if (user.getUsername().equals(username) && user.getEmail().equals(email) && profilePic.isEmpty()) {
                return "redirect:/provider/profile";
            }

            user.setUsername(username);
            user.setEmail(email);
            userService.updateProfile(user.getId(), user);

            if (profilePic != null && !profilePic.isEmpty()) {
                userService.updateProfilePic(user.getId(), profilePic);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
            session.setAttribute("loggedInUser", user);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/provider/profile";
    }

    /**
     * Handles password changes for logged-in users.
     *
     * @param currentPassword The current password.
     * @param newPassword     The new password.
     * @param session         HTTP session for retrieving the logged-in user.
     * @param redirectAttributes Redirect attributes for status messages.
     * @return Redirect to the provider's profile page.
     */
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/ProviderLogin";
        }

        try {
            userService.updatePassword(loggedInUser.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to change password: " + e.getMessage());
        }

        return "redirect:/provider/profile";
    }

    @GetMapping("/notifications/{id}/markAsRead")
    public String markNotificationAsRead(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Adjust as necessary
        }
        Notification notification = notificationRepository.getById(id);
        if (notification != null && notification.getNotifee().getId().equals(user.getId())) {
            notificationService.markAsRead(notification);
            // Redirect to the notification's target URL
            return "redirect:" + notification.getTargetUrl();
        }
        return "redirect:/";
    }
}
