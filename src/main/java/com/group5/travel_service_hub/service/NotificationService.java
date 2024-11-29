package com.group5.travel_service_hub.service;
import com.group5.travel_service_hub.entity.Notification;
import com.group5.travel_service_hub.entity.NotificationReason;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // Inject the repository via constructor
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(User notifier, User notifee, NotificationReason reason, String message, String targetUrl) {
        Notification notification = new Notification(notifier, notifee, reason, message, targetUrl);
        notificationRepository.save(notification);
    }

    // Get notifications for a user
    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByNotifeeOrderByCreatedAtDesc(user);
    }

    // Mark a notification as read
    public void markAsRead(Notification notification) {
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // Mark all notifications as read for a user
    public void markAllAsRead(User user) {
        List<Notification> notifications = notificationRepository.findByNotifeeOrderByCreatedAtDesc(user);
        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                notification.setRead(true);
            }
        }
        notificationRepository.saveAll(notifications);
    }
}