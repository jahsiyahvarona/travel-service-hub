package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Booking;
import com.group5.travel_service_hub.entity.BookingStatus;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.service.BookingService;
import com.group5.travel_service_hub.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Changed from @RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for managing bookings.
 */
@Controller // Changed from @RestController
@RequestMapping("/provider")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    /**
     * Unconfirms a booking by setting its status back to UNCONFIRMED.
     *
     * @param bookingId          The ID of the booking to unconfirm.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/unconfirmBooking")
    public String unconfirmBooking(@RequestParam Long bookingId,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Update booking status to UNCONFIRMED
            bookingService.updateBookingStatus(bookingId, BookingStatus.UNCONFIRMED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking unconfirmed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error unconfirming booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings";
    }

    /**
     * Confirms a booking by setting its status to CONFIRMED.
     *
     * @param bookingId          The ID of the booking to confirm.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/confirmBooking")
    public String confirmBooking(@RequestParam Long bookingId,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Update booking status to CONFIRMED
            bookingService.updateBookingStatus(bookingId, BookingStatus.CONFIRMED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking confirmed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error confirming booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings";
    }

    /**
     * Denies a booking by setting its status to DENIED.
     *
     * @param bookingId          The ID of the booking to deny.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/denyBooking")
    public String denyBooking(@RequestParam Long bookingId,
                              RedirectAttributes redirectAttributes) {
        try {
            // Update booking status to DENIED
            bookingService.updateBookingStatus(bookingId, BookingStatus.DENIED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking denied successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error denying booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings";
    }

    /**
     * Reports a booking by setting its status to REPORTED.
     *
     * @param bookingId          The ID of the booking to report.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirects to the manage bookings page.
     */
    @PostMapping("/reportBooking")
    public String reportBooking(@RequestParam Long bookingId,
                                RedirectAttributes redirectAttributes) {
        try {
            // Update booking status to REPORTED
            bookingService.updateBookingStatus(bookingId, BookingStatus.REPORTED);
            redirectAttributes.addFlashAttribute("successMessage", "Booking reported successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error reporting booking: " + e.getMessage());
        }
        return "redirect:/provider/manageBookings";
    }

    // Other methods as needed...
}
