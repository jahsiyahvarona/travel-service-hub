package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.NotificationReason;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.LikeDislikeService;
import com.group5.travel_service_hub.service.NotificationService;
import com.group5.travel_service_hub.service.PackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * MVC Controller for managing likes and dislikes on packages.
 */
@Controller
@RequestMapping("/like-dislikes")
public class LikeDislikeController {

    @Autowired
    private LikeDislikeService likeDislikeService;
    @Autowired
    private PackageService packageService;
@Autowired
    NotificationService notificationService;
    /**
     * Handles the "Like" button click.
     * Toggles between like/unlike if the user already liked or disliked the package.
     */
    @PostMapping("/like")
    public String likePackage(
            @RequestParam Long packageId,
            HttpSession session,
            Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        try {
            // Call a method that handles toggling the like status
            likeDislikeService.handleLike(loggedInUser.getId(), packageId);
        } catch (Exception e) {
            // If there's an error (e.g., user already reacted or no such package), show error message
            model.addAttribute("errorMessage", e.getMessage());
        }

        String message = "your package: " + packageService.getPackageById(packageId).orElseThrow().getName() + ", was liked by: " + loggedInUser.getUsername();
        String targetUrl = "/provider/managePackages"; // Booking details page
        //create notification
        notificationService.createNotification(loggedInUser,packageService.getPackageById(packageId).orElseThrow().getProviderDetails(), NotificationReason.LIKED,message,targetUrl);


        // Redirect back to package details page
        return "redirect:/customer/packageDetails/" + packageId;
    }

    /**
     * Handles the "Dislike" button click.
     * Toggles between dislike/undislike if the user already disliked or liked the package.
     */
    @PostMapping("/dislike")
    public String dislikePackage(
            @RequestParam Long packageId,
            HttpSession session,
            Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        try {
            // Call a method that handles toggling the dislike status
            likeDislikeService.handleDislike(loggedInUser.getId(), packageId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }


        // Redirect back to package details page
        return "redirect:/customer/packageDetails/" + packageId;
    }

    /**
     * Removes a like or dislike by ID.
     */
    @PostMapping("/remove")
    public String removeLikeDislike(
            @RequestParam Long likeDislikeId,
            @RequestParam Long packageId,
            HttpSession session,
            Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/CustomerLogin";
        }

        try {
            likeDislikeService.removeLikeDislike(likeDislikeId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/customer/packageDetails/" + packageId;
    }

    /**
     * Optionally: Endpoint to display like/dislike counts, if needed.
     */
    @GetMapping("/counts")
    public String getLikeDislikeCounts(@RequestParam Long packageId, Model model) {
        Long likeCount = likeDislikeService.countLikes(packageId);
        Long dislikeCount = likeDislikeService.countDislikes(packageId);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("dislikeCount", dislikeCount);
        return "someFragmentOrView";
    }
}