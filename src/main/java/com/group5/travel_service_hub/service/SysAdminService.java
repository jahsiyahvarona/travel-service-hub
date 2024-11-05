package com.group5.travel_service_hub.service;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.*;
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
     * Retrieves all users.
     *
     * @return List of all User objects.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId The ID of the user.
     * @return The User object.
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId The ID of the user to delete.
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    /**
     * Retrieves all travel packages.
     *
     * @return List of all Package objects.
     */
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    /**
     * Deletes a travel package by ID.
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
     * Retrieves all bookings.
     *
     * @return List of all Booking objects.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Deletes a booking by ID.
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
     * Deletes a comment by ID.
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
     * Retrieves all reports.
     *
     * @return List of all Report objects.
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Retrieves a report by ID.
     *
     * @param reportId The ID of the report.
     * @return The Report object.
     */
    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));
    }

    /**
     * Updates the status of a report and applies punishment if accepted.
     *
     * @param reportId        The ID of the report to update.
     * @param status          The new status of the report.
     * @param punishmentType  The punishment type to apply if accepted.
     * @return The updated Report object.
     */
    @Transactional
    public Report updateReportStatus(Long reportId, ReportStatus status, PunishmentType punishmentType) {
        Report report = getReportById(reportId);

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new IllegalStateException("Report has already been reviewed.");
        }

        report.setStatus(status);
        report.setPunishmentType(punishmentType);

        if (status == ReportStatus.ACCEPTED && punishmentType != PunishmentType.NONE) {
            applyPunishment(report);
        }

        return reportRepository.save(report);
    }

    /**
     * Deletes a report by ID.
     *
     * @param reportId The ID of the report to delete.
     */
    @Transactional
    public void deleteReport(Long reportId) {
        Report report = getReportById(reportId);
        reportRepository.delete(report);
    }

    /**
     * Applies punishment based on the report details by delegating to UserService.
     *
     * @param report The report containing punishment details.
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
     * Unbans a user.
     *
     * @param userId The ID of the user to unban.
     */
    @Transactional
    public void unbanUser(Long userId) {
        userService.unbanUser(userId);
    }

    /**
     * Unsuspends a user.
     *
     * @param userId The ID of the user to unsuspend.
     */
    @Transactional
    public void unsuspendUser(Long userId) {
        userService.activateUser(userId);
    }
}
