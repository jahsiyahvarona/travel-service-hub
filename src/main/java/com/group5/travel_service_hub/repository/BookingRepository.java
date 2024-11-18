package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Booking;
import com.group5.travel_service_hub.entity.BookingStatus;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Booking entity.
 * Extends JpaRepository to provide default implementations for common operations.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Counts the number of bookings associated with a specific package.
     *
     * @param pkg The package entity.
     * @return The count of bookings for the specified package.
     */
    long countByPkg(Package pkg);

    /**
     * Retrieves all bookings made by a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of Booking entities made by the customer.
     */
    List<Booking> findByCustomerId(Long customerId);

    /**
     * Retrieves all bookings associated with a specific package.
     *
     * @param packageId The ID of the package.
     * @return A list of Booking entities for the specified package.
     */
    List<Booking> findByPkgId(Long packageId);

    /**
     * Retrieves all distinct bookings associated with packages offered by a specific provider.
     *
     * @param providerDetailsId The ID of the provider.
     * @return A list of distinct Booking entities linked to the provider's packages.
     */
    @Query("SELECT DISTINCT b FROM Booking b JOIN b.pkg p WHERE p.providerDetails.id = :providerDetailsId")
    List<Booking> findAllDistinctBookingsByProviderDetailsId(@Param("providerDetailsId") Long providerDetailsId);

    /**
     * Retrieves all bookings with a specific status.
     *
     * @param status The BookingStatus enum value.
     * @return A list of Booking entities with the specified status.
     */
    List<Booking> findByStatus(BookingStatus status);

    /**
     * Retrieves all bookings made for packages offered by a specific provider.
     *
     * @param provider The User entity representing the provider.
     * @return A list of Booking entities linked to the provider's packages.
     */
    @Query("SELECT b FROM Booking b WHERE b.pkg.providerDetails = :provider")
    List<Booking> findByProvider(@Param("provider") User provider);
}
