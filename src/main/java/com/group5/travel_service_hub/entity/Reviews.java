package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing reviews submitted by customers for packages.
 * Includes fields for the review content, author, associated package, provider, timestamps, and optional reply content.
 */
@Entity
@Table(name = "reviews")
public class Reviews {

    // Primary key for the reviews table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Content of the review provided by the customer
    @Column(nullable = false)
    private String content;

    // Customer who authored the review
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Package being reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;

    // Provider who owns the package being reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    // Timestamp when the review was submitted
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Status of the review (e.g., ACTIVE, FLAGGED, DELETED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.ACTIVE;

    // Reply content from the provider (optional)
    @Column(columnDefinition = "TEXT")
    private String replyContent;

    // Timestamp when the reply was submitted (optional)
    private LocalDateTime replyTimestamp;

    // Constructors

    /**
     * Default constructor required by JPA.
     */
    public Reviews() {}

    /**
     * Constructor for creating a new review.
     *
     * @param content   The review content provided by the customer.
     * @param author    The customer authoring the review.
     * @param pkg       The package being reviewed.
     * @param provider  The provider who owns the package.
     * @param timestamp The timestamp of when the review was submitted.
     */
    public Reviews(String content, User author, Package pkg, User provider, LocalDateTime timestamp) {
        this.content = content;
        this.author = author;
        this.pkg = pkg;
        this.provider = provider;
        this.timestamp = timestamp;
    }

    /**
     * Constructor for initializing all fields, including the ID.
     *
     * @param id        The ID of the review.
     * @param content   The review content provided by the customer.
     * @param author    The customer authoring the review.
     * @param pkg       The package being reviewed.
     * @param provider  The provider who owns the package.
     * @param timestamp The timestamp of when the review was submitted.
     */
    public Reviews(Long id, String content, User author, Package pkg, User provider, LocalDateTime timestamp) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.pkg = pkg;
        this.provider = provider;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public LocalDateTime getReplyTimestamp() {
        return replyTimestamp;
    }

    public void setReplyTimestamp(LocalDateTime replyTimestamp) {
        this.replyTimestamp = replyTimestamp;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    // Override equals and hashCode for entity comparison

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reviews reviews = (Reviews) o;
        return Objects.equals(id, reviews.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Override toString for easier debugging

    @Override
    public String toString() {
        return "Reviews{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", author=" + (author != null ? author.getId() : null) +
                ", pkg=" + (pkg != null ? pkg.getId() : null) +
                ", provider=" + (provider != null ? provider.getId() : null) +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", replyContent='" + replyContent + '\'' +
                ", replyTimestamp=" + replyTimestamp +
                '}';
    }
}