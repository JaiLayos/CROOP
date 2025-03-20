package com.jai.croop.service;

import com.jai.croop.model.Notifications;
import com.jai.croop.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService implements INotificationsService{
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Override
    public Notifications addNotification(Notifications notifications) {
        return notificationsRepository.save(notifications);
    }

    @Override
    public List<Notifications> getAllNotifications() {
        return notificationsRepository.findAll();
    }

    @Override
    public List<Notifications> findByUserIDAndUserType(int id, String userType) {
        return notificationsRepository.findByUserIDAndUserType(id, userType);
    }

    @Override
    public Notifications getNotification(int id) {
        return notificationsRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Notification doesn't exist"));
    }
}
