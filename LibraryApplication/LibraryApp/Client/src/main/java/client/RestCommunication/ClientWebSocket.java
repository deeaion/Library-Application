package client.RestCommunication;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;

public class ClientWebSocket {

    private StompSession session;

    public void connect() {
        WebSocketClient client = new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient())));
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.connect("http://localhost:8080/ws", new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession stompSession, StompHeaders connectedHeaders) {
                session = stompSession;
                session.subscribe("/topic/notifications", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("Received: " + payload);
                        // Handle the notification
                        // Update the spinner max value here
                    }
                });
            }
        });
    }

    public StompSession getSession() {
        return session;
    }
}
