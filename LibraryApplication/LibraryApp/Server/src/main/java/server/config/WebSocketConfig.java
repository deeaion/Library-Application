package server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new AdminWebSocketHandler(), "/admin-websocket")
                .setAllowedOrigins("*");
        registry.addHandler(new LibrarianWebSocketHandler(), "/librarian-websocket")
                .setAllowedOrigins("*");
        registry.addHandler(new ClientWebSocketHandler(), "/client-websocket")
                .setAllowedOrigins("*");
    }


}