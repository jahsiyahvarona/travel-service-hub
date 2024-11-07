package com.group5.travel_service_hub.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer who made the booking


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // Package being booked
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;



    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // Constructors

    public Booking(User customer, Package pkg, LocalDate startDate, LocalDate endDate, BookingStatus status) {
        this.customer = customer;
        this.pkg = pkg;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Booking() {}

    public Booking(Long id, User customer, Package pkg, LocalDate startDate, LocalDate endDate, BookingStatus status) {
        this.id = id;
        this.customer = customer;
        this.pkg = pkg;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Booking(User customer, Package pkg, LocalDate startDate, LocalDate endDate, BookingStatus bookingStatus, LocalDateTime now) {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Automatically set the timestamp before persisting
    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    // Override equals and hashCode for entity comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Override toString for easier debugging
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customer=" + (customer != null ? customer.getId() : null) +
                ", package=" + (pkg != null ? pkg.getId() : null) +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
