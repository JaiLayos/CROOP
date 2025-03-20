package com.jai.croop.controller;

import com.jai.croop.model.Cart;
import com.jai.croop.model.CartGroupedResponseDTO;
import com.jai.croop.model.Notifications;
import com.jai.croop.service.INotificationsService;
import com.jai.croop.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/notifications")
public class NotificationsController {
    @Autowired
    INotificationsService notificationsService;

    @PostMapping
    public ResponseEntity<Notifications> addNotification(@RequestBody Notifications notifications) {
        return ResponseEntity.ok(notificationsService.addNotification(notifications));
    }

    @GetMapping("/{userID}/{userType}")
    public ResponseEntity<List<Notifications>> getNotificationOfUser(
            @PathVariable int userID,  @PathVariable String userType) {
        return ResponseEntity.ok(notificationsService.findByUserIDAndUserType(userID,userType));
    }
}
