package com.georgejrdev.lib.watcher;

import java.io.IOException;
import java.nio.file.*;


public class FileWatcher implements Runnable {
        
    private FileWatcherCallback callback;
    private String fileToWatch;
    private boolean running;

    public FileWatcher(String fileToWatch, FileWatcherCallback callback) {
        this.fileToWatch = fileToWatch;
        this.callback = callback;
        this.running = true;
    }


    @Override
    public void run(){
        try{
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(fileToWatch).getParent();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while (running){
                WatchKey key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents()){
                    Path changedFile = path.resolve((Path) event.context());

                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && changedFile.toString().equals(fileToWatch)){
                        System.out.println("File Modified: " + changedFile);
                        callback.action();
                    }
                }

                key.reset();
            }
        }

        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }


    public void stop(){
        this.running = false;
    }
}