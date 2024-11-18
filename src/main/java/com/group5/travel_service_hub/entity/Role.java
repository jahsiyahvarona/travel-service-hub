package com.group5.travel_service_hub.entity;

/**
 * Enum representing the roles a user can have in the system.
 */
public enum Role {
    /**
     * The role assigned to service providers.
     * Users with this role can create, manage, and update packages and bookings.
     */
    PROVIDER,

    /**
     * The role assigned to customers.
     * Users with this role can browse, book packages, and leave reviews.
     */
    CUSTOMER,

    /**
     * The role assigned to system administrators.
     * Users with this role can manage users, review reports, and oversee system operations.
     */
    SYSADMIN
}