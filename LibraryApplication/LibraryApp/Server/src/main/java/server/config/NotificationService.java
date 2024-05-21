package server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifySubscribers(String message) {
        logger.info("Sending notification to /topic/clients: {}", message);
        messagingTemplate.convertAndSend("/clients", message);
    }

    public void notifyLibrarians(String message) {
        logger.info("Sending notification to /topic/librarians: {}", message);
        messagingTemplate.convertAndSend("/librarians", message);
    }

    public void notifyAdmins(String message) {
        logger.info("Sending notification to /topic/admins: {}", message);
        messagingTemplate.convertAndSend("/admins", message);
    }
}
