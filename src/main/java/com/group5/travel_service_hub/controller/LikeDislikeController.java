package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.LikeDislike;
import com.group5.travel_service_hub.service.LikeDislikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing likes and dislikes on packages.
 */
@RestController
@RequestMapping("/api/like-dislikes") // Maps all endpoints in this controller to URLs starting with "/api/like-dislikes"
public class LikeDislikeController {

    @Autowired
    private LikeDislikeService likeDislikeService; // Service for managing LikeDislike-related operations

    /**
     * Adds a like or dislike to a package.
     *
     * @param userId    The ID of the user who is reacting.
     * @param packageId The ID of the package being liked or disliked.
     * @param isLike    True if the reaction is a like, false if it is a dislike.
     * @return A ResponseEntity containing the created LikeDislike object with a CREATED (201) status.
     */
    @PostMapping("/add") // Handles POST requests to "/api/like-dislikes/add"
    public ResponseEntity<LikeDislike> addLikeDislike(
            @RequestParam Long userId,
            @RequestParam Long packageId,
            @RequestParam boolean isLike) {
        // Call the service method to add the like or dislike
        LikeDislike createdLikeDislike = likeDislikeService.addLikeDislike(userId, packageId, isLike);
        return new ResponseEntity<>(createdLikeDislike, HttpStatus.CREATED);
    }

    /**
     * Counts the number of likes for a specific package.
     *
     * @param packageId The ID of the package for which likes are being counted.
     * @return A ResponseEntity containing the count of likes with an OK (200) status.
     */
    @GetMapping("/{packageId}/likes") // Handles GET requests to "/api/like-dislikes/{packageId}/likes"
    public ResponseEntity<Long> countLikes(@PathVariable Long packageId) {
        // Call the service method to get the like count
        Long likeCount = likeDislikeService.countLikes(packageId);
        return new ResponseEntity<>(likeCount, HttpStatus.OK);
    }

    /**
     * Counts the number of dislikes for a specific package.
     *
     * @param packageId The ID of the package for which dislikes are being counted.
     * @return A ResponseEntity containing the count of dislikes with an OK (200) status.
     */
    @GetMapping("/{packageId}/dislikes") // Handles GET requests to "/api/like-dislikes/{packageId}/dislikes"
    public ResponseEntity<Long> countDislikes(@PathVariable Long packageId) {
        // Call the service method to get the dislike count
        Long dislikeCount = likeDislikeService.countDislikes(packageId);
        return new ResponseEntity<>(dislikeCount, HttpStatus.OK);
    }

    /**
     * Removes a like or dislike based on its ID.
     *
     * @param likeDislikeId The ID of the like or dislike to remove.
     * @return A ResponseEntity containing a success message with an OK (200) status.
     */
    @DeleteMapping("/remove/{likeDislikeId}") // Handles DELETE requests to "/api/like-dislikes/remove/{likeDislikeId}"
    public ResponseEntity<String> removeLikeDislike(@PathVariable Long likeDislikeId) {
        // Call the service method to remove the like or dislike
        likeDislikeService.removeLikeDislike(likeDislikeId);
        return new ResponseEntity<>("Like/Dislike removed successfully.", HttpStatus.OK);
    }
}