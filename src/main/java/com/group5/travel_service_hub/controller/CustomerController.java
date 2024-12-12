package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.repository.BookingRepository;
import com.group5.travel_service_hub.repository.CityRepository;
import com.group5.travel_service_hub.repository.LikeDislikeRepository;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.group5.travel_service_hub.service.LikeDislikeService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BookingRepository bookingRepository; // Repository for booking-related data

    @Autowired
    private LikeDislikeService likeDislikeService;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private PackageRepository packageRepository; // Repository for package-related data

    @Autowired
    private NotificationService notificationService;

    @Autowired private LikeDislikeRepository likeDislikeRepository;

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
     * Displays the customer's bookings.
     */
    @GetMapping("/bookings")
    public String viewBookings(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        List<Booking> bookings = bookingService.getBookingsByCustomerId(loggedInUser.getId());
        model.addAttribute("bookings", bookings);

        // Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationsForUser(loggedInUser);

        // Count unread notifications
        long unreadNotificationsCount = notifications.stream().filter(n -> !n.isRead()).count();

        // Filter to include only unread notifications
        List<Notification> unseenNotifications = notifications.stream()
                .filter(n -> !n.isRead()) // Keep only notifications where isRead is false
                .toList();

        // Add to model
        model.addAttribute("notifications", unseenNotifications);
        model.addAttribute("unreadNotificationsCount", unreadNotificationsCount);


        return "frontendCode/CustomerUI/customerViewBookings";
    }

    /**
     * Displays available packages.
     */
    @GetMapping("/packages")
    public String viewPackages(HttpSession session, Model model) {
        // Ensure the user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin"; // Redirect to login if the user is not authenticated
        }

        // Fetch all packages
        List<Package> packages = packageService.getAllPackages();
        model.addAttribute("packages", packages);
        model.addAttribute("packagesEmpty", packages.isEmpty()); // Check if no packages are available

        // Fetch all cities for dropdown
        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getName)); // Sort cities alphabetically by name
        model.addAttribute("cities", cities);

// Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationsForUser(loggedInUser);

        // Count unread notifications
        long unreadNotificationsCount = notifications.stream().filter(n -> !n.isRead()).count();

        // Filter to include only unread notifications
        List<Notification> unseenNotifications = notifications.stream()
                .filter(n -> !n.isRead()) // Keep only notifications where isRead is false
                .toList();

        // Add to model
        model.addAttribute("notifications", unseenNotifications);
        model.addAttribute("unreadNotificationsCount", unreadNotificationsCount);

        return "frontendCode/CustomerUI/customerViewPackages";
    }



    private long calculateTotalBookings(List<Booking> bookings) {
        return bookings.size();
    }

    private Package determineFavoritePackage(List<Booking> bookings) {
        return bookings.stream()
                .collect(Collectors.groupingBy(Booking::getPkg, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
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
            return "redirect:/CustomerLogin";
        }

        Optional<Package> selectedPackage = packageService.getPackageById(packageId);
        if (selectedPackage.isEmpty()) {
            model.addAttribute("errorMessage", "Selected package not found.");
            return "redirect:/customer/packages";
        }

        bookingService.createBooking(loggedInUser.getId(), packageId, LocalDate.now(), LocalDate.now().plusDays(7));

        String message = "You have a new booking request from " + loggedInUser.getUsername();
        String targetUrl = "/provider/manageBookings"; // Booking details page
        //create notification
        notificationService.createNotification(loggedInUser,selectedPackage.get().getProviderDetails(), NotificationReason.NEW_BOOKING,message,targetUrl);
        return "redirect:/customer/bookings";
    }

    @GetMapping("/packages/search")
    public String searchPackages(
            HttpSession session,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "cityId", required = false) String cityIdStr,
            Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        List<Package> packages;

        // Parse cityIdStr to Long if it's not null or empty
        Long cityId = null;
        if (cityIdStr != null && !cityIdStr.isEmpty()) {
            try {
                cityId = Long.parseLong(cityIdStr);
            } catch (NumberFormatException e) {
                // Handle invalid cityId parameter
                model.addAttribute("error", "Invalid city selection.");
                // Optionally, return to the packages view with an error message
                return "frontendCode/CustomerUI/customerViewPackages";
            }
        }

        // Determine which search method to use based on parameters
        if ((query != null && !query.isEmpty()) && cityId != null) {
            packages = packageService.findByNameContainingIgnoreCaseAndLocationId(query, cityId);
        } else if (query != null && !query.isEmpty()) {
            packages = packageService.findByNameContainingIgnoreCase(query);
        } else if (cityId != null) {
            packages = packageService.findByLocationId(cityId);
        } else {
            packages = packageService.getAllPackages();
        }

        model.addAttribute("packages", packages);
        model.addAttribute("packagesEmpty", packages.isEmpty());

        // Add cities for the dropdown
        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getName)); // Sort alphabetically
        model.addAttribute("cities", cities);

        return "frontendCode/CustomerUI/customerViewPackages"; // Ensure correct template path
    }




    /**
     * Submits a review for a booking.
     */
    @PostMapping("/reviews/submit")
    public String submitReview(@RequestParam Long packageId,
                               @RequestParam String reviewContent,
                               HttpSession session,
                               Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        Optional<Package> optionalPackage = packageService.getPackageById(packageId);
        if (optionalPackage.isEmpty()) {
            model.addAttribute("errorMessage", "Selected package not found.");
            return "frontendCode/CustomerUI/customerLeaveReview";
        }

        reviewsService.addReview(loggedInUser.getId(), packageId, reviewContent);

        String message = loggedInUser.getUsername() + " has left a review on your package: " + optionalPackage.get().getName();
        String targetUrl = "/provider/replyToReviews";
        notificationService.createNotification(loggedInUser, optionalPackage.get().getProviderDetails(), NotificationReason.NEW_REVIEW, message, targetUrl);

        return "redirect:/customer/reviews?success";
    }
    @GetMapping("/reviews/{packageId}")
    public String viewPackageReviews(@PathVariable Long packageId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        Optional<Package> optionalPackage = packageService.getPackageById(packageId);
        if (optionalPackage.isEmpty()) {
            model.addAttribute("errorMessage", "Selected package not found.");
            return "frontendCode/CustomerUI/customerViewPackages";
        }

        List<Reviews> reviews = reviewsService.getReviewsByPackage(packageId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("package", optionalPackage.get());

        // Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationsForUser(loggedInUser);
        long unreadNotificationsCount = notifications.stream().filter(n -> !n.isRead()).count();
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadNotificationsCount", unreadNotificationsCount);

        return "frontendCode/CustomerUI/viewpackagereviews";
    }


    /**
     * Displays the customer's profile.
     */
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        // Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationsForUser(loggedInUser);

        // Count unread notifications
        long unreadNotificationsCount = notifications.stream().filter(n -> !n.isRead()).count();

        // Filter to include only unread notifications
        List<Notification> unseenNotifications = notifications.stream()
                .filter(n -> !n.isRead()) // Keep only notifications where isRead is false
                .toList();

        // Add to model
        model.addAttribute("notifications", unseenNotifications);
        model.addAttribute("unreadNotificationsCount", unreadNotificationsCount);


        model.addAttribute("loggedInUser", loggedInUser);
        return "frontendCode/CustomerUI/customerProfile";
    }

    /**
     * Updates the customer's profile.
     */
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam Long userId,
                                @ModelAttribute User userDetails,
                                HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }
        return "redirect:/customer/profile";
    }

    /**
     * Updates the customer's profile picture.
     */
    @PostMapping("/profile/updatePic")
    public String updateProfilePic(@RequestParam Long userId,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        return "redirect:/customer/profile";
    }

    /**
     * Handles logout and invalidates the session.
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/CustomerLogin";

    }

    /**
     * Registers a new customer.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerCustomer(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/packageDetails/{id}")
    public String viewPackageDetails(@PathVariable Long id, HttpSession session, Model model) {
        // Ensure the user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        // Fetch the package details by id
        Optional<Package> optionalPackage = packageService.getPackageById(id);
        if (optionalPackage.isEmpty()) {
            // If package is not found, show an error page
            model.addAttribute("errorMessage", "The requested package was not found.");
            return "frontendCode/CustomerUI/error";
        }

        Package pkg = optionalPackage.get();
        model.addAttribute("package", pkg);

        // Fetch the reviews for this package
        List<Reviews> reviews = reviewsService.getReviewsByPackage(id);
        model.addAttribute("reviews", reviews);

        // Determine like/dislike counts
        long likeCount = pkg.getLikeDislikes().stream().filter(LikeDislike::isLike).count();
        long dislikeCount = pkg.getLikeDislikes().size() - likeCount;
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("dislikeCount", dislikeCount);

        // Check if the user already liked or disliked this package
        Optional<LikeDislike> userReaction = likeDislikeService.findUserReaction(loggedInUser.getId(), id);
        boolean userLiked = false;
        boolean userDisliked = false;
        if (userReaction.isPresent()) {
            userLiked = userReaction.get().isLike();
            userDisliked = !userReaction.get().isLike();
        }

        // Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationsForUser(loggedInUser);

        // Count unread notifications
        long unreadNotificationsCount = notifications.stream().filter(n -> !n.isRead()).count();

        // Filter to include only unread notifications
        List<Notification> unseenNotifications = notifications.stream()
                .filter(n -> !n.isRead()) // Keep only notifications where isRead is false
                .toList();

        // Add to model
        model.addAttribute("notifications", unseenNotifications);
        model.addAttribute("unreadNotificationsCount", unreadNotificationsCount);

        model.addAttribute("userLiked", userLiked);
        model.addAttribute("userDisliked", userDisliked);

        return "frontendCode/CustomerUI/packageDetails";
    }

    /**
     * Displays a form for creating a new review for a specific package.
     *
     * @param packageId The ID of the package being reviewed.
     * @param model     The model to pass attributes to the view.
     * @param session   The HTTP session to retrieve the logged-in user.
     * @return The Thymeleaf template for creating a new review.
     */
    @GetMapping("/packages/{packageId}/reviews/new")
    public String showCreateReviewForm(@PathVariable Long packageId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to submit a review.");
            return "redirect:/CustomerLogin";
        }

        // Optionally, verify if the user has booked the package before allowing them to review
        List<Booking> hasBooked = bookingService.getBookingsByCustomerId(loggedInUser.getId());
        if (hasBooked.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "You can only review packages you've booked.");
            return "redirect:/customer/packages/" + packageId;
        }

        model.addAttribute("packageId", packageId);
        model.addAttribute("review", new Reviews());
        return "frontendCode/CustomerUI/createReview";
    }
    /**
     * Handles the submission of a new review for a package.
     *
     * @param packageId           The ID of the package being reviewed.
     * @param review              The review object containing user input.
     * @param redirectAttributes  Redirect attributes for flash messages.
     * @param session             The HTTP session to retrieve the logged-in user.
     * @return Redirects to the package details page with a success or error message.
     */
    @PostMapping("/packages/{packageId}/reviews")
    public String submitReview(@PathVariable Long packageId,
                               @ModelAttribute("review") Reviews review,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to submit a review.");
            return "redirect:/CustomerLogin";
        }

        try {
            reviewsService.addReview(loggedInUser.getId(), packageId, review.getContent());
            redirectAttributes.addFlashAttribute("successMessage", "Review submitted successfully.");

            // Optional: Redirect to the package details page
            return "redirect:/customer/packages/" + packageId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer/packages/" + packageId + "/reviews/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred while submitting your review.");
            return "redirect:/customer/packages/" + packageId + "/reviews/new";
        }
    }

    /**
     * Displays all reviews for a specific package.
     *
     * @param packageId The ID of the package.
     * @param model     The model to pass attributes to the view.
     * @return The Thymeleaf template displaying all reviews for the package.
     */
    @GetMapping("/packages/{packageId}/reviews")
    public String viewPackageReviews(@PathVariable Long packageId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to view reviews.");
            return "redirect:/CustomerLogin";
        }

        Optional<Package> optionalPackage = packageService.getPackageById(packageId);
        if (optionalPackage.isEmpty()) {
            model.addAttribute("errorMessage", "Selected package not found.");
            return "frontendCode/CustomerUI/customerViewPackages";
        }

        List<Reviews> reviews = reviewsService.getReviewsByPackage(packageId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("package", optionalPackage.get());

        // Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationsForUser(loggedInUser);
        long unreadNotificationsCount = notifications.stream().filter(n -> !n.isRead()).count();
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadNotificationsCount", unreadNotificationsCount);

        return "frontendCode/CustomerUI/viewpackagereviews";
    }

    /**
     * Displays a form for editing an existing review.
     *
     * @param reviewId The ID of the review to edit.
     * @param model    The model to pass attributes to the view.
     * @param session  The HTTP session to retrieve the logged-in user.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return The Thymeleaf template for editing the review.
     */
    @GetMapping("/reviews/{reviewId}/edit")
    public String showEditReviewForm(@PathVariable Long reviewId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to edit your review.");
            return "redirect:/CustomerLogin";
        }

        Reviews existingReview = reviewsService.getReviewById(reviewId);
        if (!existingReview.getAuthor().getId().equals(loggedInUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this review.");
            return "redirect:/customer/reviews";

        }

        model.addAttribute("review", existingReview);
        return "frontendCode/CustomerUI/customerLeaveReview";
    }

    /**
     * Handles the submission of an edited review.
     *
     * @param reviewId           The ID of the review being edited.
     * @param reviewContent      The updated review content from the form.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @param session            The HTTP session to retrieve the logged-in user.
     * @return Redirects to the package details page with a success or error message.
     */
    @PostMapping("/reviews/{reviewId}/edit")
    public String submitEditReview(@PathVariable("reviewId") Long reviewId,
                                   @RequestParam("reviewContent") String reviewContent,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to edit your review.");
            return "redirect:/CustomerLogin";
        }

        try {
            Reviews existingReview = reviewsService.getReviewById(reviewId);
            if (existingReview == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Review not found.");
                return "redirect:/customer/reviews";
            }

            // Ensure that the logged-in user is the author of the review
            if (!existingReview.getAuthor().getId().equals(loggedInUser.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this review.");
                return "redirect:/customer/reviews";
            }

            // Update the review content
            reviewsService.editReview(reviewId, reviewContent);
            redirectAttributes.addFlashAttribute("successMessage", "Review updated successfully.");

            // Redirect to the package details page (ensure a slash before the ID)
            return "redirect:/customer/reviews";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer/reviews";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred while updating your review.");
            return "redirect:/customer/reviews";
        }
    }


    /**
     * Handles the deletion of a customer's review.
     *
     * @param reviewId           The ID of the review to delete.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @param session            The HTTP session to retrieve the logged-in user.
     * @return Redirects to the package details page with a success or error message.
     */
    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to delete your review.");
            return "redirect:/CustomerLogin";
        }

        Reviews existingReview = null;
        try {
            existingReview = reviewsService.getReviewById(reviewId);
            if (!existingReview.getAuthor().getId().equals(loggedInUser.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this review.");
                return "redirect:/customer/packageDetails" + existingReview.getPkg().getId();
            }

            reviewsService.deleteReview(reviewId);
            redirectAttributes.addFlashAttribute("successMessage", "Review deleted successfully.");
            return "redirect:/customer/reviews";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer/reviews" + e.getMessage();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred while deleting your review.");
            assert existingReview != null;
            return  "redirect:/customer/reviews" + existingReview.getPkg().getId();
        }
    }

    /**
     * Displays all reviews authored by the logged-in customer.
     *
     * @param session             The HTTP session to retrieve the logged-in user.
     * @param model               The model to pass attributes to the view.
     * @param redirectAttributes  Redirect attributes for flash messages.
     * @return The Thymeleaf template for managing customer reviews.
     */
    @GetMapping("/reviews")
    public String viewMyReviews(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/ProviderLogin"; // Redirect to login if user is not found in the session
        }

        // Fetch the latest user details to ensure up-to-date information
        User user = userService.findByUsername(loggedInUser.getUsername());
        if (user == null) {
            // Optionally, invalidate the session if the user doesn't exist
            session.invalidate();
            return "redirect:/ProviderLogin"; // Redirect to login if user details are missing
        }

        List<Reviews> myReviews = reviewsService.getReviewsByAuthor(loggedInUser.getId());
        model.addAttribute("myReviews", myReviews);

        // Prepare a new review object for creation
        List<Booking> bookedPackages = bookingService.getBookingsByCustomerId(loggedInUser.getId()).stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.CONFIRMED))
                .toList();

        Set<Long> reviewedPackageIds = myReviews.stream()
                .map(review -> review.getPkg().getId())
                .collect(Collectors.toSet());

        List<Package> packagesToReview = bookedPackages.stream()
                .map(Booking::getPkg)
                .filter(pkg -> !reviewedPackageIds.contains(pkg.getId()))
                .collect(Collectors.toList());

        model.addAttribute("packagesToReview", packagesToReview);
        model.addAttribute("canReview", !packagesToReview.isEmpty());

        // Prepare a new Review object for the create form
        model.addAttribute("newReview", new Reviews());

        // Fetch notifications for the user
        List<Notification> notifications = notificationService.getNotificationsForUser(loggedInUser);

        // Count unread notifications
        long unreadNotificationsCount = notifications.stream().filter(n -> !n.isRead()).count();

        // Filter to include only unread notifications
        List<Notification> unseenNotifications = notifications.stream()
                .filter(n -> !n.isRead()) // Keep only notifications where isRead is false
                .toList();

        // Add to model
        model.addAttribute("notifications", unseenNotifications);
        model.addAttribute("unreadNotificationsCount", unreadNotificationsCount);

        return "frontendCode/CustomerUI/customerLeaveReview";
    }

}
