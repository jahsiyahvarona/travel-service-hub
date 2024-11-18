package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing reports.
 */
@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private BookingService bookingService;

    /**
     * Creates a new report for a package, review, or booking.
     *
     * @param user      The user creating the report.
     * @param reason    The reason for the report.
     * @param packageId The ID of the package being reported (optional).
     * @param reviewId  The ID of the review being reported (optional).
     * @param bookingId The ID of the booking being reported (optional).
     */
    @Transactional
    public void createReport(User user, String reason, Long packageId, Long reviewId, Long bookingId) {
        // Validate that at least one entity is being reported
        if (packageId == null && reviewId == null && bookingId == null) {
            throw new IllegalArgumentException("At least one of packageId, reviewId, or bookingId must be provided.");
        }

        // Initialize the Report entity
        Report report = new Report();
        report.setReporter(user);
        report.setReason(reason);
        report.setStatus(ReportStatus.PENDING);
        report.setTimestamp(LocalDateTime.now());

        // Associate the report with the appropriate entity
        if (packageId != null) {
            Package pkg = packageService.getPackageById(packageId)
                    .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + packageId));
            report.setPkg(pkg);
        }

        if (reviewId != null) {
            Reviews review = reviewsService.getReviewById(reviewId);
            report.setReview(review);
            // Update the review's status to FLAGGED
            reviewsService.reportReview(reviewId);
        }

        if (bookingId != null) {
            Booking booking = bookingService.getBookingById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
            report.setBooking(booking);
            // Update the booking's status to REPORTED
            bookingService.updateBookingStatus(bookingId, BookingStatus.REPORTED);
        }

        // Save the report to the database
        reportRepository.save(report);
    }

    /**
     * Retrieves all reports.
     *
     * @return List of all reports.
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Retrieves a report by its ID.
     *
     * @param reportId The ID of the report.
     * @return The report if found.
     */
    public Optional<Report> getReportById(Long reportId) {
        return reportRepository.findById(reportId);
    }
}
