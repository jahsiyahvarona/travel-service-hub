package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who triggers the notification
    @ManyToOne
    @JoinColumn(name = "notifier_id")
    private User notifier;

    // User who receives the notification
    @ManyToOne
    @JoinColumn(name = "notifee_id")
    private User notifee;

    // Reason for the notification
    @Enumerated(EnumType.STRING)
    private NotificationReason reason;

    private String message;

    private String targetUrl;

    // Timestamp when the notification was created
    private LocalDateTime createdAt;

    // Flag to check if the notification has been read
    private boolean isRead;


    public Notification(Long id, User notifier, User notifee, NotificationReason reason, LocalDateTime createdAt, boolean isRead, String message ,String targetUrl) {
        this.id = id;
        this.notifier = notifier;
        this.notifee = notifee;
        this.reason = reason;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.message = message;
        this.targetUrl = targetUrl;
    }

    // Constructors
    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public Notification(User notifier, User notifee, NotificationReason reason, String message, String targetUrl) {
        this();
        this.notifier = notifier;
        this.notifee = notifee;
        this.reason = reason;
        this.message = message;
        this.targetUrl = targetUrl;
    }


    // Getters and Setters
    // ... (Include getters and setters for all fields)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getNotifier() {
        return notifier;
    }

    public void setNotifier(User notifier) {
        this.notifier = notifier;
    }

    public User getNotifee() {
        return notifee;
    }

    public void setNotifee(User notifee) {
        this.notifee = notifee;
    }

    public NotificationReason getReason() {
        return reason;
    }

    public void setReason(NotificationReason reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}