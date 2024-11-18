package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Reviews;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.ReviewsService;
import com.group5.travel_service_hub.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for managing reviews and provider interactions.
 * Handles actions such as replying to reviews, editing replies, deleting replies, and reporting reviews.
 */
@Controller
@RequestMapping("/provider")
public class ReviewController {

    @Autowired
    private ReviewsService reviewsService; // Service for managing reviews

    @Autowired
    private UserService userService; // Service for managing user-related operations

    /**
     * Handles the submission of a reply to a review.
     *
     * @param reviewId           The ID of the review being replied to.
     * @param replyContent       The content of the reply.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @param session            The HTTP session to retrieve the logged-in user.
     * @return Redirects back to the replyToReviews page with a success or error message.
     */
    @PostMapping("/replyToReview")
    public String replyToReview(@RequestParam("reviewId") Long reviewId,
                                @RequestParam("replyContent") String replyContent,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        try {
            // Retrieve the logged-in user to ensure they are authorized to reply
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                return "redirect:/ProviderLogin"; // Redirect to login if user is not logged in
            }

            // Fetch the review to ensure it belongs to the provider
            Reviews review = reviewsService.getReviewById(reviewId);
            if (!review.getProvider().getId().equals(loggedInUser.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to reply to this review.");
                return "redirect:/provider/replyToReviews";
            }

            // Submit the reply to the review
            reviewsService.replyToReview(reviewId, replyContent);

            redirectAttributes.addFlashAttribute("successMessage", "Reply submitted successfully.");
            return "redirect:/provider/replyToReviews";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/provider/replyToReviews";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
            return "redirect:/provider/replyToReviews";
        }
    }

    /**
     * Handles editing an existing reply to a review.
     *
     * @param reviewId           The ID of the review whose reply is being edited.
     * @param replyContent       The updated content of the reply.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @param session            The HTTP session to retrieve the logged-in user.
     * @return Redirects to the replyToReviews page with a success or error message.
     */
    @PostMapping("/editReply")
    public String editReply(@RequestParam("reviewId") Long reviewId,
                            @RequestParam("replyContent") String replyContent,
                            RedirectAttributes redirectAttributes,
                            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to edit replies.");
            return "redirect:/ProviderLogin";
        }

        try {
            // Edit the reply to the review
            reviewsService.editReply(reviewId, replyContent);

            redirectAttributes.addFlashAttribute("successMessage", "Reply edited successfully.");
            return "redirect:/provider/replyToReviews";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/provider/replyToReviews";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
            return "redirect:/provider/replyToReviews";
        }
    }

    /**
     * Handles deleting an existing reply to a review.
     *
     * @param reviewId           The ID of the review whose reply is being deleted.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @param session            The HTTP session to retrieve the logged-in user.
     * @return Redirects to the replyToReviews page with a success or error message.
     */
    @PostMapping("/deleteReply")
    public String deleteReply(@RequestParam("reviewId") Long reviewId,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to delete replies.");
            return "redirect:/ProviderLogin";
        }

        try {
            // Delete the reply to the review
            reviewsService.deleteReply(reviewId);

            redirectAttributes.addFlashAttribute("successMessage", "Reply deleted successfully.");
            return "redirect:/provider/replyToReviews";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/provider/replyToReviews";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
            return "redirect:/provider/replyToReviews";
        }
    }

    /**
     * Handles reporting a review.
     *
     * @param reviewId           The ID of the review being reported.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @param session            The HTTP session to retrieve the logged-in user.
     * @return Redirects to the replyToReviews page with a success or error message.
     */
    @PostMapping("/reportReview")
    public String reportReview(@RequestParam("reviewId") Long reviewId,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        try {
            // Retrieve the logged-in user
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                return "redirect:/ProviderLogin";
            }

            // Fetch the review to ensure it belongs to the provider
            Reviews review = reviewsService.getReviewById(reviewId);
            if (!review.getProvider().getId().equals(loggedInUser.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to report this review.");
                return "redirect:/provider/replyToReviews";
            }

            // Report the review
            reviewsService.reportReview(reviewId);

            redirectAttributes.addFlashAttribute("successMessage", "Review reported successfully.");
            return "redirect:/provider/replyToReviews";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/provider/replyToReviews";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
            return "redirect:/provider/replyToReviews";
        }
    }
}
