package com.georgejrdev.lib.watcher;

/**
 * FileWatcherCallback
 * 
 * <p>This interface is used to call a callback when a file is modified</p>
 * 
 * @author George Jr
 * @since 1.1.0
 */
public interface FileWatcherCallback {
    /**
     * Action
     * 
     * <p>This method is called when a file is modified</p>
     */
    void action();
}
