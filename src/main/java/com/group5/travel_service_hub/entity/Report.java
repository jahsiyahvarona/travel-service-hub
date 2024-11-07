package com.group5.travel_service_hub.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.group5.travel_service_hub.entity.ReportStatus;
import com.group5.travel_service_hub.entity.PunishmentType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Long id;

    // User who made the report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User reporter;

    // Optional: Package being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Package pkg;

    // Optional: review being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Reviews Review;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    // Admin who reviewed the report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User reviewedBy;

    @Enumerated(EnumType.STRING)
    private PunishmentType punishmentType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors
    public Report() {}

    public Report(User reporter, Package pkg, Reviews Review, String reason, ReportStatus status, User reviewedBy, PunishmentType punishmentType, LocalDateTime timestamp) {
        this.reporter = reporter;
        this.pkg = pkg;
        this.Review = Review;
        this.reason = reason;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.punishmentType = punishmentType;
        this.timestamp = timestamp;
    }

    public Report( User reporter, Package pkg, Reviews review, String reason, ReportStatus status, LocalDateTime timestamp) {
        this.reporter = reporter;
        this.pkg = pkg;
        this.Review = review;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Report(Long id, User reporter, Package pkg, Reviews Review, String reason, ReportStatus status, User reviewedBy, PunishmentType punishmentType, LocalDateTime timestamp) {
        this.id = id;
        this.reporter = reporter;
        this.pkg = pkg;
        this.Review = Review;
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
        return Review;
    }

    public void setReview(Reviews review) {
        Review = review;
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
