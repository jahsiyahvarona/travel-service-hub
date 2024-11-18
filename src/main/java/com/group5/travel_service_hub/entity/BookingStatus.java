package com.group5.travel_service_hub.entity;

/**
 * Enum representing the various statuses a booking can have in the system.
 */
public enum BookingStatus {
    /**
     * The booking has been created but not yet confirmed by the provider.
     */
    UNCONFIRMED,

    /**
     * The booking has been reviewed and confirmed by the provider.
     */
    CONFIRMED,

    /**
     * The booking has been reviewed and denied by the provider.
     * This might occur due to unavailability or other issues.
     */
    DENIED,

    /**
     * The booking has been flagged or reported for issues such as fraud or misuse.
     */
    REPORTED
}
