package client.RestCommunication.webSocket.admin;

import client.RestCommunication.webSocket.WebSocketMessageListener;
import client.RestCommunication.webSocket.client.ClientNotificationManager;

import java.util.ArrayList;
import java.util.List;

public class AdminNotificationManager {
    private static AdminNotificationManager instance;
    private List<WebSocketMessageListener> listeners = new ArrayList<>();

    private AdminNotificationManager() {
    }

    public static AdminNotificationManager getInstance() {
        if (instance == null) {
            instance = new AdminNotificationManager();
        }
        return instance;
    }

    public void addListener(WebSocketMessageListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(String message) {
        for (WebSocketMessageListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }
}
