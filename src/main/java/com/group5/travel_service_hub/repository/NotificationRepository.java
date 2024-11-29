package com.group5.travel_service_hub.repository;
import com.group5.travel_service_hub.entity.Notification;
import com.group5.travel_service_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByNotifeeOrderByCreatedAtDesc(User notifee);
}