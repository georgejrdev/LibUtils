package com.georgejrdev;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.georgejrdev.lib.http.SimpleHttpServer;


public class SimpleHttpServerTest {

    private SimpleHttpServer httpServer;
    private final int port = 8083;
    private final String filePath = "test.html";

    @Before
    public void setUp() throws IOException {
        Files.writeString(Path.of(filePath), "<html><body>Hello, World!</body></html>");
        httpServer = new SimpleHttpServer();
        httpServer.start(port, filePath);
    }

    @After
    public void tearDown() {
        httpServer.stop();
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testServerIsRunning() throws IOException {
        URL url = new URL("http://localhost:" + port + "/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(200, responseCode);
    }


    @Test
    public void testServerResponseContent() throws IOException {
        URL url = new URL("http://localhost:" + port + "/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        String content = new String(connection.getInputStream().readAllBytes());

        assertEquals(200, responseCode);
        assertTrue(content.contains("Hello, World!"));
    }

    
    @Test
    public void testServerStop() {
        httpServer.stop();

        try {
            URL url = new URL("http://localhost:" + port + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Connection refused"));
        }
    }
}