package client.RestCommunication.webSocket.librarian;

import client.RestCommunication.webSocket.WebSocketMessageListener;
import client.RestCommunication.webSocket.client.ClientNotificationManager;
import common.model.Librarian;

import java.util.ArrayList;
import java.util.List;

public class LibrarianNotificationManager {
    private static LibrarianNotificationManager instance;
    private List<WebSocketMessageListener> listeners = new ArrayList<>();

    private LibrarianNotificationManager () {
    }

    public static LibrarianNotificationManager  getInstance() {
        if (instance == null) {
            instance = new LibrarianNotificationManager ();
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
