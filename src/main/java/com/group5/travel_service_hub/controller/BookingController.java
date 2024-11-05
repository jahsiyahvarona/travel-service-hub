package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Booking;
import com.group5.travel_service_hub.entity.BookingStatus;
import com.group5.travel_service_hub.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing bookings.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Creates a new booking.
     *
     * @param customerId The ID of the customer.
     * @param packageId  The ID of the package.
     * @param startDate  The start date of the booking.
     * @param endDate    The end date of the booking.
     * @return The created Booking entity.
     */

    @PostMapping("/new")
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long customerId,
            @RequestParam Long packageId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        Booking booking = bookingService.createBooking(customerId, packageId, startDate, endDate);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    /**
     * Retrieves all bookings made by a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return List of Booking entities.
     */

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getBookingsByCustomerId(@PathVariable Long customerId) {
        List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    /**
     * Retrieves all bookings for a specific package.
     *
     * @param packageId The ID of the package.
     * @return List of Booking entities.
     */

    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<Booking>> getBookingsByPackageId(@PathVariable Long packageId) {
        List<Booking> bookings = bookingService.getBookingsByPackageId(packageId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    /**
     * Retrieves all bookings for packages offered by a specific provider.
     *
     * @param providerId The ID of the provider.
     * @return List of Booking entities.
     */

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Booking>> getBookingsByProviderId(@PathVariable Long providerId) {
        List<Booking> bookings = bookingService.getBookingsByProviderId(providerId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    /**
     * Updates the status of a booking.
     *
     * @param bookingId The ID of the booking.
     * @param status    The new status of the booking.
     * @return The updated Booking entity.
     */

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status) {
        Booking updatedBooking = bookingService.updateBookingStatus(bookingId, status);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    /**
     * Deletes a booking by ID.
     *
     * @param bookingId The ID of the booking.
     * @return ResponseEntity with a success message.
     */

    @DeleteMapping("delete/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>("Booking deleted successfully.", HttpStatus.OK);
    }

    /**
     * Retrieves all bookings with a specific status.
     *
     * @param status The status of the bookings to retrieve.
     * @return List of Booking entities.
     */

    @GetMapping("/status")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@RequestParam BookingStatus status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    /**
     * Retrieves all bookings.
     *
     * @return List of all Booking entities.
     */

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
