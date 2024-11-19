package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Booking;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.BookingService;
import com.group5.travel_service_hub.service.PackageService;
import com.group5.travel_service_hub.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final UserService userService;
    private final BookingService bookingService;
    private final PackageService packageService;

    @Autowired
    public CustomerController(UserService userService, BookingService bookingService, PackageService packageService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.packageService = packageService;
    }

    /**
     * Displays the Customer Dashboard.
     */
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User customer = userService.findByUsername(loggedInUser.getUsername());
        model.addAttribute("customer", customer);
        model.addAttribute("totalSpending", 4300);  // Example value
        model.addAttribute("totalBookings", 10);    // Example value
        model.addAttribute("favoritePackage", "Jamaica"); // Example value

        return "frontendCode/CustomerUI/customerDashboard";
    }

    /**
     * Displays the customer's bookings.
     */
    @GetMapping("/bookings")
    public String viewBookings(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Booking> bookings = bookingService.getBookingsByCustomerId(loggedInUser.getId());
        model.addAttribute("bookings", bookings);

        return "frontendCode/CustomerUI/customerViewBookings";
    }

    /**
     * Displays available packages.
     */
    @GetMapping("/packages")
    public String viewPackages(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<Package> packages = packageService.getAllPackages();
        model.addAttribute("packages", packages);

        return "frontendCode/CustomerUI/customerViewPackages";
    }

    /**
     * Handles booking a package.
     */
    @PostMapping("/bookPackage")
    public String bookPackage(HttpSession session,
                              @RequestParam Long packageId,
                              Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Optional<Package> selectedPackage = packageService.getPackageById(packageId);
        if (selectedPackage.isEmpty()) {
            model.addAttribute("errorMessage", "Selected package not found.");
            return "redirect:/customer/packages";
        }

        bookingService.createBooking(loggedInUser.getId(), packageId, LocalDate.now(), LocalDate.now().plusDays(7));

        return "redirect:/customer/bookings";
    }
    @GetMapping("/packages/search")
    public String searchPackages(@RequestParam("query") String query, Model model) {
        List<Package> packages = packageService.searchPackagesByName(query);
        model.addAttribute("packages", packages);
        model.addAttribute("packagesEmpty", packages.isEmpty());
        return "frontendCode/CustomerUI/viewPackages"; // Ensure this path matches your view template
    }
    /**
     * Displays the Leave a Review page.
     */
    @GetMapping("/reviews")
    public String leaveReview(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Fetch the booked packages for the logged-in customer
        List<Booking> bookedPackages = bookingService.getBookingsByCustomerId(loggedInUser.getId());

        if (bookedPackages.isEmpty()) {
            model.addAttribute("errorMessage", "You currently have no booked packages to review.");
        }

        model.addAttribute("bookedPackages", bookedPackages);
        return "frontendCode/CustomerUI/customerLeaveReview";
    }

    /**
     * Submits a review for a booking.
     */
    @PostMapping("/reviews/submit")
    public String submitReview(@RequestParam Long bookingId,
                               @RequestParam String reviewContent,
                               HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Placeholder for submitting review logic
        return "redirect:/customer/reviews?success";
    }

    /**
     * Displays the customer's profile.
     */
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);
        return "frontendCode/CustomerUI/customerProfile";
    }

    /**
     * Updates the customer's profile.
     */
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam Long userId,
                                @ModelAttribute User userDetails,
                                HttpSession session) {
        User updatedUser = userService.updateProfile(userId, userDetails);
        session.setAttribute("loggedInUser", updatedUser);
        return "redirect:/customer/profile";
    }

    /**
     * Updates the customer's profile picture.
     */
    @PostMapping("/profile/updatePic")
    public String updateProfilePic(@RequestParam Long userId,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session) {
        User updatedUser = userService.updateProfilePic(userId, file);
        session.setAttribute("loggedInUser", updatedUser);
        return "redirect:/customer/profile";
    }

    /**
     * Handles logout and invalidates the session.
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    /**
     * Registers a new customer.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerCustomer(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}
