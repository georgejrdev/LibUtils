package com.georgejrdev.lib.watcher;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicLong;


/**
 * FileWatcher
 * 
 * <p>This class is used to watch a file and call a callback when it is modified</p>
 * 
 * @author George Jr
 * @since 1.0.2
 */
public class FileWatcher implements Runnable {

    private final FileWatcherCallback callback;
    private final String fileToWatch;
    private volatile boolean running;
    private final AtomicLong lastEventTime; 

    private static final long DEBOUNCE_TIME_MS = 500; 

    /**
     * Constructor
     * 
     * @param fileToWatch path of the file to watch
     * @param callback callback to be called when the file is modified
     */
    public FileWatcher(String fileToWatch, FileWatcherCallback callback) {
        this.fileToWatch = fileToWatch;
        this.callback = callback;
        this.running = true;
        this.lastEventTime = new AtomicLong(0);
    }


    /**
     * Run
     * 
     * <p>This method is used to start the file watcher</p>
     */
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


    /**
     * Stop
     * 
     * <p>This method is used to stop the file watcher</p>
     */
    public void stop() {
        this.running = false;
    }
}