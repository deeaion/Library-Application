package client.RestCommunication;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketManager {

    private static WebSocketManager instance;
    private StompSession session;
    private final CopyOnWriteArrayList<WebSocketMessageListener> listeners = new CopyOnWriteArrayList<>();
    private boolean connected = false;

    private WebSocketManager() {
    }

    public static WebSocketManager getInstance() {
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
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        try {
            session = stompClient.connectAsync(url, new StompSessionHandlerAdapter() {
                @Override
                public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                    System.out.println("Connected to WebSocket at: " + url);
                    connected = true;
                    session.subscribe("/topic/clients", new StompFrameHandler() {
                        @Override
                        public Type getPayloadType(StompHeaders headers) {
                            System.out.println("Subscribed to /topic/clients");
                            return String.class;
                        }

                        @Override
                        public void handleFrame(StompHeaders headers, Object payload) {
                            String message = (String) payload;
                            System.out.println("Received message: " + message);
                            notifyListeners(message);
                        }
                    });
                }

                @Override
                public void handleTransportError(StompSession session, Throwable exception) {
                    System.err.println("Transport error: " + exception.getMessage());
                    exception.printStackTrace();
                }

                @Override
                public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                    System.err.println("Exception: " + exception.getMessage());
                    exception.printStackTrace();
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
