package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.AuthenticationService;
import com.group5.travel_service_hub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for managing login-related actions for different user roles.
 */
@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService; // Handles user authentication logic

    @Autowired
    private UserService userService; // Handles user-related services

    /**
     * Displays the customer login page.
     *
     * @return The path to the customer login template.
     */
    @GetMapping("/CustomerLogin")
    public String showCustomerLoginPage() {
        return "frontendCode/Landing/CustomerLogin"; // Maps to the customer login template
    }

    /**
     * Handles login for customers.
     *
     * @param username           Customer's username.
     * @param password           Customer's password.
     * @param request            The HTTP request to manage session.
     * @param redirectAttributes Flash attributes for passing feedback messages.
     * @return Redirect to the customer dashboard if successful, or back to the login page on failure.
     */
    @PostMapping("/CustomerLogin")
    public String loginCustomer(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        User user = authenticationService.authenticate(username, password);

        if (user != null) {
            // Invalidate the previous session to prevent session fixation attacks
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            if (!user.isActive()) {
                // If account is suspended, provide feedback to the user
                redirectAttributes.addFlashAttribute("error2", "Account is suspended.");
                return "redirect:/CustomerLogin?error2";
            }

            // Create a new session for the authenticated user
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("loggedInUser", user);

            // Redirect to the customer dashboard
            return "redirect:/CostumerDashboard";
        } else {
            // Provide error feedback if authentication fails
            redirectAttributes.addFlashAttribute("error", "Invalid username or password for customer.");
            return "redirect:/CustomerLogin?error";
        }
    }

    /**
     * Displays the provider login page.
     *
     * @return The path to the provider login template.
     */
    @GetMapping("/ProviderLogin")
    public String showProviderLoginPage() {
        return "frontendCode/Landing/ProvidersLogin"; // Maps to the provider login template
    }

    /**
     * Handles login for providers.
     *
     * @param username           Provider's username.
     * @param password           Provider's password.
     * @param request            The HTTP request to manage session.
     * @param redirectAttributes Flash attributes for passing feedback messages.
     * @return Redirect to the provider dashboard if successful, or back to the login page on failure.
     */
    @PostMapping("/ProviderLogin")
    public String loginProvider(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        User user = authenticationService.authenticate2(username, password);

        if (user != null) {
            // Invalidate the previous session to prevent session fixation attacks
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            if (!user.isActive()) {
                // If account is suspended, provide feedback to the user
                redirectAttributes.addFlashAttribute("error2", "Account is suspended.");
                return "redirect:/ProviderLogin";
            }

            // Create a new session for the authenticated user
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("loggedInUser", user);
            int timeoutInSeconds = 30 * 60; // For a 30-minute timeout
            newSession.setMaxInactiveInterval(timeoutInSeconds);

            // Redirect to the provider dashboard
            return "redirect:/ProviderDashboard";
        } else {
            // Provide error feedback if authentication fails
            redirectAttributes.addFlashAttribute("error", "Invalid username or password for provider.");
            return "redirect:/ProviderLogin?error";
        }
    }

    /**
     * Displays the sysadmin login page.
     *
     * @return The path to the sysadmin login template.
     */
    @GetMapping("/SysAdminLogin")
    public String showSysAdminLoginPage() {
        return "frontendCode/Landing/SysAdminLogin"; // Maps to the sysadmin login template
    }

    /**
     * Handles login for sysadmins.
     *
     * @param username           Sysadmin's username.
     * @param password           Sysadmin's password.
     * @param request            The HTTP request to manage session.
     * @param redirectAttributes Flash attributes for passing feedback messages.
     * @return Redirect to the sysadmin dashboard if successful, or back to the login page on failure.
     */
    @PostMapping("/SysAdminLogin")
    public String loginSysAdmin(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        User user = authenticationService.authenticate3(username, password);

        if (user != null) {
            // Invalidate the previous session to prevent session fixation attacks
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create a new session for the authenticated user
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("loggedInUser", user);

            // Redirect to the sysadmin dashboard
            return "redirect:/SysAdminDashboard";
        } else {
            // Provide error feedback if authentication fails
            redirectAttributes.addFlashAttribute("error", "Invalid username or password for sysadmin.");
            return "redirect:/SysAdminLogin?error";
        }
    }
}
