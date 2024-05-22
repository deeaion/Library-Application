package server.config;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifySubscribers(String message) {
        messagingTemplate.convertAndSend("/topic/clients", message);
    }

    public void notifyAdmins(String message) {
        messagingTemplate.convertAndSend("/topic/admins", message);
    }

    public void notifyLibrarians(String message) {
        messagingTemplate.convertAndSend("/topic/librarians", message);
    }
}
