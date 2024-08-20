package com.georgejrdev.auxiliar.reload;

import java.io.IOException;
import java.nio.file.*;

import com.georgejrdev.auxiliar.file.ManipulateFile;

public class FileWatcher implements Runnable {

    private final String fileToWatch;
    private final SimpleWebSocketServer webSocketServer;
    private final String fileToUpdate;
    private final String content;
    private final ManipulateFile manipulateFile;

    public FileWatcher(String fileToWatch, SimpleWebSocketServer webSocketServer) {
        this(fileToWatch, webSocketServer, null, null);
    }

    public FileWatcher(String fileToWatch, SimpleWebSocketServer webSocketServer, String fileToUpdate, String content) {
        this.fileToWatch = fileToWatch;
        this.webSocketServer = webSocketServer;
        this.fileToUpdate = fileToUpdate;
        this.content = content;
        this.manipulateFile = new ManipulateFile(); 
    }
    

    @Override
    public void run() {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(fileToWatch).getParent();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changedFile = path.resolve((Path) event.context());

                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && changedFile.toString().equals(fileToWatch)) {
                        System.out.println("File Modified: " + changedFile);

                        if (fileToUpdate != null && content != null) {
                            manipulateFile.writeFile(fileToUpdate, content);
                        }

                        webSocketServer.notifyClients("reload");
                    }
                }

                key.reset();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}