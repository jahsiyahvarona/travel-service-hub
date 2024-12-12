package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Report;
import com.group5.travel_service_hub.repository.ReportRepository;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.Reviews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private ReviewsService reviewService;

    /**
     * Creates a new report for a package or review.
     *
     * @param user      The user creating the report.
     * @param report    The Report entity with details.
     * @param packageId The ID of the package being reported (optional).
     * @param reviewId  The ID of the review being reported (optional).
     */
    @Transactional
    public void createReport(User user, Report report, Long packageId, Long reviewId) {
        // Set the reporter for the report
        report.setReporter(user);

        // Associate the report with a package or review
        if (packageId != null) {
            // Retrieve the package by ID and handle the case where it may not be found
            Package pkg = packageService.getPackageById(packageId)
                    .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + packageId));
            report.setPkg(pkg);
        } else if (reviewId != null) {
            // Retrieve the review by ID and set it in the report
            Reviews review = reviewService.getReviewById(reviewId);
            report.setReview(review);
        } else {
            // Throw an exception if neither a package nor a review ID is provided
            throw new IllegalArgumentException("Report must target a package or a review.");
        }

        // Save the report and return the saved entity
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

