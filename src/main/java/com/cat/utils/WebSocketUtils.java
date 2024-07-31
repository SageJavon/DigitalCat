package com.cat.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtils {
    public static final Map<String, WebSocket> serverClients = new ConcurrentHashMap<String, WebSocket>();

    public static void sendMessage(String token,String sessionId) {
        for(WebSocket webSocket : serverClients.values()){
            if(token.equals(webSocket.getToken()) && webSocket.getSession() != null && webSocket.getSession().isOpen()) {
                webSocket.getSession().getAsyncRemote().sendText(sessionId);
            }
        }
    }
}
