package com.georgejrdev.lib.websocket;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class SimpleWebSocketServer extends WebSocketServer {

    private Set<WebSocket> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public SimpleWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }


    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }


    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }


    @Override
    public void onStart() {
        System.out.println("WebSocket server started successfully!");
    }
    

    public void notifyClients(String message) {
        for (WebSocket client : clients) {
            client.send(message);
        }
    }
}