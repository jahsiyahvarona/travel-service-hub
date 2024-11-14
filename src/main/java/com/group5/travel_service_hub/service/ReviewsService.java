package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.ReviewStatus;
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
 * Service class for managing reviews.
 */
@Service
public class ReviewsService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    /**
     * Retrieves all Reviews for a provider's packages.
     *
     * @param providerId The ID of the provider.
     * @return List of Reviews for the provider.
     */
    public List<Reviews> getReviewsByProvider(Long providerId) {
        return reviewRepository.findByProviderId(providerId);
    }

    /**
     * Deletes a Review by ID.
     *
     * @param reviewId The ID of the Review to delete.
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found."));
        reviewRepository.delete(review);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param reviewId The ID of the review.
     * @return The Review if found.
     * @throws IllegalArgumentException if the review is not found.
     */
    public Reviews getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
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
        review.setProvider(pkg.getProviderDetails());
        review.setTimestamp(LocalDateTime.now());

        // Save the review to the database
        return reviewRepository.save(review);
    }

    /**
     * Allows a provider to reply to a review.
     *
     * @param reviewId The ID of the review to reply to.
     * @param reply    The reply content.
     */
    @Transactional
    public void replyToReview(Long reviewId, String reply) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        // Only allow replying if not already replied
        if (review.getReplyContent() != null) {
            throw new IllegalStateException("Review already has a reply.");
        }

        review.setReplyContent(reply);
        review.setReplyTimestamp(LocalDateTime.now());

        reviewRepository.save(review);
    }

    /**
     * Reports a review by changing its status to FLAGGED.
     *
     * @param reviewId The ID of the review to report.
     */
    @Transactional
    public void reportReview(Long reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        review.setStatus(ReviewStatus.FLAGGED);

        reviewRepository.save(review);
    }

    /**
     * Allows a provider to edit an existing reply to a review.
     *
     * @param reviewId     The ID of the review whose reply is being edited.
     * @param replyContent The updated content of the reply.
     * @throws IllegalArgumentException if the review does not exist.
     * @throws IllegalStateException    if the review does not have an existing reply.
     */
    @Transactional
    public void editReply(Long reviewId, String replyContent) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        // Check if the review has an existing reply
        if (review.getReplyContent() == null) {
            throw new IllegalStateException("Review does not have an existing reply to edit.");
        }

        // Update the reply content and timestamp
        review.setReplyContent(replyContent);
        review.setReplyTimestamp(LocalDateTime.now());

        // Save the updated review
        reviewRepository.save(review);
    }
    /**
     * Allows a provider to delete an existing reply to a review.
     *
     * @param reviewId The ID of the review whose reply is being deleted.
     * @throws IllegalArgumentException if the review does not exist.
     * @throws IllegalStateException    if the review does not have an existing reply.
     */
    @Transactional
    public void deleteReply(Long reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        // Check if the review has an existing reply
        if (review.getReplyContent() == null) {
            throw new IllegalStateException("Review does not have an existing reply to delete.");
        }

        // Remove the reply content and timestamp
        review.setReplyContent(null);
        review.setReplyTimestamp(null);

        // Save the updated review
        reviewRepository.save(review);
    }

}