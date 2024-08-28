package com.georgejrdev;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.*;

import static org.mockito.Mockito.*;

import com.georgejrdev.lib.watcher.FileWatcher;
import com.georgejrdev.lib.watcher.FileWatcherCallback;


public class FileWatcherTest {

    @Mock
    private FileWatcherCallback callback;

    private FileWatcher fileWatcher;
    private Path filePath;
    private String fileToWatch = "src/test/resources/testFile.txt";

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        filePath = Paths.get(fileToWatch).toAbsolutePath(); 
        fileWatcher = new FileWatcher(fileToWatch, callback);
    }


    @Test
    public void testFileWatcherTriggersCallbackOnFileModify() throws IOException, InterruptedException {
        Thread watcherThread = new Thread(fileWatcher);
        watcherThread.start();
    
        Thread.sleep(500);
    
        Files.write(filePath, "Test".getBytes(), StandardOpenOption.APPEND);
    
        Thread.sleep(2000);
    
        verify(callback, times(1)).action();
    
        watcherThread.interrupt();
        watcherThread.join();
    }  


    @Test
    public void testFileWatcherDoesNotTriggerCallbackOnDifferentFileModify() throws IOException, InterruptedException {
        new Thread(fileWatcher).start();

        Path anotherFilePath = Paths.get("src/test/resources/anotherFile.txt").toAbsolutePath();
        Files.write(anotherFilePath, "Test".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        Thread.sleep(1000);

        verify(callback, never()).action();
    }
}
