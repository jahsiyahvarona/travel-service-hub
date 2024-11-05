package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.service.LikeDislikeService;
import com.group5.travel_service_hub.service.PackageService;
import com.group5.travel_service_hub.service.ProviderService;
import com.group5.travel_service_hub.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.*;

/**
 * Controller for managing Provider-related operations.
 */
@Controller
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeDislikeService likeDislikeService;


    /**
     * Handles the creation of a new package.----------------no
     *
     * @param principal          The authenticated provider.
     * @param pkg                The Package entity.
     * @param packageImage       The uploaded image file.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @return Redirects to the list of packages.
     */
    @PostMapping("/packages")
    public String createPackage(
            Principal principal,
            @ModelAttribute Package pkg,
            @RequestParam("packageImage") MultipartFile packageImage,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());

        // Check if the user has the PROVIDER role
        if (user.getRole() != Role.PROVIDER) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can create packages.");
            return "redirect:/providers/packages";
        }

        try {
            // Handle image upload
            if (!packageImage.isEmpty()) {
                String imageUrl = providerService.saveImage(packageImage);
                pkg.setImageUrl(imageUrl);
            }

            providerService.createPackage(user.getId(), pkg);
            redirectAttributes.addFlashAttribute("success", "Package created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create package: " + e.getMessage());
        }
        return "redirect:/providers/packages";
    }

    /**
     * Provides package data as JSON for editing.
     *
     * @param principal The authenticated provider.
     * @param packageId The ID of the package.
     * @return The Package object.
     */
    @GetMapping("/packages/{packageId}/data")
    public @ResponseBody Package getPackageData(
            Principal principal,
            @PathVariable Long packageId) {
        User user = userService.findByUsername(principal.getName());

        // Check if the user has the PROVIDER role
        if (user.getRole() != Role.PROVIDER) {
            throw new SecurityException("Access denied: Only providers can access package data.");
        }

        Package pkg = providerService.getPackageById(packageId);

        // Check if the package belongs to the provider
        if (!pkg.getProviderDetails().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to access this package.");
        }

        return pkg;
    }

    /**
     * Handles the updating of an existing package.
     *
     * @param principal          The authenticated provider.
     * @param packageId          The ID of the package to update.
     * @param pkg                The Package entity containing updated details.
     * @param packageImage       The uploaded image file.
     * @param redirectAttributes Redirect attributes for passing messages.
     * @return Redirects to the list of packages.
     */
    @PostMapping("/packages/{packageId}/update")
    public String updatePackage(
            Principal principal,
            @PathVariable Long packageId,
            @ModelAttribute Package pkg,
            @RequestParam("packageImage") MultipartFile packageImage,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());

        // Check if the user has the PROVIDER role
        if (user.getRole() != Role.PROVIDER) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can update packages.");
            return "redirect:/providers/packages";
        }

        try {
            // Handle image upload
            if (!packageImage.isEmpty()) {
                String imageUrl = providerService.saveImage(packageImage);
                pkg.setImageUrl(imageUrl);
            }

            providerService.updatePackage(user.getId(), packageId, pkg);
            redirectAttributes.addFlashAttribute("success", "Package updated successfully.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized to update this package.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Package not found.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed))))) to update package: " + e.getMessage());
        }
        return "redirect:/providers/packages";
    }






    // Mapping for Profile Page
    @GetMapping("/profile")
    public String profile(Model model) {
        return "frontendCode/ProviderUI/Profile"; // Ensure you have a profile.html file
    }

    // Mapping for Reply to Reviews Page
    @GetMapping("/replyToReviews")
    public String replyToReviews(Model model) {
        return "frontendCode/ProviderUI/RelyToReviews"; // Ensure you have a replyToReviews.html file
    }
    // Mapping for Manage Bookings Page
    @GetMapping("/manageBookings")
    public String manageBookings(Model model) {
        return "frontendCode/ProviderUI/manageBookings"; // Ensure you have a manageBookings.html file
    }
    @GetMapping("/managePackages")
    public String getPackagesByProvider(Model model, HttpSession session) {

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if user is not found in the session
        }

        String username = loggedInUser.getUsername();
        User user = userService.findByUsername(username);

        // Get packages for the provider and sort by name or ID
        List<Package> packages = packageService.getAllPackagesByProvider(user.getId());
        packages.sort(Comparator.comparing(Package::getName));

        // Create a map to store package, like count, and dislike count
        Map<Package, Map<String, Integer>> packageLikeDislikeMap = new LinkedHashMap<>();
        for (Package pkg : packages) {
            int likeCount = Math.toIntExact(likeDislikeService.countLikes(pkg.getId()));
            int dislikeCount = Math.toIntExact(likeDislikeService.countDislikes(pkg.getId()));
            Map<String, Integer> counts = new HashMap<>();
            counts.put("likes", likeCount);
            counts.put("dislikes", dislikeCount);
            packageLikeDislikeMap.put(pkg, counts);
        }

        model.addAttribute("packageLikeDislikeMap", packageLikeDislikeMap);

        return "frontendCode/ProviderUI/managePackages"; // Name of your Thymeleaf template
    }
    }