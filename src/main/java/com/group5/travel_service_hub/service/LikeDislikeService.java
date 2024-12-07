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
     * @param userId The ID of the user.
     * @param packageId The ID of the package.
     * @param isLike True for like, false for dislike.
     * @return The created LikeDislike object.
     */
    @Transactional
    public LikeDislike addLikeDislike(Long userId, Long packageId, boolean isLike) {
        if (likeDislikeRepository.existsByPkgIdAndUserId(packageId, userId)) {
            throw new IllegalArgumentException("User has already reacted to this package.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));

        LikeDislike likeDislike = new LikeDislike(isLike, pkg, user,pkg.getProviderDetails());
        return likeDislikeRepository.save(likeDislike);
    }

    /**
     * Counts the number of likes for a specific package.
     *
     * @param packageId The ID of the package.
     * @return The number of likes.
     */
    public Long countLikes(Long packageId) {
        return likeDislikeRepository.countByPkgIdAndIsLikeTrue(packageId);
    }

    /**
     * Counts the number of dislikes for a specific package.
     *
     * @param packageId The ID of the package.
     * @return The number of dislikes.
     */
    public Long countDislikes(Long packageId) {
        return likeDislikeRepository.countByPkgIdAndIsLikeFalse(packageId);
    }

    /**
     * Removes a like or dislike.
     *
     * @param likeDislikeId The ID of the LikeDislike to delete.
     */
    @Transactional
    public void removeLikeDislike(Long likeDislikeId) {
        LikeDislike likeDislike = likeDislikeRepository.findById(likeDislikeId)
                .orElseThrow(() -> new IllegalArgumentException("Like/Dislike not found."));
        likeDislikeRepository.delete(likeDislike);
    }
}
