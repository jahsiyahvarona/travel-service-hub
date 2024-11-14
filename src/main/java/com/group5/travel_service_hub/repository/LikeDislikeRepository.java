package com.group5.travel_service_hub.repository;

import com.group5.travel_service_hub.entity.LikeDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for LikeDislike entity.
 */
@Repository
public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    /**
     * Checks if a user has already reacted (liked/disliked) to a package.
     *
     * @param pkgId The ID of the package.
     * @param userId The ID of the user.
     * @return True if the reaction exists, else false.
     */
    boolean existsByPkgIdAndUserId(Long pkgId, Long userId);

    /**
     * Counts the number of likes for a specific package.
     *
     * @param pkgId The ID of the package.
     * @return The count of likes.
     */
    Long countByPkgIdAndIsLikeTrue(Long pkgId);

    /**
     * Counts the number of dislikes for a specific package.
     *
     * @param pkgId The ID of the package.
     * @return The count of dislikes.
     */
    Long countByPkgIdAndIsLikeFalse(Long pkgId);
}
