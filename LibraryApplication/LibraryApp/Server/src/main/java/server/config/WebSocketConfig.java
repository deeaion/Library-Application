package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String endpoint = "/client-websocket";
        registry.addEndpoint(endpoint).addInterceptors(new CustomHandshakeInterceptor()).setAllowedOriginPatterns("*","ws://localhost:55555").withSockJS();
        endpoint = "/librarian-websocket";
        registry.addEndpoint(endpoint).setAllowedOriginPatterns("*","ws://localhost:55555").withSockJS();
        endpoint = "/admin-websocket";
        registry.addEndpoint(endpoint).setAllowedOriginPatterns("*","ws://localhost:55555").withSockJS();
    }


}
