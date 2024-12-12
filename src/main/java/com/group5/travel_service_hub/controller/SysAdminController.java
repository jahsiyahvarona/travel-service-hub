package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.repository.UserRepository;
import com.group5.travel_service_hub.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.AccountStatus;

import java.util.List;

@RestController
@RequestMapping("/api/sysadmin")
public class SysAdminController {

    @Autowired
    private SysAdminService sysAdminService;

    @Autowired
    public SysAdminController(SysAdminService sysAdminService) {
        this.sysAdminService = sysAdminService;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return UserRepository.save(user);
    }

    // Retrieve all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return sysAdminService.getAllUsers();
    }

    // Retrieve a user by ID
    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return sysAdminService.getUserById(userId);
    }

    // Update user status
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam AccountStatus newStatus) {
        sysAdminService.updateUserStatus(userId, newStatus);
        return ResponseEntity.ok("User status updated successfully.");
    }

    // Unban a user by ID
    @PutMapping("/users/{userId}/unban")
    public ResponseEntity<String> unbanUser(@PathVariable Long userId) {
        sysAdminService.unbanUser(userId);
        return ResponseEntity.ok("User unbanned successfully.");
    }

    // Unsuspend a user by ID
    @PutMapping("/users/{userId}/unsuspend")
    public ResponseEntity<String> unsuspendUser(@PathVariable Long userId) {
        sysAdminService.unsuspendUser(userId);
        return ResponseEntity.ok("User unsuspended successfully.");
    }

    // Retrieve all packages
    @GetMapping("/packages")
    public List<Package> getAllPackages() {
        return sysAdminService.getAllPackages();
    }

    // Delete a package by ID
    @DeleteMapping("/packages/{packageId}")
    public ResponseEntity<String> deletePackage(@PathVariable Long packageId) {
        sysAdminService.deletePackage(packageId);
        return ResponseEntity.ok("Package deleted successfully.");
    }

    // Retrieve all bookings
    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return sysAdminService.getAllBookings();
    }

    // Delete a booking by ID
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
        sysAdminService.deleteBooking(bookingId);
        return ResponseEntity.ok("Booking deleted successfully.");
    }

    // Retrieve all reviews
    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return sysAdminService.getAllReviews();
    }

    // Delete a review by ID
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        sysAdminService.deleteReview(reviewId);
        return ResponseEntity.ok("Review deleted successfully.");
    }

    // Delete a comment by ID
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        sysAdminService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully.");
    }

    // Retrieve all reports
    @GetMapping("/reports")
    public List<Report> getAllReports() {
        return sysAdminService.getAllReports();
    }

    // Retrieve a report by ID
    @GetMapping("/reports/{reportId}")
    public Report getReportById(@PathVariable Long reportId) {
        return sysAdminService.getReportById(reportId);
    }

    // Update a report's status and apply punishment if applicable
    @PutMapping("/reports/{reportId}/status")
    public ResponseEntity<String> updateReportStatus(
            @PathVariable Long reportId,
            @RequestParam ReportStatus status,
            @RequestParam PunishmentType punishmentType) {
        sysAdminService.updateReportStatus(reportId, status, punishmentType);
        return ResponseEntity.ok("Report status updated successfully.");
    }

    // Delete a report by ID
    @DeleteMapping("/reports/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        sysAdminService.deleteReport(reportId);
        return ResponseEntity.ok("Report deleted successfully.");
    }

    @GetMapping("/statistics")
    public List getAppStatistics() {
        return sysAdminService.getAppStatistics();
    }
}
