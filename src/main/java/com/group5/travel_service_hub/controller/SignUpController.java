package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for managing user sign-up processes for customers and providers.
 */
@Controller
public class SignUpController {

    @Autowired
    private UserService userService; // Service for managing user-related operations

    // ---------------------- Customer Sign-Up ----------------------

    /**
     * Displays the Customer Sign-Up form.
     *
     * @return The Customer Sign-Up form template.
     */
    @GetMapping("/CustomersSignUp")
    public String showCustomerSignUpForm() {
        return "frontendCode/Landing/CustomersSignUp"; // Ensure this matches your template name
    }

    /**
     * Handles the Customer Sign-Up form submission.
     *
     * @param username        The username entered by the customer.
     * @param email           The email address entered by the customer.
     * @param password        The password entered by the customer.
     * @param confirmPassword The confirmation password entered by the customer.
     * @param profilePic      An optional profile picture uploaded by the customer.
     * @param model           The model for passing attributes to the view.
     * @return Redirects to the login page if successful, otherwise returns to the sign-up page with errors.
     */
    @PostMapping("/CustomersSignUp")
    public String registerCustomer(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
            Model model) {

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("registrationError", "Passwords do not match");
            return "frontendCode/Landing/CustomersSignUp";
        }

        // Check if username already exists
        if (userService.usernameExists(username)) {
            model.addAttribute("registrationError", "Username already exists");
            return "frontendCode/Landing/CustomersSignUp";
        }

        // Check if email already exists
        if (userService.emailExists(email)) {
            model.addAttribute("registrationError", "Email already exists");
            return "frontendCode/Landing/CustomersSignUp";
        }

        // Create a new User object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.CUSTOMER);

        try {
            // Register the user
            User registeredUser = userService.registerUser(user);

            // Handle profile picture upload
            if (profilePic != null && !profilePic.isEmpty()) {
                userService.updateProfilePic(registeredUser.getId(), profilePic);
            }

            // Redirect to the customer login page
            return "redirect:/CustomerLogin";

        } catch (Exception e) {
            // Handle registration errors
            model.addAttribute("registrationError", e.getMessage());
            return "frontendCode/Landing/CustomersSignUp";
        }
    }

    // ---------------------- Provider Sign-Up ----------------------

    /**
     * Displays the Provider Sign-Up form.
     *
     * @return The Provider Sign-Up form template.
     */
    @GetMapping("/ProvidersSignUp")
    public String showProviderSignUpForm() {
        return "frontendCode/Landing/ProvidersSignUp"; // Ensure this matches your template name
    }

    /**
     * Handles the Provider Sign-Up form submission.
     *
     * @param username        The username entered by the provider.
     * @param email           The email address entered by the provider.
     * @param password        The password entered by the provider.
     * @param confirmPassword The confirmation password entered by the provider.
     * @param profilePic      An optional profile picture uploaded by the provider.
     * @param model           The model for passing attributes to the view.
     * @return Redirects to the provider login page if successful, otherwise returns to the sign-up page with errors.
     */
    @PostMapping("/ProvidersSignUp")
    public String registerProvider(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
            Model model) {

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("registrationError", "Passwords do not match");
            return "frontendCode/Landing/ProvidersSignUp";
        }

        // Check if username already exists
        if (userService.usernameExists(username)) {
            model.addAttribute("registrationError", "Username already exists");
            return "frontendCode/Landing/ProvidersSignUp";
        }

        // Check if email already exists
        if (userService.emailExists(email)) {
            model.addAttribute("registrationError", "Email already exists");
            return "frontendCode/Landing/ProvidersSignUp";
        }

        // Create a new User object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.PROVIDER);

        try {
            // Register the user
            User registeredUser = userService.registerUser(user);

            // Handle profile picture upload
            if (profilePic != null && !profilePic.isEmpty()) {
                userService.updateProfilePic(registeredUser.getId(), profilePic);
            }

            // Redirect to the provider login page
            return "redirect:/ProviderLogin";

        } catch (Exception e) {
            // Handle registration errors
            model.addAttribute("registrationError", e.getMessage());
            return "frontendCode/Landing/ProvidersSignUp";
        }
    }
}
