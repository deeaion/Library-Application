package client.RestCommunication;

import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

public class WebSocketManager {

    private static WebSocketManager instance;
    private StompSession session;
    private final CopyOnWriteArrayList<WebSocketMessageListener> listeners = new CopyOnWriteArrayList<>();
    private boolean connected = false;

    private WebSocketManager() {
    }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public synchronized void connect() {
        if (connected) return;
        String url = "ws://localhost:55555/client-websocket";
        System.out.println("Connecting to WebSocket at: " + url);

        WebSocketClient webSocketClient = new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))
        );
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

        // Set up converters
        List<MessageConverter> messageConverters = Arrays.asList(new StringMessageConverter(), new MappingJackson2MessageConverter());
        stompClient.setMessageConverter(new CompositeMessageConverter(messageConverters));

        try {
            session = stompClient.connectAsync(url, new StompSessionHandlerAdapter() {
                @Override
                public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                    System.out.println("Connected to WebSocket at: " + url);
                    connected = true;
                    subscribeToTopic(session, "/topic/clients");
                }

                @Override
                public void handleTransportError(StompSession session, Throwable exception) {
                    System.err.println("Transport error: " + exception.getMessage());
                    exception.printStackTrace();
                    connected = false;
                }

                @Override
                public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                    System.err.println("Exception: " + exception.getMessage());
                    exception.printStackTrace();
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    // This method can be used to handle control frames if needed
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToTopic(StompSession session, String topic) {
        session.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                System.out.println("Subscribed to " + topic);
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                String message = (String) payload;
                System.out.println("Received message: " + message);
                notifyListeners(message);
            }
        });
        System.out.println("Subscription request to " + topic + " sent.");
    }

    public void addListener(WebSocketMessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WebSocketMessageListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String message) {
        for (WebSocketMessageListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }
}
