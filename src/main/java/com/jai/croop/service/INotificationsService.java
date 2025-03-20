package com.jai.croop.service;


import com.jai.croop.model.Notifications;

import java.util.List;

public interface INotificationsService {
    Notifications addNotification(Notifications notifications);
    List<Notifications> getAllNotifications();
    List<Notifications> findByUserIDAndUserType(int userID, String userType);
    Notifications getNotification(int id);
}
