package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on the Reviews entity.
 * Extends JpaRepository to provide built-in methods for database interaction.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    /**
     * Retrieves all reviews associated with a specific provider.
     *
     * @param providerId The ID of the provider's details.
     * @return A list of Reviews entities related to the specified provider.
     */
    List<Reviews> findByProviderId(Long providerId);

    /**
     * Retrieves all reviews associated with a specific package.
     *
     * @param packageId The ID of the package.
     * @return A list of Reviews entities related to the specified package.
     */
    List<Reviews> findByPkgId(Long packageId);

    /**
     * Retrieves all reviews written by a specific author.
     *
     * @param authorId The ID of the user who authored the reviews.
     * @return A list of Reviews entities created by the specified author.
     */
    List<Reviews> findByAuthorId(Long authorId);

    /**
     * Deletes a specific review entity from the database.
     *
     * @param review The review entity to be deleted.
     */
    void delete(Reviews review);
}
