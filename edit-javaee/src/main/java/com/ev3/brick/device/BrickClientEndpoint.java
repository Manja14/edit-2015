package com.ev3.brick.device;

import java.io.IOException;
import java.net.URI;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class BrickClientEndpoint {

    private static final String WS_HOST_EV3 = "ws://localhost:8081/ev3";

    private Session session;

    @PostConstruct
    public void connectBrickEndpoint() {
        final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

        try {
            webSocketContainer.connectToServer(this, URI.create(WS_HOST_EV3));
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    public void addMessageHandler(MessageHandler.Whole<String> handler) {
        this.session.addMessageHandler(handler);
    }

    public void sendCommand(String command) throws IOException {
        this.session.getBasicRemote().sendText(command);
    }
}
