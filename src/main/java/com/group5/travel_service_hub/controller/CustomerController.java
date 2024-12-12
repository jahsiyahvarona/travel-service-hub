package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private LikeDislikeService likeDislikeService;
    @Autowired
    private ReportService reportService;
    /**
     * Create a new Customer profile.
     *
     * @param customer The Customer entity.
     * @return The created Customer object.
     */
    @PostMapping("")
    public ResponseEntity<User> createCustomerProfile(@RequestBody User customer) {
        customer.setRole(Role.CUSTOMER);
        User createdProvider = userService.registerUser(customer);
        return ResponseEntity.ok(createdProvider);
    }

    /**
     * Get Customer profile by CustomerId.
     *
     * @param CustomerId The ID of the Customer.
     * @return The Provider object.
     */
    @GetMapping("/{CustomerId}")
    public ResponseEntity<User> getCustomerProfile(@PathVariable Long CustomerId) {
        User customer = userService.findById(CustomerId);
        return ResponseEntity.ok(customer);
    }

    /**
     * Update Customer profile.
     *
     * @param customerId The ID of the customer.
     * @param customer   The updated Customer details.
     * @return The updated Customer object.
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<User> updateCustomerProfile(
            @PathVariable Long customerId,
            @RequestBody User customer) {
        User updatedCustomer = userService.updateProfile(customerId, customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * Delete Customer profile.
     *
     * @param customerId The ID of the customer.
     * @return A confirmation message.
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomerProfile(@PathVariable Long customerId) {
        userService.deactivateUser(customerId);
        return ResponseEntity.ok("Customer profile deactivated successfully.");
    }
    /**
     * Subscribe to a service by creating a new booking.
     *
     * @param customerId The ID of the customer.
     * @param packageId  The ID of the package.
     * @param startDate  The start date of the booking.
     * @param endDate    The end date of the booking.
     * @return The created Booking object.
     */
    @PostMapping("/{customerId}/subscribe")
    public ResponseEntity<Object> subscribeToService(
            @PathVariable Long customerId,
            @RequestParam Long packageId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Booking newBooking = bookingService.createBooking(
                customerId,
                packageId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );

        return ResponseEntity.status(201).body(newBooking);
    }

    /**
     * Get all bookings for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return List of Booking objects.
     */
    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<List<Booking>> getAllBookingsForCustomer(@PathVariable Long customerId) {
        List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
        return ResponseEntity.ok(bookings);
    }
    /**
     * Update an existing Package.
     *
     * @param providerId The ID of the provider.
     * @param packageId  The ID of the package to update.
     * @param pkg        The updated Package details.
     * @return The updated Package object.
     */
    @PutMapping("/{providerId}/packages/{packageId}")
    public ResponseEntity<com.group5.travel_service_hub.entity.Package> updatePackage(
            @PathVariable Long providerId,
            @PathVariable Long packageId,
            @RequestBody com.group5.travel_service_hub.entity.Package pkg) {
        Package existingPackage = packageService.getPackageById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));
        if (!existingPackage.getProviderDetails().getId().equals(providerId)) {
            return ResponseEntity.status(403).build();
        }
        pkg.setId(packageId);
        pkg.setProviderDetails(existingPackage.getProviderDetails());
        packageService.updatePackage(pkg);
        return ResponseEntity.ok(pkg);
    }
    /**
     * Write a review for a subscribed service.
     *
     * @param customerId The ID of the customer writing the review.
     * @param packageId  The ID of the package being reviewed.
     * @param content    The review content.
     * @return The created Review object.
     */
    @PostMapping("/{customerId}/reviews")
    public ResponseEntity<Reviews> writeReview(
            @PathVariable Long customerId,
            @RequestParam Long packageId,
            @RequestParam String content) {

        // Ensure the customer has subscribed to the service
        boolean isSubscribed = bookingService.getBookingsByCustomerId(customerId).stream()
                .anyMatch(booking -> booking.getPkg().getId().equals(packageId));

        if (!isSubscribed) {
            return ResponseEntity.status(403).body(null); // Forbidden if not subscribed
        }

        Reviews newReview = reviewsService.addReview(customerId, packageId, content);
        return ResponseEntity.status(201).body(newReview);
    }
}