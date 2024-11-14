package com.group5.travel_service_hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    /**
     * Display the welcome page.
     * @return the path to the welcome page template
     */
    @GetMapping("/welcome")
    public String welcomePage() {
        return "frontendCode/Landing/WelcomePage"; // Maps to resources/templates/landing/welcome.html
    }

    /**
     * Display the home page.
     * @return the path to the home page template
     */
    @GetMapping("/signUpChoice")
    public String signUpChoicePage() {
        return "frontendCode/Landing/signUpChoice"; // Maps to resources/templates/landing/home.html
    }

    /**
     * Display the about page.
     * @return the path to the about page template
     */
    @GetMapping("/about")
    public String aboutPage() {
        return "frontendCode/Landing/AboutUs"; // Maps to resources/templates/landing/about.html
    }

    /**
     * Display the contact page.
     * @return the path to the contact page template
     */
    @GetMapping("/services")
    public String servicesPage() {
        return "frontendCode/Landing/Services"; // Maps to resources/templates/landing/contact.html
    }

    /**
     * Display the home page.
     * @return the path to the home page template
     */
    @GetMapping("/LoginChoice")
    public String LoginChoicePage() {
        return "frontendCode/Landing/loginChoice"; // Maps to resources/templates/landing/home.html
    }
}
