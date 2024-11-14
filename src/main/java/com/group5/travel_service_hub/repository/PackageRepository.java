package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Package;

import com.group5.travel_service_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    /**
     * Finds all packages associated with a specific ProviderDetails ID.
     *
     * @param providerId The ID of the ProviderDetails.
     * @return List of Package objects.
     */
    @Query(value = "SELECT * FROM packages WHERE provider_details_id = :providerId", nativeQuery = true)
    List<Package> findByProviderDetailsId(@Param("providerId") Long providerId);

    /**findByProviderDetailsId
     * Finds all packages where the name contains the specified string (case-insensitive).
     *
     * @param name The string to search for within package names.
     * @return List of Package objects.
     */
    List<Package> findByNameContainingIgnoreCase(String name);


    List<Package> findByProviderDetailsIdAndNameContainingIgnoreCase(Long providerId, String name);

}
