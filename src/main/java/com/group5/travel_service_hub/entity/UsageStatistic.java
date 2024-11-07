package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage_statistics")
public class UsageStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType metricType; // Enum: active_users, bookings, views, average_rating

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    private LocalDate dateRange;

    // Constructors, Getters and Setters

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum MetricType {
        ACTIVE_USERS,
        BOOKINGS,
        VIEWS,
        AVERAGE_RATING
    }

    public UsageStatistic() {}

    public UsageStatistic(MetricType metricType, BigDecimal value, LocalDate startDate, LocalDate endDate) {
        this.metricType = metricType;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Additional Getters and Setters
    public Long getStatId() { return statId; }

    public MetricType getMetricType() { return metricType; }

    public BigDecimal getValue() { return value; }

    public LocalDate getStartDate() { return startDate; }

    public LocalDate getEndDate() { return endDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setDateRange(LocalDate dateRange) {
        this.dateRange = dateRange;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
