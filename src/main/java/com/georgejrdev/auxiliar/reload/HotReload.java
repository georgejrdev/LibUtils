package com.georgejrdev.auxiliar.reload;

public class HotReload {

    private final String fileToWatch;
    private final String fileToUpdate;
    private final Parser parser;
    private final int httpPort;
    private final int webSocketPort;

    public HotReload(String fileToWatch, String fileToUpdate, int httpPort, int webSocketPort) {
        this.fileToWatch = fileToWatch;
        this.fileToUpdate = fileToUpdate;
        this.httpPort = httpPort;
        this.webSocketPort = webSocketPort;
        this.parser = null;
    }

    public HotReload(String fileToWatch, String fileToUpdate, int httpPort, int webSocketPort, Parser parser) {
        this.fileToWatch = fileToWatch;
        this.fileToUpdate = fileToUpdate;
        this.httpPort = httpPort;
        this.webSocketPort = webSocketPort;
        this.parser = parser;
    }

    
    public void start() {
        SimpleHttpServer httpServer = new SimpleHttpServer(this.httpPort, this.fileToUpdate);
        new Thread(() -> {
            try {
                httpServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        SimpleWebSocketServer webSocketServer = new SimpleWebSocketServer(this.webSocketPort);
        webSocketServer.start();

        FileWatcher fileWatcher = new FileWatcher(this.fileToWatch, webSocketServer, this.fileToUpdate, this.parser);
        new Thread(fileWatcher).start();
    }
}