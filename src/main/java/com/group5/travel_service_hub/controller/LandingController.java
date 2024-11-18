package com.group5.travel_service_hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle requests for landing pages and basic site navigation.
 */
@Controller
public class LandingController {

    /**
     * Displays the welcome page.
     *
     * @return The path to the welcome page template.
     */
    @GetMapping("/welcome")
    public String welcomePage() {
        return "frontendCode/Landing/WelcomePage"; // Maps to resources/templates/frontendCode/Landing/WelcomePage.html
    }

    /**
     * Displays the signup choice page.
     *
     * @return The path to the signup choice page template.
     */
    @GetMapping("/signUpChoice")
    public String signUpChoicePage() {
        return "frontendCode/Landing/signUpChoice"; // Maps to resources/templates/frontendCode/Landing/signUpChoice.html
    }

    /**
     * Displays the "About Us" page.
     *
     * @return The path to the "About Us" page template.
     */
    @GetMapping("/about")
    public String aboutPage() {
        return "frontendCode/Landing/AboutUs"; // Maps to resources/templates/frontendCode/Landing/AboutUs.html
    }

    /**
     * Displays the services page.
     *
     * @return The path to the services page template.
     */
    @GetMapping("/services")
    public String servicesPage() {
        return "frontendCode/Landing/Services"; // Maps to resources/templates/frontendCode/Landing/Services.html
    }

    /**
     * Displays the login choice page.
     *
     * @return The path to the login choice page template.
     */
    @GetMapping("/LoginChoice")
    public String LoginChoicePage() {
        return "frontendCode/Landing/loginChoice"; // Maps to resources/templates/frontendCode/Landing/loginChoice.html
    }
}