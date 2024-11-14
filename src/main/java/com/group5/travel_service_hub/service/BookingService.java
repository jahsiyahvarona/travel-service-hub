package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Booking;
import com.group5.travel_service_hub.entity.BookingStatus;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.BookingRepository;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing bookings.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PackageRepository packageRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new booking for a specific customer and package.
     *
     * @param customerId The ID of the customer making the booking.
     * @param packageId  The ID of the package being booked.
     * @param startDate  The start date of the booking.
     * @param endDate    The end date of the booking.
     * @return The created Booking entity.
     */
    @Transactional
    public Booking createBooking(Long customerId, Long packageId, LocalDate startDate, LocalDate endDate) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + packageId));

        // Capture the current price of the package
        double currentPrice = pkg.getPrice();

        // Create a new Booking instance with the captured price
        Booking booking = new Booking(customer, pkg, currentPrice, startDate, endDate, BookingStatus.UNCONFIRMED);
        return bookingRepository.save(booking);
    }

    /**
     * Retrieves all bookings made by a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return List of Booking entities.
     */
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    /**
     * Retrieves all bookings for a specific package.
     *
     * @param packageId The ID of the package.
     * @return List of Booking entities.
     */
    public List<Booking> getBookingsByPackageId(Long packageId) {
        return bookingRepository.findByPkgId(packageId);
    }

    /**
     * Retrieves all bookings for packages offered by a specific provider.
     *
     * @param providerId The ID of the provider.
     * @return List of Booking entities.
     */
    public List<Booking> getBookingsByProviderId(Long providerId) {
        return bookingRepository.findAllDistinctBookingsByProviderDetailsId(providerId);
    }

    /**
     * Updates the status of a booking.
     *
     * @param bookingId The ID of the booking.
     * @param status    The new status of the booking.
     */
    @Transactional
    public void updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    /**
     * Deletes a booking by ID.
     *
     * @param bookingId The ID of the booking to delete.
     */
    @Transactional
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        bookingRepository.delete(booking);
    }

    /**
     * Retrieves all bookings with a specific status.
     *
     * @param status The booking status to filter by.
     * @return List of Booking entities.
     */
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    /**
     * Retrieves all bookings.
     *
     * @return List of all Booking entities.
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking.
     * @return Optional containing the Booking if found.
     */
    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }
}