package com.georgejrdev.lib.reload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import com.georgejrdev.lib.http.SimpleHttpServer;
import com.georgejrdev.lib.watcher.FileWatcher;
import com.georgejrdev.lib.websocket.SimpleWebSocketServer;


public class HotReload {
    
    private SimpleWebSocketServer webSocketServer;
    private SimpleHttpServer httpServer;
    private ActionToFileWatcher actionToFileWatcher;
    private Parser parser;
    private FileWatcher fileWatcher;
    
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
        this.actionToFileWatcher = new ActionToFileWatcher(this.webSocketServer, this.parser, this.fileToUpdate, this.websocketPort);
        this.fileWatcher = new FileWatcher(this.fileToWatch, this.actionToFileWatcher);
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
        
        this.webSocketServer.start();

        new Thread(this.fileWatcher).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fileWatcher.stop();

            try {
                this.webSocketServer.stop();
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            this.httpServer.stop();
            
            removeScriptToInitHotReloadOnHtml();

            System.out.println("");
            System.out.println("---> Server is shutting down...");
            System.out.println("");
        }));
    }


    private void removeScriptToInitHotReloadOnHtml(){
        Path path = Paths.get(this.fileToUpdate);

        try {
            if (!Files.exists(path)) {
                System.out.println("File not found: " + this.fileToUpdate);
                return;
            }

            String content = new String(Files.readAllBytes(path));
            String modifiedContent = content.replaceAll("(?s)<script.*?>.*?</script>", "");

            Files.write(path, modifiedContent.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}