package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.LikeDislike;
import com.group5.travel_service_hub.service.LikeDislikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like-dislikes")
public class LikeDislikeController {

    @Autowired
    private LikeDislikeService likeDislikeService;

    /**
     * Adds a like or dislike to a package.
     *
     * @param userId The ID of the user.
     * @param packageId The ID of the package.
     * @param isLike True for like, false for dislike.
     * @return The created LikeDislike object.
     */

    @PostMapping("/add")
    public ResponseEntity<LikeDislike> addLikeDislike(
            @RequestParam Long userId,
            @RequestParam Long packageId,
            @RequestParam boolean isLike) {
        LikeDislike createdLikeDislike = likeDislikeService.addLikeDislike(userId, packageId, isLike);
        return new ResponseEntity<>(createdLikeDislike, HttpStatus.CREATED);
    }

    /**
     * Counts the number of likes for a package.
     *
     * @param packageId The ID of the package.
     * @return The count of likes.
     */
    @GetMapping("/{packageId}/likes")
    public ResponseEntity<Long> countLikes(@PathVariable Long packageId) {
        Long likeCount = likeDislikeService.countLikes(packageId);
        return new ResponseEntity<>(likeCount, HttpStatus.OK);
    }

    /**
     * Counts the number of dislikes for a package.
     *
     * @param packageId The ID of the package.
     * @return The count of dislikes.
     */
    @GetMapping("/{packageId}/dislikes")
    public ResponseEntity<Long> countDislikes(@PathVariable Long packageId) {
        Long dislikeCount = likeDislikeService.countDislikes(packageId);
        return new ResponseEntity<>(dislikeCount, HttpStatus.OK);
    }

    /**
     * Removes a like or dislike by ID.
     *
     * @param likeDislikeId The ID of the like/dislike to remove.
     * @return A success message.
     */

    @DeleteMapping("/remove/{likeDislikeId}")
    public ResponseEntity<String> removeLikeDislike(@PathVariable Long likeDislikeId) {
        likeDislikeService.removeLikeDislike(likeDislikeId);
        return new ResponseEntity<>("Like/Dislike removed successfully.", HttpStatus.OK);
    }
}
