package com.georgejrdev;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import com.georgejrdev.lib.websocket.SimpleWebSocketServer;


@ClientEndpoint
public class SimpleWebSocketServerTest {

    private SimpleWebSocketServer webSocketServer;
    private Session session;
    private final CountDownLatch latch = new CountDownLatch(1);
    private String receivedMessage;
    private final int PORT = 8085;

    @Before
    public void setUp() {
        webSocketServer = new SimpleWebSocketServer();
        webSocketServer.start("localhost", this.PORT);
    }

    @After
    public void tearDown() {
        webSocketServer.stop();
    }


    @Test
    public void testWebSocketServerIsRunning() throws Exception {
        webSocketServer.stop(); 
        webSocketServer.start("localhost", PORT);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, new URI("ws://localhost:" + this.PORT + "/websocket"));
        session.getBasicRemote().sendText("Hello, Server!");
        
        boolean messageReceived = latch.await(5, TimeUnit.SECONDS);

        if (messageReceived) {
            assertTrue(receivedMessage.contains("Hello, Server!"));
        } else {
            fail("No message received within the timeout period.");
        }
    }


    @Test
    public void testWebSocketServerStop() throws Exception {
        webSocketServer.stop();

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI("ws://localhost:"+this.PORT+"/websocket"));
        } catch (DeploymentException | IOException e) {
            assertTrue(e.getMessage().contains("failed"));
        }
    }
    

    @OnMessage
    public void onMessage(String message) {
        receivedMessage = message;
        latch.countDown();
    }
}
