package com.group5.travel_service_hub.entity;

/**
 * Enum representing the status of a review in the system.
 */
public enum ReviewStatus {
    /**
     * The review is active and visible.
     * This status is assigned to reviews that are approved and accessible to users.
     */
    ACTIVE,

    /**
     * The review has been flagged for inappropriate content or other issues.
     * Further review or action may be required by moderators or admins.
     */
    FLAGGED,

    /**
     * The review has been deleted and is no longer visible.
     * This status is typically assigned when a review violates guidelines or is removed by the author or admin.
     */
    DELETED
}
