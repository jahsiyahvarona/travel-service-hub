package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    /**
     * Finds all Reviews associated with a specific provider's details.
     *
     * @param providerId The ID of the provider.
     * @return List of Reviews entities.
     */
    List<Reviews> findByProviderId(Long providerId);

    /**
     * Finds all Reviews associated with a specific package.
     *
     * @param packageId The ID of the package.
     * @return List of Reviews entities.
     */
    List<Reviews> findByPkgId(Long packageId);

    /**
     * Finds all Reviews written by a specific author.
     *
     * @param authorId The ID of the author.
     * @return List of Reviews entities.
     */
    List<Reviews> findByAuthorId(Long authorId);

    void delete(Reviews review);
}