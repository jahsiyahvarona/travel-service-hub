package com.group5.travel_service_hub.controller;
import com.group5.travel_service_hub.entity.Package;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing Provider-related operations.
 */
@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private LikeDislikeService likeDislikeService;
    @Autowired
    private ReportService reportService;

    /**
     * Create a new Provider profile.
     *
     * @param provider The Provider entity.
     * @return The created Provider object.
     */
    @PostMapping("")
    public ResponseEntity<User> createProviderProfile(@RequestBody User provider) {
        provider.setRole(Role.PROVIDER);
        User createdProvider = userService.registerUser(provider);
        return ResponseEntity.ok(createdProvider);
    }

    /**
     * Get Provider profile by providerId.
     *
     * @param providerId The ID of the provider.
     * @return The Provider object.
     */
    @GetMapping("/{providerId}")
    public ResponseEntity<User> getProviderProfile(@PathVariable Long providerId) {
        User provider = userService.findById(providerId);
        return ResponseEntity.ok(provider);
    }

    /**
     * Update Provider profile.
     *
     * @param providerId The ID of the provider.
     * @param provider   The updated Provider details.
     * @return The updated Provider object.
     */
    @PutMapping("/{providerId}")
    public ResponseEntity<User> updateProviderProfile(
            @PathVariable Long providerId,
            @RequestBody User provider) {
        User updatedProvider = userService.updateProfile(providerId, provider);
        return ResponseEntity.ok(updatedProvider);
    }

    /**
     * Delete Provider profile.
     *
     * @param providerId The ID of the provider.
     * @return A confirmation message.
     */
    @DeleteMapping("/{providerId}")
    public ResponseEntity<String> deleteProviderProfile(@PathVariable Long providerId) {
        userService.deactivateUser(providerId);
        return ResponseEntity.ok("Provider profile deactivated successfully.");
    }

    /**
     * Create a new Package (Service).
     *
     * @param providerId The ID of the provider.
     * @param pkg        The Package entity.
     * @return The created Package object.
     */
    @PostMapping("/{providerId}/packages")
    public ResponseEntity<Package> createPackage(
            @PathVariable Long providerId,
            @RequestBody Package pkg) {
        User provider = userService.findById(providerId);
        pkg.setProviderDetails(provider);
        packageService.createPackage(pkg);
        return ResponseEntity.ok(pkg);
    }

    /**
     * Update an existing Package.
     *
     * @param providerId The ID of the provider.
     * @param packageId  The ID of the package to update.
     * @param pkg        The updated Package details.
     * @return The updated Package object.
     */
    @PutMapping("/{providerId}/packages/{packageId}")
    public ResponseEntity<Package> updatePackage(
            @PathVariable Long providerId,
            @PathVariable Long packageId,
            @RequestBody Package pkg) {
        Package existingPackage = packageService.getPackageById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));
        if (!existingPackage.getProviderDetails().getId().equals(providerId)) {
            return ResponseEntity.status(403).build();
        }
        pkg.setId(packageId);
        pkg.setProviderDetails(existingPackage.getProviderDetails());
        packageService.updatePackage(pkg);
        return ResponseEntity.ok(pkg);
    }

    /**
     * Delete a Package.
     *
     * @param providerId The ID of the provider.
     * @param packageId  The ID of the package to delete.
     * @return A confirmation message.
     */
    @DeleteMapping("/{providerId}/packages/{packageId}")
    public ResponseEntity<String> deletePackage(
            @PathVariable Long providerId,
            @PathVariable Long packageId) {
        User provider = userService.findById(providerId);
        packageService.deletePackage(provider, packageId);
        return ResponseEntity.ok("Package deleted successfully.");
    }

    /**
     * Get all Packages for a Provider.
     *
     * @param providerId The ID of the provider.
     * @return A list of Package objects.
     */
    @GetMapping("/{providerId}/packages")
    public ResponseEntity<List<Package>> getPackagesByProvider(@PathVariable Long providerId) {
        List<Package> packages = packageService.getAllPackagesByProvider(providerId);
        return ResponseEntity.ok(packages);
    }

    /**
     * View customer statistics (likes, dislikes, bookings) for services
     * by this Provider.
     *
     * @param providerId The ID of the provider.
     * @return A list of statistics for each package.
     */
    @GetMapping("/{providerId}/statistics")
    public ResponseEntity<List<Map<String, Object>>> getCustomerStatistics(
            @PathVariable Long providerId) {

        List<Package> packages = packageService
                .getAllPackagesByProvider(providerId);

        List<Map<String, Object>> statisticsList = new ArrayList<>();

        for (Package pkg : packages) {
            Long likes = likeDislikeService.countLikes(pkg.getId());
            Long dislikes = likeDislikeService.countDislikes(pkg.getId());
            int bookingsCount = bookingService
                    .getBookingsByPackageId(pkg.getId()).size();

            Map<String, Object> stats = new HashMap<>();
            stats.put("packageId", pkg.getId());
            stats.put("packageName", pkg.getName());
            stats.put("likes", likes);
            stats.put("dislikes", dislikes);
            stats.put("bookingsCount", bookingsCount);

            statisticsList.add(stats);
        }

        return ResponseEntity.ok(statisticsList);
    }

    /**
     * Get reviews for this Provider.
     *
     * @param providerId The ID of the provider.
     * @return A list of Review objects.
     */
    @GetMapping("/{providerId}/reviews")
    public ResponseEntity<List<Reviews>> getReviewsByProvider(@PathVariable Long providerId) {
        List<Package> packages = packageService.getAllPackagesByProvider(providerId);
        List<Reviews> reviews = packages.stream()
                .flatMap(pkg -> reviewsService.getReviewsByPackage(pkg.getId()).stream())
                .toList();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get all Bookings for a Provider.
     *
     * @param providerId The ID of the provider.
     * @return A list of Booking objects for the provider's packages.
     */
    @GetMapping("/{providerId}/bookings")
    public ResponseEntity<List<Booking>> getBookingsByProvider(@PathVariable Long providerId) {
        // Retrieve all packages for the provider
        List<Package> providerPackages = packageService.getAllPackagesByProvider(providerId);

        // Collect bookings for each package
        List<Booking> providerBookings = new ArrayList<>();
        for (Package pkg : providerPackages) {
            providerBookings.addAll(bookingService.getBookingsByPackageId(pkg.getId()));
        }

        return ResponseEntity.ok(providerBookings);
    }
    /**
     * Create a new report on a review.
     *
     * @param reporterId The ID of the user reporting the review.
     * @param reviewId   The ID of the review being reported.
     * @param report     The report details (reason).
     * @return The created Report object.
     */
    @PostMapping("/newReport/{reviewId}/users/{reporterId}")
    public ResponseEntity<Report> createReviewReport(
            @PathVariable Long reporterId,
            @PathVariable Long reviewId,
            @RequestBody Report report) {

        User reporter = userService.findById(reporterId);
        report.setStatus(ReportStatus.PENDING); // Set initial status to PENDING
        report.setTimestamp(LocalDateTime.now()); // Set the current timestamp

        reportService.createReport(reporter, report, null, reviewId);

        return ResponseEntity.ok(report);
    }
}
