package com.jai.croop.service;

import com.jai.croop.model.GroupSellersProductsInventory;
import com.jai.croop.repository.GroupSellersProductsRepository;
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
    private GroupSellersProductsRepository groupSellersProductInventoryService;

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void notifyRestockBasedOnDemand(int productId, int groupSellerId) {
        GroupSellersProductsInventory productsInventory = groupSellersProductInventoryService.findById(productId).orElseThrow(
                ()-> new RuntimeException("Notification couldn't find the product."));
        String name = productsInventory.getItemName();
        String message = "Restock needed for Product: " +name + " based on demand. ";
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