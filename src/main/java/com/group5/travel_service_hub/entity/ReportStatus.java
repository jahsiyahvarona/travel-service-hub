package com.group5.travel_service_hub.entity;

/**
 * Enum representing the status of a report in the system.
 */
public enum ReportStatus {
    /**
     * The report is pending review.
     * This is the initial status assigned when a report is created.
     */
    PENDING,

    /**
     * The report has been reviewed and rejected.
     * Indicates that no further action will be taken on this report.
     */
    REJECTED,

    /**
     * The report has been reviewed and accepted.
     * Indicates that action will be taken based on the contents of the report.
     */
    ACCEPTED
}
