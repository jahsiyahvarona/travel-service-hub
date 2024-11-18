package com.group5.travel_service_hub.entity;

/**
 * Enum representing the types of punishments that can be applied to a user or entity in the system.
 */
public enum PunishmentType {
    /**
     * No punishment has been issued.
     * Typically used as a default or when no action is necessary.
     */
    NONE,

    /**
     * A warning has been issued.
     * This serves as a notice to the user or entity about improper behavior or rule violation.
     */
    WARNING,

    /**
     * A temporary suspension has been applied.
     * The user or entity is restricted from accessing specific services or the system for a set duration.
     */
    SUSPENSION,

    /**
     * A permanent ban has been enforced.
     * The user or entity is completely barred from accessing the system or its services.
     */
    BAN
}
