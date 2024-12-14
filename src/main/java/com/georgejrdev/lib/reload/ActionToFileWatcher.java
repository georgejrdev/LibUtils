package com.georgejrdev.lib.reload;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.georgejrdev.lib.watcher.FileWatcherCallback;
import com.georgejrdev.lib.websocket.SimpleWebSocketServer;


/**
 * ActionToFileWatcher
 * 
 * <p>This class is used to call a callback when a file is modified</p>
 * 
 * @author George Jr
 * @since 1.1.0
 */
public class ActionToFileWatcher implements FileWatcherCallback {

    private SimpleWebSocketServer webSocketServer;
    private Parser parser;
    private String fileToUpdate;
    private int websocketPort;

    /**
     * Constructor
     *
     * @param webSocketServer implementation of SimpleWebSocketServer
     * @param websocketPort port of the websocket server
     * @param fileToUpdate path of the file to watch
     * @param parser implementation of Parser
     */
    public ActionToFileWatcher(SimpleWebSocketServer webSocketServer, Parser parser, String fileToUpdate, int websocketPort) {
        this.webSocketServer = webSocketServer;
        this.parser = parser;
        this.fileToUpdate  = fileToUpdate;
        this.websocketPort = websocketPort;
    }

    /**
     * Constructor
     *
     * @param webSocketServer implementation of SimpleWebSocketServer
     * @param websocketPort port of the websocket server
     * @param fileToUpdate path of the file to watch
     */
    public ActionToFileWatcher(SimpleWebSocketServer webSocketServer, String fileToUpdate, int websocketPort) {
        this(webSocketServer, null, fileToUpdate, websocketPort);       
    }

    
    /**
     * Action
     * 
     * <p>This method is called when a file is modified</p>
     * <p>This method notifies the websocket server, so it can reload the page</p>
     * <p>This method is synchronized</p>
     */
    @Override
    public void action() {
        synchronized(this) {
            if (parser != null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                parser.parse();
            }
            
            addScriptToInitHotReloadOnHtml();
    
            webSocketServer.notifyClients("reload");
        }
    }
    

    private void addScriptToInitHotReloadOnHtml() {
        Path path = Paths.get(this.fileToUpdate);
        
        String script = "<script> document.addEventListener('DOMContentLoaded', (event) => { var ws = new WebSocket('ws://localhost:"+this.websocketPort+"/reload'); ws.onmessage = function(event) { if (event.data === 'reload') { window.location.reload(); } }; }); </script>";
        
        try {
            if (!Files.exists(path)){
                System.out.println("File not found: " + path);
                return;
            }
    
            String content = new String(Files.readAllBytes(path));
            if (content.contains(script)) {
                return;
            }

            Thread.sleep(100);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileToUpdate, true))) {
                writer.newLine();
                writer.write(script);
            }
    
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }    
    
}