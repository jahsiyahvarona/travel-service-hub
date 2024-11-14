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

@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    /**
     * Display the customer's login page.
     *
     * @return The customer login page template.
     */
    @GetMapping("/CustomerLogin")
    public String showCustomerLoginPage() {
        return "frontendCode/Landing/CustomerLogin"; // Ensure this template exists
    }

    /**
     * Handle customer login requests.
     *
     * @param username           The username entered by the user.
     * @param password           The password entered by the user.
     * @param request            The HTTP request to store user data.
     * @param redirectAttributes Attributes for flash messages.
     * @return Redirect to the appropriate dashboard based on the login result.
     */
    @PostMapping("/CustomerLogin")
    public String loginCustomer(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        User user = authenticationService.authenticate(username, password);

        if (user != null) {
            // Invalidate the old session to prevent session fixation
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            if(!user.isActive()){
                redirectAttributes.addFlashAttribute("error2", "account is suspended.");

                return "redirect:/CustomerLogin?error2";
            }

            // Create a new session
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("loggedInUser", user);


            return "redirect:/CostumerDashboard";
        } else {
            // Redirect to login page with error if login fails
            redirectAttributes.addFlashAttribute("error", "Invalid username or password for Provider.");
            return "redirect:/CustomerLogin?error";
        }
    }

    /**
     * Display the provider's login page.
     *
     * @return The provider login page template.
     */
    @GetMapping("/ProviderLogin")
    public String showProviderLoginPage() {
        return "frontendCode/Landing/ProvidersLogin"; // Ensure this template exists
    }

    /**
     * Handle provider login requests.
     *
     * @param username           The username entered by the user.
     * @param password           The password entered by the user.
     * @param request           The HTTP request  to store user data.
     * @param redirectAttributes Attributes for flash messages.
     * @return Redirect to the appropriate dashboard based on the login result.
     */
    @PostMapping("/ProviderLogin")
    public String loginProvider(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request, // Inject HttpServletRequest to create a new session
                                RedirectAttributes redirectAttributes) {

        User user = authenticationService.authenticate2(username, password);

        if (user != null) {
            // Invalidate the old session to prevent session fixation
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            if(!user.isActive()){
                redirectAttributes.addFlashAttribute("error2", "account is suspended.");

                return "redirect:/ProviderLogin";
            }

            // Create a new session
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("loggedInUser", user);


            return "redirect:/ProviderDashboard";
        } else {
            // Redirect to login page with error if login fails
            redirectAttributes.addFlashAttribute("error", "Invalid username or password for Provider.");
            return "redirect:/ProviderLogin?error";
        }
    }

    /**
     * Display the sysadmin's login page.
     *
     * @return The sysadmin login page template.
     */
    @GetMapping("/SysAdminLogin")
    public String showSysAdminLoginPage() {
        return "frontendCode/Landing/SysAdminLogin"; // Ensure this template exists
    }

    /**
     * Handle sysadmin login requests.
     *
     * @param username           The username entered by the user.
     * @param password           The password entered by the user.
     * @param request            The HTTP request  to store user data.
     * @param redirectAttributes Attributes for flash messages.
     * @return Redirect to the appropriate dashboard based on the login result.
     */
    @PostMapping("/SysAdminLogin")
    public String loginSysAdmin(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        User user = authenticationService.authenticate3(username, password);

        if (user != null) {
            // Invalidate the old session to prevent session fixation
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create a new session
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("loggedInUser", user);


            return "redirect:/SysAdminDashboard";
        } else {
            // Redirect to login page with error if login fails
            redirectAttributes.addFlashAttribute("error", "Invalid username or password for Provider.");
            return "redirect:/SysAdminLogin?error";
        }
    }
}
