package server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/message")
    @SendTo("/topic/client")
    public String handleMessageClient(String message) {
        return "Server received: " + message;
    }

    // Method to send notifications
    public void sendNotification(String message) {
        notificationService.notifySubscribers(message);
    }
}
