package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Booking;
import com.group5.travel_service_hub.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds all bookings made by a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return List of Booking entities.
     */
    List<Booking> findByCustomerId(Long customerId);

    /**
     * Finds all bookings for a specific package.
     *
     * @param packageId The ID of the package.
     * @return List of Booking entities.
     */
    List<Booking> findByPkgId(Long packageId);

    /**
     * Finds all bookings associated with packages offered by a specific provider.
     *
     * @param providerDetailsId The ID of the provider.
     * @return List of Booking entities.
     */
    List<Booking> findByPkgProviderDetailsId(Long providerDetailsId);

    /**
     * Finds all bookings with a specific status.
     *
     * @param status The booking status.
     * @return List of Booking entities.
     */
    List<Booking> findByStatus(BookingStatus status);
}

