package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.BookingStatus;
import com.group5.travel_service_hub.service.BookingService;
import com.group5.travel_service_hub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Indicates this class serves web requests
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for managing booking-related operations.
 */
@Controller // Marks this class as a Spring MVC controller handling web requests
@RequestMapping("/provider") // Maps all endpoints in this controller to URLs starting with "/provider"
public class BookingController {

    @Autowired
    private BookingService bookingService; // Service for managing booking operations

    @Autowired
    private UserService userService; // Service for managing user-related operations

    /**
     * Handles unconfirming a booking by setting its status back to UNCONFIRMED.
     *
     * @param bookingId          The ID of the booking to unconfirm.
     * @param redirectAttributes Used to pass flash messages to the redirected view.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/unconfirmBooking") // Handles POST requests to "/provider/unconfirmBooking"
    public String unconfirmBooking(@RequestParam Long bookingId,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Update the status of the booking to UNCONFIRMED
            bookingService.updateBookingStatus(bookingId, BookingStatus.UNCONFIRMED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking unconfirmed successfully.");
        } catch (Exception e) {
            // Handle errors and provide feedback to the user
            redirectAttributes.addFlashAttribute("errorMessage", "Error unconfirming booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings"; // Redirects to the manage bookings page
    }

    /**
     * Handles confirming a booking by setting its status to CONFIRMED.
     *
     * @param bookingId          The ID of the booking to confirm.
     * @param redirectAttributes Used to pass flash messages to the redirected view.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/confirmBooking") // Handles POST requests to "/provider/confirmBooking"
    public String confirmBooking(@RequestParam Long bookingId,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Update the status of the booking to CONFIRMED
            bookingService.updateBookingStatus(bookingId, BookingStatus.CONFIRMED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking confirmed successfully.");
        } catch (Exception e) {
            // Handle errors and provide feedback to the user
            redirectAttributes.addFlashAttribute("errorMessage", "Error confirming booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings"; // Redirects to the manage bookings page
    }

    /**
     * Handles denying a booking by setting its status to DENIED.
     *
     * @param bookingId          The ID of the booking to deny.
     * @param redirectAttributes Used to pass flash messages to the redirected view.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/denyBooking") // Handles POST requests to "/provider/denyBooking"
    public String denyBooking(@RequestParam Long bookingId,
                              RedirectAttributes redirectAttributes) {
        try {
            // Update the status of the booking to DENIED
            bookingService.updateBookingStatus(bookingId, BookingStatus.DENIED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking denied successfully.");
        } catch (Exception e) {
            // Handle errors and provide feedback to the user
            redirectAttributes.addFlashAttribute("errorMessage", "Error denying booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings"; // Redirects to the manage bookings page
    }

    /**
     * Handles reporting a booking by setting its status to REPORTED.
     *
     * @param bookingId          The ID of the booking to report.
     * @param redirectAttributes Used to pass flash messages to the redirected view.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/reportBooking") // Handles POST requests to "/provider/reportBooking"
    public String reportBooking(@RequestParam Long bookingId,
                                RedirectAttributes redirectAttributes) {
        try {
            // Update the status of the booking to REPORTED
            bookingService.updateBookingStatus(bookingId, BookingStatus.REPORTED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking reported successfully.");
        } catch (Exception e) {
            // Handle errors and provide feedback to the user
            redirectAttributes.addFlashAttribute("errorMessage", "Error reporting booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings"; // Redirects to the manage bookings page
    }

    // Additional methods can be added here for handling other booking-related operations
}
