package com.georgejrdev.lib.reload;

import com.georgejrdev.lib.watcher.FileWatcherCallback;
import com.georgejrdev.lib.websocket.SimpleWebSocketServer;


public class ActionToFileWatcher implements FileWatcherCallback {

    private SimpleWebSocketServer webSocketServer;
    private Parser parser;

    public ActionToFileWatcher(SimpleWebSocketServer webSocketServer, Parser parser) {
        this.webSocketServer = webSocketServer;
        this.parser = parser;
    }

    public ActionToFileWatcher(SimpleWebSocketServer webSocketServer) {
        this(webSocketServer, null);       
    }

    
    @Override
    public void action(){

        if (parser != null) {
            parser.parse();
        }

        webSocketServer.notifyClients("reload");
    }    
}