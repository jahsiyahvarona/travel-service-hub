package com.group5.travel_service_hub.service;


import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.Reviews;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.ReviewRepository;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing comments.
 */
@Service
public class ReviewsService {
    // Injecting the ReviewRepository instance using @Autowired
    @Autowired
    private ReviewRepository ReviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;




    /**
     * Retrieves all Reviews for a package.
     *
     * @param packageId The ID of the package.
     * @return List of Reviews for the package.
     */
    public List<Reviews> getReviewsByPackage(Long packageId) {
        return ReviewRepository.findByPkgId(packageId);
    }



    /**
     * Deletes a Review by ID.
     *
     * @param ReviewId The ID of the Review to delete.
     */
    @Transactional
    public void deleteReview(Long ReviewId) {
        Reviews Review = ReviewRepository.findById(ReviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found."));
        ReviewRepository.delete(Review);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param reviewId The ID of the review.
     * @return The Review if found.
     * @throws IllegalArgumentException if the review is not found.
     */
    public Reviews getReviewById(Long reviewId) {
        return ReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));
    }


    /**
     * Adds a new review for a package.
     *
     * @param userId    The ID of the user creating the review.
     * @param packageId The ID of the package being reviewed.
     * @param content   The content of the review.
     * @return The created Reviews entity.
     */
    @Transactional
    public Reviews addReview(Long userId, Long packageId, String content) {
        // Retrieve the user by ID
        User user = userService.findById(userId);

        // Retrieve the package by ID
        Package pkg = packageService.getPackageById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + packageId));

        // Create a new Reviews entity and set its properties
        Reviews review = new Reviews();
        review.setAuthor(user);
        review.setPkg(pkg);
        review.setContent(content);

        // Save the review to the database
        return ReviewRepository.save(review);
    }
}

