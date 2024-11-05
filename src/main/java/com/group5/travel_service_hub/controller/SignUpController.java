package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    // ---------------------- Customer Sign-Up ----------------------

    @GetMapping("/CustomersSignUp")
    public String showCustomerSignUpForm() {
        return "frontendCode/Landing/CustomersSignUp"; // Ensure this matches your template name
    }

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

            // Redirect to log in or welcome page
            return "redirect:/CustomerLogin"; // Adjust as needed

        } catch (Exception e) {
            // Handle registration errors
            model.addAttribute("registrationError", e.getMessage());
            return "frontendCode/Landing/CustomersSignUp";
        }
    }

    // ---------------------- Provider Sign-Up ----------------------

    @GetMapping("/ProvidersSignUp")
    public String showProviderSignUpForm() {
        return "frontendCode/Landing/ProvidersSignUp"; // Ensure this matches your template name
    }

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

            // Redirect to log in or welcome page
            return "redirect:/ProviderLogin"; // Adjust as needed


        } catch (Exception e) {
            // Handle registration errors
            model.addAttribute("registrationError", e.getMessage());
            return "frontendCode/Landing/ProvidersSignUp";
        }
    }
}

