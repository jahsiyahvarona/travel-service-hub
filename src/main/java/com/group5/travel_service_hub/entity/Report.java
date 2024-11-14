package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who made the report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    // Optional: Package being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Package pkg;

    // Optional: Review being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Reviews review;

    // Optional: Booking being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;


    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    // Admin who reviewed the report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    @Enumerated(EnumType.STRING)
    private PunishmentType punishmentType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors
    public Report() {}

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
