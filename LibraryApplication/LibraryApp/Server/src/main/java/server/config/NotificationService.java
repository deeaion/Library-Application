package server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.config.AdminWebSocketHandler;

@Service
public class NotificationService {

    @Autowired
    private AdminWebSocketHandler adminwebSocketHandler;
    @Autowired
    private LibrarianWebSocketHandler librarianWebSocketHandler;
    @Autowired
    private ClientWebSocketHandler subscriberWebSocketHandler;

    public void notifySubscribers(String message) throws Exception {
        subscriberWebSocketHandler.broadcast(message);
    }
    public void notifyLibrarians(String message) throws Exception {
        librarianWebSocketHandler.broadcast(message);
    }
    public void notifyAdmins(String message) throws Exception {
        adminwebSocketHandler.broadcast(message);
    }
}
