package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.LikeDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on LikeDislike entity.
 * Extends JpaRepository to provide built-in methods for database interactions.
 */
@Repository
public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    /**
     * Checks whether a user has already reacted (liked or disliked) to a specific package.
     *
     * @param pkgId The ID of the package being checked.
     * @param userId The ID of the user being checked.
     * @return True if a reaction (like or dislike) exists, otherwise false.
     */
    boolean existsByPkgIdAndUserId(Long pkgId, Long userId);

    /**
     * Counts the total number of likes for a specific package.
     *
     * @param pkgId The ID of the package for which likes are counted.
     * @return The total count of likes for the specified package.
     */
    Long countByPkgIdAndIsLikeTrue(Long pkgId);

    /**
     * Counts the total number of dislikes for a specific package.
     *
     * @param pkgId The ID of the package for which dislikes are counted.
     * @return The total count of dislikes for the specified package.
     */
    Long countByPkgIdAndIsLikeFalse(Long pkgId);
}
