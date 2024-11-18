package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a report submitted by a user regarding a package, review, or booking.
 * A report includes details such as the reason, status, and any punishment associated with it.
 */
@Entity
@Table(name = "reports")
public class Report {

    // Unique identifier for the report
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who submitted the report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    // Optional: The package being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Package pkg;

    // Optional: The review being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Reviews review;

    // Optional: The booking being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // Reason provided by the reporter for submitting the report
    @Column(nullable = false)
    private String reason;

    // Status of the report (e.g., PENDING, ACCEPTED, REJECTED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    // The admin who reviewed the report, if applicable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    // Type of punishment assigned based on the report's resolution
    @Enumerated(EnumType.STRING)
    private PunishmentType punishmentType;

    // Timestamp indicating when the report was created
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors

    /**
     * Default constructor required by JPA.
     */
    public Report() {}

    /**
     * Constructor to create a new report instance.
     *
     * @param reporter      The user submitting the report.
     * @param pkg           The package being reported (optional).
     * @param review        The review being reported (optional).
     * @param booking       The booking being reported (optional).
     * @param reason        The reason for the report.
     * @param status        The initial status of the report.
     * @param reviewedBy    The admin who reviewed the report (optional).
     * @param punishmentType The type of punishment assigned (optional).
     * @param timestamp     The timestamp of when the report was created.
     */
    public Report(User reporter, Package pkg, Reviews review, Booking booking, String reason, ReportStatus status, User reviewedBy, PunishmentType punishmentType, LocalDateTime timestamp) {
        this.reporter = reporter;
        this.pkg = pkg;
        this.review = review;
        this.booking = booking;
        this.reason = reason;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.punishmentType = punishmentType;
        this.timestamp = timestamp;
    }

    /**
     * Constructor to initialize a report with all fields, including ID.
     *
     * @param id            The ID of the report.
     * @param reporter      The user submitting the report.
     * @param pkg           The package being reported (optional).
     * @param review        The review being reported (optional).
     * @param booking       The booking being reported (optional).
     * @param reason        The reason for the report.
     * @param status        The initial status of the report.
     * @param reviewedBy    The admin who reviewed the report (optional).
     * @param punishmentType The type of punishment assigned (optional).
     * @param timestamp     The timestamp of when the report was created.
     */
    public Report(Long id, User reporter, Package pkg, Reviews review, Booking booking, String reason, ReportStatus status, User reviewedBy, PunishmentType punishmentType, LocalDateTime timestamp) {
        this.id = id;
        this.reporter = reporter;
        this.pkg = pkg;
        this.review = review;
        this.booking = booking;
        this.reason = reason;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.punishmentType = punishmentType;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public Reviews getReview() {
        return review;
    }

    public void setReview(Reviews review) {
        this.review = review;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public User getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(User reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(PunishmentType punishmentType) {
        this.punishmentType = punishmentType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
