package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.Report;
import com.group5.travel_service_hub.entity.Reviews;
import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.PackageService;
import com.group5.travel_service_hub.service.ReportService;
import com.group5.travel_service_hub.service.ReviewsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private ReviewsService ReviewsService;
    @Autowired
    private PackageService PackageService;

    /**
     * Creates a new report.
     * Endpoint: POST /reports/create
     *
     * @param reason             The reason for the report.
     * @param packageId          The ID of the package being reported (optional).
     * @param reviewId           The ID of the review being reported (optional).
     * @param session            The authenticated user session.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirect to the reports management page.
     */
    @PostMapping("/create")
    public String createReport(@RequestParam("reason") String reason,
                               @RequestParam(required = false) Long packageId,
                               @RequestParam(required = false) Long reviewId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to create a report.");
            return "redirect:/login"; // Redirect to login page if user is not logged in
        }



        // Create a new Report instance
        Report report = new Report();
        report.setReason(reason);
        report.setReporter(loggedInUser);

        try {
            // Associate report with a package or review
            if (packageId != null) {
                Package pkg = PackageService.getPackageById(packageId)
                        .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + packageId));
                report.setPkg(pkg);
            } else if (reviewId != null) {
                Reviews review = ReviewsService.getReviewById(reviewId);
                report.setReview(review);
            } else {
                throw new IllegalArgumentException("Report must target a package or a review.");
            }

            // Save the report
            reportService.createReport(loggedInUser, report, packageId, reviewId);
            redirectAttributes.addFlashAttribute("success", "Report created successfully.");
            return "redirect:/user/manageReports"; // Adjust redirection as needed

        } catch (Exception e) {
            // Handle errors and add an error message
            redirectAttributes.addFlashAttribute("error", "Error creating report: " + e.getMessage());
            return "redirect:/user/manageReports"; // Adjust as needed
        }
    }


    /**
     * Retrieves all reports.
     *
     * @return List of all reports as a JSON response.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Retrieves a report by ID.
     *
     * @param reportId The ID of the report.
     * @return The report as a JSON response if found.
     */
    @GetMapping("/{reportId}")
    @ResponseBody
    public ResponseEntity<Report> getReportById(@PathVariable Long reportId) {
        Optional<Report> report = reportService.getReportById(reportId);
        return report.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
