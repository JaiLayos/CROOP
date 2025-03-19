package com.jai.croop.service;

import ch.qos.logback.classic.Logger;
import com.jai.croop.controller.CustomerOrdersController;
import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.repository.GroupSellersProductsRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class WebSocketService {
    @Autowired
    private GroupSellersProductsRepository groupSellersProductsRepository;
    private static final Logger log = (Logger) LoggerFactory.getLogger(WebSocketService.class);


    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void notifyRestockBasedOnDemand(int productId, int groupSellerId) {
        log.info("Preparing to send WebSocket notification for productId: {}", productId);

        GroupSellersProductsInventory productsInventory = groupSellersProductsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Notification couldn't find the product."));

        String name = productsInventory.getItemName();
        String message = "Restock needed for Product: " + name + " based on demand.";

        log.warn("Sending WebSocket notification: {}", message);
        broadcast(message);
    }


    private void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace(); // Handle error properly in production
                }
            }
        }
    }
}