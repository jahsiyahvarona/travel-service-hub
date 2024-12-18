package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.LikeDislike;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.LikeDislikeRepository;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing likes and dislikes for packages.
 */
@Service
public class LikeDislikeService {

    @Autowired
    private LikeDislikeRepository likeDislikeRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a like or dislike to a package.
     *
     * @param userId    The ID of the user.
     * @param packageId The ID of the package.
     * @param isLike    True for like, false for dislike.
     * @throws IllegalArgumentException If the user has already reacted to the package.
     */
    @Transactional
    public void addLikeDislike(Long userId, Long packageId, boolean isLike) {
        // Check if the user has already reacted to this package
        if (likeDislikeRepository.existsByPkgIdAndUserId(packageId, userId)) {
            throw new IllegalArgumentException("User has already reacted to this package.");
        }

        // Fetch the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Fetch the package by ID
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));

        // Create and save a new LikeDislike entity
        LikeDislike likeDislike = new LikeDislike(isLike, pkg, user, pkg.getProviderDetails());
        likeDislikeRepository.save(likeDislike);
    }

    /**
     * Counts the number of likes for a specific package.
     *
     * @param packageId The ID of the package.
     * @return The number of likes for the package.
     */
    public Long countLikes(Long packageId) {
        // Count the number of likes for the given package
        return likeDislikeRepository.countByPkgIdAndIsLikeTrue(packageId);
    }

    /**
     * Counts the number of dislikes for a specific package.
     *
     * @param packageId The ID of the package.
     * @return The number of dislikes for the package.
     */
    public Long countDislikes(Long packageId) {
        // Count the number of dislikes for the given package
        return likeDislikeRepository.countByPkgIdAndIsLikeFalse(packageId);
    }

    /**
     * Removes a like or dislike by ID.
     *
     * @param likeDislikeId The ID of the LikeDislike to delete.
     * @throws IllegalArgumentException If the LikeDislike is not found.
     */
    @Transactional
    public void removeLikeDislike(Long likeDislikeId) {
        // Fetch the LikeDislike entity by ID
        LikeDislike likeDislike = likeDislikeRepository.findById(likeDislikeId)
                .orElseThrow(() -> new IllegalArgumentException("Like/Dislike not found."));

        // Delete the LikeDislike entity
        likeDislikeRepository.delete(likeDislike);
    }

    public Optional<LikeDislike> findUserReaction(Long userId, Long packageId) {
        // Query your repository for an existing like/dislike from this user for this package
        // This assumes you have a custom query or can add a method like:
        // Optional<LikeDislike> findByPkgIdAndUserId(Long pkgId, Long userId);
        return likeDislikeRepository.findByPkgIdAndUserId(packageId, userId);
    }

    @Transactional
    public void handleLike(Long userId, Long packageId) {
        Optional<LikeDislike> userReaction = likeDislikeRepository.findByPkgIdAndUserId(packageId, userId);
        if (userReaction.isPresent()) {
            LikeDislike reaction = userReaction.get();
            if (reaction.isLike()) {
                // User already liked, so remove the like
                likeDislikeRepository.delete(reaction);
            } else {
                // User had disliked, remove dislike, then add like
                likeDislikeRepository.delete(reaction);
                addLikeDislike(userId, packageId, true);
            }
        } else {
            // No previous reaction, add a like
            addLikeDislike(userId, packageId, true);
        }
    }

    @Transactional
    public void handleDislike(Long userId, Long packageId) {
        Optional<LikeDislike> userReaction = likeDislikeRepository.findByPkgIdAndUserId(packageId, userId);
        if (userReaction.isPresent()) {
            LikeDislike reaction = userReaction.get();
            if (!reaction.isLike()) {
                // User already disliked, remove the dislike
                likeDislikeRepository.delete(reaction);
            } else {
                // User had liked, remove like, then add dislike
                likeDislikeRepository.delete(reaction);
                addLikeDislike(userId, packageId, false);
            }
        } else {
            // No previous reaction, add a dislike
            addLikeDislike(userId, packageId, false);
        }
    }
}
