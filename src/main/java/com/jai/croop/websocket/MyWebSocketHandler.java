package com.jai.croop.websocket;

import com.jai.croop.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketService webSocketService;

    @Autowired
    public MyWebSocketHandler(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();
        System.out.println("Received: " + clientMessage);
        session.sendMessage(new TextMessage("Server: " + clientMessage));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Client connected: " + session.getId());
        webSocketService.addSession(session);
        session.sendMessage(new TextMessage("Hello! WebSocket connection established."));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Client disconnected: " + session.getId());
        webSocketService.removeSession(session);
    }
}

