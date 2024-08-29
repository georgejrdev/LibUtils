package com.georgejrdev.lib.reload;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.georgejrdev.lib.watcher.FileWatcherCallback;
import com.georgejrdev.lib.websocket.SimpleWebSocketServer;


public class ActionToFileWatcher implements FileWatcherCallback {

    private SimpleWebSocketServer webSocketServer;
    private Parser parser;
    private String fileToUpdate;
    private int websocketPort;

    public ActionToFileWatcher(SimpleWebSocketServer webSocketServer, Parser parser, String fileToUpdate, int websocketPort) {
        this.webSocketServer = webSocketServer;
        this.parser = parser;
        this.fileToUpdate  = fileToUpdate;
        this.websocketPort = websocketPort;
    }

    public ActionToFileWatcher(SimpleWebSocketServer webSocketServer, String fileToUpdate, int websocketPort) {
        this(webSocketServer, null, fileToUpdate, websocketPort);       
    }

    
    @Override
    public void action(){

        if (parser != null) {
            parser.parse();
            addScriptToInitHotReloadOnHtml();
        }

        webSocketServer.notifyClients("reload");
    }    


    private void addScriptToInitHotReloadOnHtml(){
        System.out.println("opps");
        Path path = Paths.get(this.fileToUpdate);

        String script = "<script> document.addEventListener('DOMContentLoaded', (event) => { var ws = new WebSocket('ws://localhost:"+this.websocketPort+"/reload'); ws.onmessage = function(event) { if (event.data === 'reload') { window.location.reload(); } }; }); </script>";

        try {
            if (!Files.exists(path)){
                System.out.println("File not found: " + path);
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileToUpdate, true))) {
                writer.newLine();
                writer.write(script);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}