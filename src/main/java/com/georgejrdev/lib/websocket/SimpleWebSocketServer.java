package com.georgejrdev.lib.websocket;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import java.io.IOException;


@ServerEndpoint("/websocket")
public class SimpleWebSocketServer {

    private Server server;
    
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connection opened: " + session.getId());
    }


    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }


    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed: " + session.getId());
    }


    public void start(String host, int port) {
        server = new Server(host, port, "/", null, SimpleWebSocketServer.class);
        try {
            server.start();
            System.out.println("WebSocket Server start on ws://" + host + ":" + port + "/websocket");
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("Stop WebSocket Server.");
        }
    }
}