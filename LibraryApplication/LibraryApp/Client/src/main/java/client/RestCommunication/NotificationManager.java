package client.RestCommunication;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private static NotificationManager instance;
    private List<WebSocketMessageListener> listeners = new ArrayList<>();

    private NotificationManager() {
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
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
