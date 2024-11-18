package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.service.ReportService;
import com.group5.travel_service_hub.service.ReviewsService;
import com.group5.travel_service_hub.service.PackageService;
import com.group5.travel_service_hub.service.BookingService;
import com.group5.travel_service_hub.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing report-related operations.
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService; // Service for handling report-related operations

    @Autowired
    private ReviewsService reviewsService; // Service for managing reviews

    @Autowired
    private PackageService packageService; // Service for managing packages

    @Autowired
    private BookingService bookingService; // Service for managing bookings

    @Autowired
    private UserService userService; // Service for managing users

    /**
     * Creates a new report.
     * Endpoint: POST /reports/create
     *
     * @param reason             The reason for the report.
     * @param packageId          The ID of the package being reported (optional).
     * @param reviewId           The ID of the review being reported (optional).
     * @param bookingId          The ID of the booking being reported (optional).
     * @param session            The authenticated user session.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirect to the appropriate management page with a success or error message.
     */
    @PostMapping("/create")
    public String createReport(@RequestParam("reason") String reason,
                               @RequestParam(required = false) Long packageId,
                               @RequestParam(required = false) Long reviewId,
                               @RequestParam(required = false) Long bookingId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to create a report.");
            return "redirect:/ProviderLogin"; // Redirect to login page if user is not logged in
        }

        try {
            // Create the report using the service
            reportService.createReport(loggedInUser, reason, packageId, reviewId, bookingId);

            // Determine the type of report and redirect accordingly with a success message
            if (bookingId != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Booking reported successfully.");
                return "redirect:/provider/manageBookings"; // Redirect to Manage Bookings page
            } else if (reviewId != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Review reported successfully.");
                return "redirect:/provider/replyToReviews"; // Redirect to Reply to Reviews page
            } else if (packageId != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Package reported successfully.");
                return "redirect:/provider/managePackages"; // Redirect to Manage Packages page
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Report submitted successfully.");
                return "redirect:/provider/dashboard"; // Redirect to Provider Dashboard
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Handle known exceptions and set an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating report: " + e.getMessage());
            return "redirect:/provider/dashboard"; // Redirect to Provider Dashboard
        } catch (Exception e) {
            // Handle unexpected exceptions
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred while creating the report.");
            return "redirect:/provider/dashboard"; // Redirect to Provider Dashboard
        }
    }

    /**
     * Retrieves all reports.
     * Endpoint: GET /reports
     *
     * @return List of all reports as a JSON response.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Report>> getAllReports() {
        // Fetch and return all reports as JSON
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Retrieves a report by ID.
     * Endpoint: GET /reports/{reportId}
     *
     * @param reportId The ID of the report.
     * @return The report as a JSON response if found.
     */
    @GetMapping("/{reportId}")
    @ResponseBody
    public ResponseEntity<Report> getReportById(@PathVariable Long reportId) {
        // Fetch the report by ID and return it if found, otherwise return a 404 response
        Optional<Report> report = reportService.getReportById(reportId);
        return report.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
