package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing system administrator-specific operations.
 */
@Service
public class SysAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReviewRepository ReviewsRepository;

    @Autowired
    private UserService userService; // Delegates user-related operations

    /**
     * Retrieves all users in the system.
     *
     * @return List of all User objects.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user.
     * @return The User object if found.
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    /**
     * Retrieves all travel packages in the system.
     *
     * @return List of all Package objects.
     */
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    /**
     * Deletes a travel package by its ID.
     *
     * @param packageId The ID of the package to delete.
     */
    @Transactional
    public void deletePackage(Long packageId) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));
        packageRepository.delete(pkg);
    }

    /**
     * Retrieves all bookings in the system.
     *
     * @return List of all Booking objects.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param bookingId The ID of the booking to delete.
     */
    @Transactional
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        bookingRepository.delete(booking);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param commentId The ID of the comment to delete.
     */
    @Transactional
    public void deleteComment(Long commentId) {
        Reviews Review = ReviewsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found."));
        ReviewsRepository.delete(Review);
    }

    /**
     * Retrieves all reports submitted in the system.
     *
     * @return List of all Report objects.
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Retrieves a specific report by its ID.
     *
     * @param reportId The ID of the report.
     * @return The Report object if found.
     */
    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));
    }

    /**
     * Updates the status of a report and applies punishment if necessary.
     *
     * @param reportId       The ID of the report.
     * @param status         The new status of the report (e.g., ACCEPTED, REJECTED).
     * @param punishmentType The type of punishment to apply if the report is accepted.
     */
    @Transactional
    public void updateReportStatus(Long reportId, ReportStatus status, PunishmentType punishmentType) {
        Report report = getReportById(reportId);

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new IllegalStateException("Report has already been reviewed.");
        }

        report.setStatus(status);
        report.setPunishmentType(punishmentType);

        if (status == ReportStatus.ACCEPTED && punishmentType != PunishmentType.NONE) {
            applyPunishment(report);
        }

        reportRepository.save(report);
    }

    /**
     * Deletes a report by its ID.
     *
     * @param reportId The ID of the report to delete.
     */
    @Transactional
    public void deleteReport(Long reportId) {
        Report report = getReportById(reportId);
        reportRepository.delete(report);
    }

    /**
     * Applies the appropriate punishment based on the report details.
     *
     * @param report The report containing punishment information.
     */
    private void applyPunishment(Report report) {
        PunishmentType punishment = report.getPunishmentType();
        User targetUser;

        if (report.getPkg() != null) {
            targetUser = report.getPkg().getProviderDetails();
        } else if (report.getReview() != null) {
            targetUser = report.getReview().getAuthor();
        } else {
            throw new IllegalArgumentException("Report must target a package or a comment.");
        }

        switch (punishment) {
            case WARNING:
                userService.sendWarning(targetUser, report);
                break;
            case SUSPENSION:
                userService.deactivateUser(targetUser.getId());
                break;
            case BAN:
                userService.banUser(targetUser.getId());
                break;
            default:
                throw new IllegalArgumentException("Invalid punishment type.");
        }
    }

    /**
     * Unbans a previously banned user.
     *
     * @param userId The ID of the user to unban.
     */
    @Transactional
    public void unbanUser(Long userId) {
        userService.unbanUser(userId);
    }

    /**
     * Unsuspends a previously suspended user.
     *
     * @param userId The ID of the user to unsuspend.
     */
    @Transactional
    public void unsuspendUser(Long userId) {
        userService.activateUser(userId);
    }

    /**
     * Retrieves application statistics such as total users, packages, bookings, and reports.
     *
     * @return List of statistics for the application.
     */
    public List<Statistic> getAppStatistics() {
        long totalUsers = userRepository.count();
        long totalPackages = packageRepository.count();
        long totalBookings = bookingRepository.count();
        long totalReports = reportRepository.count();

        Statistic stats = new Statistic(totalUsers, totalPackages, totalBookings, totalReports);
        return List.of(stats);
    }

    /**
     * Deletes a review by its ID.
     *
     * @param reviewId The ID of the review to delete.
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        Reviews review = ReviewsRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found."));
        ReviewsRepository.delete(review);
    }

    /**
     * Updates the account status of a user.
     *
     * @param userId    The ID of the user.
     * @param newStatus The new account status to set.
     */
    public void updateUserStatus(Long userId, AccountStatus newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.save(user);
    }
}
