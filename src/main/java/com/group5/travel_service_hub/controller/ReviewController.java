package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Reviews;
import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.ReviewsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for managing reviews.
 */
@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewsService reviewsService;

    /**
     * Adds a new review to a package.
     *
     * @param content             The content of the review.
     * @param packageId           The ID of the package.
     * @param session             The authenticated user session.
     * @param redirectAttributes  Redirect attributes for flash messages.
     * @return Redirect to the reviews management page.
     */
    @PostMapping("/create")
    public String addReview(@RequestParam("content") String content,
                            @RequestParam("packageId") Long packageId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to add a review.");
            return "redirect:/login"; // Redirect to login page if user is not logged in
        }

        // Verify the user role if needed (e.g., only certain roles can add reviews)
        if (loggedInUser.getRole() != Role.CUSTOMER && loggedInUser.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only users and admins can add reviews.");
            return "redirect:/user/dashboard";
        }

        try {
            // Create the review
            Reviews createdReview = reviewsService.addReview(loggedInUser.getId(), packageId, content);
            redirectAttributes.addFlashAttribute("success", "Review added successfully.");
            return "redirect:/user/manageReviews"; // Adjust as needed

        } catch (Exception e) {
            // Handle any errors that occur while creating the review
            redirectAttributes.addFlashAttribute("error", "Error adding review: " + e.getMessage());
            return "redirect:/user/manageReviews"; // Adjust as needed
        }
    }

    /**
     * Retrieves all reviews for a package.
     *
     * @param packageId The ID of the package.
     * @return List of Review entities.
     */
    @GetMapping("/package/{packageId}")
    @ResponseBody
    public ResponseEntity<List<Reviews>> getReviewsByPackage(@PathVariable Long packageId) {
        List<Reviews> reviews = reviewsService.getReviewsByPackage(packageId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }



    /**
     * Deletes a review by ID.
     *
     * @param reviewId The ID of the review to delete.
     * @return ResponseEntity with a success message.
     */
    @GetMapping("/{reviewId}")
    @ResponseBody
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewsService.deleteReview(reviewId);
        return new ResponseEntity<>("Review deleted successfully.", HttpStatus.OK);
    }
}
