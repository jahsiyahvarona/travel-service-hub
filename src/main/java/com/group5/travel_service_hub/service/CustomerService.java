package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing customer-specific operations.
 */
@Service
public class CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReportRepository reportRepository;

    /**
     * Ensures the given user ID belongs to a customer and retrieves the user.
     *
     * @param userId The ID of the user.
     * @return User object.
     * @throws IllegalArgumentException If the user is not found or not a customer.
     */
    private User getCustomerUser(Long userId) {
        // Retrieve the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        // Ensure the user is a customer
        if (user.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("User is not a customer.");
        }
        return user;
    }

    /**
     * Retrieves all available travel packages.
     *
     * @return List of Package objects.
     */
    public List<Package> getAllPackages() {
        // Fetch all packages from the repository
        return packageRepository.findAll();
    }

    /**
     * Retrieves a travel package by ID.
     *
     * @param packageId The ID of the package.
     * @return The Package object.
     */
    public Package getPackageById(Long packageId) {
        // Fetch the package by ID
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));
    }

    /**
     * Books a travel package for the customer.
     *
     * @param userId    The ID of the customer.
     * @param packageId The ID of the package to book.
     * @param booking   The Booking entity containing booking details.
     * @return The created Booking object.
     */
    @Transactional
    public Booking bookPackage(Long userId, Long packageId, Booking booking) {
        // Retrieve the customer and package
        User customer = getCustomerUser(userId);
        Package pkg = getPackageById(packageId);

        // Associate booking details with the customer and package
        booking.setCustomer(customer);
        booking.setPkg(pkg);

        // Save and return the booking
        return bookingRepository.save(booking);
    }

    /**
     * Retrieves all bookings made by the customer.
     *
     * @param userId The ID of the customer.
     * @return List of Booking objects.
     */
    public List<Booking> getBookingsByCustomer(Long userId) {
        // Retrieve the customer and their bookings
        User customer = getCustomerUser(userId);
        return bookingRepository.findByCustomerId(customer.getId());
    }

    /**
     * Cancels a booking made by the customer.
     *
     * @param userId    The ID of the customer.
     * @param bookingId The ID of the booking to cancel.
     */
    @Transactional
    public void cancelBooking(Long userId, Long bookingId) {
        // Retrieve the customer and booking
        User customer = getCustomerUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        // Check authorization and delete the booking
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new SecurityException("Unauthorized to cancel this booking.");
        }

        bookingRepository.delete(booking);
    }

    /**
     * Reports a package or review.
     *
     * @param userId      The ID of the customer.
     * @param report      The Report entity containing report details.
     * @param packageId   (Optional) The ID of the package being reported.
     * @param reviewId    (Optional) The ID of the comment being reported.
     * @return The created Report object.
     */
    @Transactional
    public Report createReport(Long userId, Report report, Long packageId, Long reviewId) {
        // Retrieve the customer and associate with the report
        User customer = getCustomerUser(userId);
        report.setReporter(customer);

        // Associate the report with a package or review
        if (packageId != null) {
            Package pkg = getPackageById(packageId);
            report.setPkg(pkg);
        } else if (reviewId != null) {
            Reviews review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found."));
            report.setReview(review);
        } else {
            throw new IllegalArgumentException("Report must target a package or a review.");
        }

        // Save and return the report
        return reportRepository.save(report);
    }

    /**
     * Retrieves all Reviews made by the customer.
     *
     * @param userId The ID of the customer.
     * @return List of Reviews objects.
     */
    public List<Reviews> getCommentsByCustomer(Long userId) {
        // Retrieve the customer and their reviews
        User customer = getCustomerUser(userId);
        return reviewRepository.findByAuthorId(customer.getId());
    }
}
