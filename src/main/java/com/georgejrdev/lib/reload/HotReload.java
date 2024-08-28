package com.georgejrdev.lib.reload;

import java.io.IOException;

import com.georgejrdev.lib.http.SimpleHttpServer;
import com.georgejrdev.lib.watcher.FileWatcher;
import com.georgejrdev.lib.websocket.SimpleWebSocketServer;


public class HotReload {
    
    private SimpleWebSocketServer webSocketServer;
    private SimpleHttpServer httpServer;
    private ActionToFileWatcher actionToFileWatcher;
    private Parser parser;
    
    private String fileToWatch;
    private String fileToUpdate;
    private int httpPort;
    private int websocketPort;

    public HotReload(String fileToWatch, String fileToUpdate, int httpPort, int websocketPort, Parser parser){
        this.fileToWatch = fileToWatch;
        this.fileToUpdate = fileToUpdate;
        this.httpPort = httpPort;
        this.websocketPort = websocketPort;
        
        this.parser = parser;
        this.httpServer = new SimpleHttpServer();
        this.webSocketServer = new SimpleWebSocketServer(this.websocketPort);
        this.actionToFileWatcher = new ActionToFileWatcher(this.webSocketServer, this.parser);
    }

    public HotReload(String fileToWatch, String fileToUpdate, int httpPort, int websocketPort){
        this(fileToWatch, fileToUpdate, httpPort, websocketPort, null);
    }


    public void start(){
        new Thread(()->{
            try {
                this.httpServer.start(this.httpPort, this.fileToUpdate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        webSocketServer.start();

        FileWatcher fileWatcher = new FileWatcher(this.fileToWatch, this.actionToFileWatcher);
        new Thread(fileWatcher).start();
    }
}
