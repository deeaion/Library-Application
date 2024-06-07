package client.RestCommunication.webSocket.client;

import client.RestCommunication.webSocket.WebSocketMessageListener;

import java.util.ArrayList;
import java.util.List;

public class ClientNotificationManager {

    private static ClientNotificationManager instance;
    private List<WebSocketMessageListener> listeners = new ArrayList<>();

    private ClientNotificationManager() {
    }

    public static ClientNotificationManager getInstance() {
        if (instance == null) {
            instance = new ClientNotificationManager();
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
