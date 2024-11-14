package com.group5.travel_service_hub.entity;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // Package being booked
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;

    // Price at the time of booking; not updatable after creation
    @Column(nullable = false, updatable = false)
    private double price;

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

    public Booking(Long id, User customer, Package pkg, double price, LocalDate startDate, LocalDate endDate, BookingStatus status, LocalDateTime timestamp) {
        this.id = id;
        this.customer = customer;
        this.pkg = pkg;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.timestamp = timestamp;
    }

    /**
     * Constructor to create a new Booking instance.
     *
     * @param customer   The customer making the booking.
     * @param pkg        The package being booked.
     * @param price      The price at the time of booking.
     * @param startDate  The start date of the booking.
     * @param endDate    The end date of the booking.
     * @param status     The initial status of the booking.
     */
    public Booking(User customer, Package pkg, double price, LocalDate startDate, LocalDate endDate, BookingStatus status) {
        this.customer = customer;
        this.pkg = pkg;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Default constructor required by JPA
    public Booking() {}

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

    public double getPrice() {
        return price;
    }

    // No setter for price to prevent updates after creation

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
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
