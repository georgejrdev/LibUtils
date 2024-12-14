package com.georgejrdev.lib.websocket;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * SimpleWebSocketServer
 * 
 * <p>This class is used to create a simple websocket server</p>
 * 
 * @author George Jr
 * @since 1.0.2
 */
public class SimpleWebSocketServer extends WebSocketServer {

    private Set<WebSocket> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Constructor
     * 
     * @param port port of the server
     */
    public SimpleWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }


    /**
     * onOpen
     * 
     * <p>This method is called when a new connection is opened</p>
     * 
     * @param conn connection to the client
     * @param handshake handshake of the client
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }


    /**
     * onClose
     * 
     * <p>This method is called when a connection is closed</p>
     * 
     * @param conn connection to the client
     * @param code code of the connection
     * @param reason reason of the connection
     * @param remote if the connection is remote
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }


    /**
     * onMessage
     * 
     * <p>This method is called when a message is received from the client</p>
     * 
     * @param conn connection to the client
     * @param message message received from the client
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);
    }


    /**
     * onError
     * 
     * <p>This method is called when an error occurs</p>
     * 
     * @param conn connection to the client
     * @param ex exception thrown
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }


    /**
     * onStart
     * 
     * <p>This method is called when the server starts</p>
     */
    @Override
    public void onStart() {
        System.out.println("WebSocket server started successfully!");
    }
    

    /**
     * notifyClients
     * 
     * <p>This method is used to send a message to all clients</p>
     * 
     * @param message message to send
     */
    public void notifyClients(String message) {
        for (WebSocket client : clients) {
            client.send(message);
        }
    }
}