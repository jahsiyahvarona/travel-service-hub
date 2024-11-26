package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.City;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on the Package entity.
 * Extends JpaRepository to provide basic methods for database interaction.
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    /**
     * Retrieves all packages associated with a specific provider.
     *
     * @param providerId The ID of the provider's details.
     * @return A list of Package objects related to the specified provider.
     */
    @Query(value = "SELECT * FROM packages WHERE provider_details_id = :providerId", nativeQuery = true)
    List<Package> findByProviderDetailsId(@Param("providerId") Long providerId);

    /**
     * Retrieves all packages where the name contains a specified string, ignoring case.
     *
     * @param name The substring to search for in package names.
     * @return A list of Package objects with names matching the specified substring.
     */
    List<Package> findByNameContainingIgnoreCase(String name);

    /**
     * Retrieves all packages for a specific provider where the name contains a specified string, ignoring case.
     *
     * @param providerId The ID of the provider's details.
     * @param name The substring to search for in package names.
     * @return A list of Package objects matching the criteria.
     */
    List<Package> findByProviderDetailsIdAndNameContainingIgnoreCase(Long providerId, String name);


        List<Package> findByLocation(City city);


    List<Package> findByLocationId(Long cityId);

    List<Package> findByNameContainingIgnoreCaseAndLocationId(String name, Long cityId);

}
