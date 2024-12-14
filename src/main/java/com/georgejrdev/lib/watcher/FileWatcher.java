package com.georgejrdev.lib.watcher;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicLong;

public class FileWatcher implements Runnable {

    private final FileWatcherCallback callback;
    private final String fileToWatch;
    private volatile boolean running;
    private final AtomicLong lastEventTime; 

    private static final long DEBOUNCE_TIME_MS = 500; 

    public FileWatcher(String fileToWatch, FileWatcherCallback callback) {
        this.fileToWatch = fileToWatch;
        this.callback = callback;
        this.running = true;
        this.lastEventTime = new AtomicLong(0);
    }

    @Override
    public void run() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(fileToWatch).getParent();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while (running) {
                WatchKey key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path changedFile = path.resolve((Path) event.context());
                        if (changedFile.toString().equals(fileToWatch)) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastEventTime.get() > DEBOUNCE_TIME_MS) {
                                lastEventTime.set(currentTime);
                                System.out.println("File Modified: " + changedFile);
                                callback.action();
                            }
                        }
                    }
                }

                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.running = false;
    }
}