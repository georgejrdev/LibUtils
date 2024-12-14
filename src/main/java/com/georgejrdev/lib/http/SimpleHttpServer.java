package com.georgejrdev.lib.http;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * SimpleHttpServer
 * 
 * <p>This class is used to create a simple http server</p>
 * 
 * @author George Jr
 * @since 1.0.2
 */
public class SimpleHttpServer {
    
    private HttpServer server;

    
    /**
     * Start
     * 
     * <p>This method is used to start the http server</p>
     *
     * @param port port of the server
     * @param filePath path of the file exposed by the server
     * @throws IOException
     */
    public void start(int port, String filePath) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new HttpHandler() {

            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] response = Files.readAllBytes(Path.of(filePath));
                
                exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.length);
            
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Http Server start on http://localhost:" + port);
    }


    /**
     * Stop
     * 
     * <p>This method is used to stop the http server</p>
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}