package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // Customer who made the review
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Package being reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;

    // Provider who owns the package
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.ACTIVE;

    // **New Fields for Reply**
    @Column(columnDefinition = "TEXT")
    private String replyContent;

    private LocalDateTime replyTimestamp;

    // Constructors

    public Reviews() {
    }

    public Reviews(String content, User author, Package pkg, User provider, LocalDateTime timestamp) {
        this.content = content;
        this.author = author;
        this.pkg = pkg;
        this.provider = provider;
        this.timestamp = timestamp;
    }

    public Reviews(Long id, String content, User author, Package pkg, User provider, LocalDateTime timestamp) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.pkg = pkg;
        this.provider = provider;
        this.timestamp = timestamp;
    }






    // Getters and Setters
    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

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
// Override equals and hashCode...

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

    // Override toString for debugging

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