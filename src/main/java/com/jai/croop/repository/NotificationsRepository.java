package com.jai.croop.repository;

import com.jai.croop.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findByUserIDAndUserType(int userID, String userType);
}
