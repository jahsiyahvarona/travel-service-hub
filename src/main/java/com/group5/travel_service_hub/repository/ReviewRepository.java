package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for the Reviews entity.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    /**
     * Finds all Reviews associated with a specific package.
     *
     * @param packageId The ID of the package.
     * @return List of Reviews entities.
     */
     List<Reviews> findByPkgId(Long packageId);

    /**
     * Finds all Reviews associated with a specific provider's details.
     *
     * @param id The ID of the provider details.
     * @return List of Reviews entities.
     */
    List<Reviews> findByPkgProviderDetailsId(Long id);

    /**
     * Finds all Reviews written by a specific author.
     *
     * @param id The ID of the author.
     * @return List of Reviews entities.
     */
    List<Reviews> findByAuthorId(Long id);

    void delete(Reviews review);
}

