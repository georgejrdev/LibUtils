package com.georgejrdev.auxiliar.reload;

public class HotReload {

    private final String fileToWatch;
    private final String fileToUpdate;
    private final String content;

    public HotReload(String fileToWatch, String fileToUpdate) {
        this(fileToWatch, fileToUpdate, null);
    }

    public HotReload(String fileToWatch, String fileToUpdate, String content) {
        this.fileToWatch = fileToWatch;
        this.fileToUpdate = fileToUpdate;
        this.content = content;
    }

    
    public void start() {
        SimpleHttpServer httpServer = new SimpleHttpServer(8080, this.fileToUpdate);
        new Thread(() -> {
            try {
                httpServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        SimpleWebSocketServer webSocketServer = new SimpleWebSocketServer(8081);
        webSocketServer.start();

        FileWatcher fileWatcher = new FileWatcher(this.fileToWatch, webSocketServer, this.fileToUpdate, this.content);
        new Thread(fileWatcher).start();
    }
}