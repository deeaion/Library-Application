package server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import server.config.NotificationService;

@Controller
public class WebSocketController {

    @Autowired
    private NotificationService notificationService;
    @MessageMapping("/clients")
    public void sendNotification(@Payload String message) {
        notificationService.notifySubscribers(message);
    }
}
