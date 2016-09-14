package com.konv.dolphinexplorer;

import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.*;

public class WatchServiceHelper {

    private WatchService mWatchService;
    private WatchKey mWatchKey;
    private volatile Thread mWatchThread;

    private ListView mListView;
    private Path mCurrentDirectory;

    public WatchServiceHelper(ListView listView) {
        mListView = listView;
        try {
            mWatchService = FileSystems.getDefault().newWatchService();
            mWatchKey = mListView.getDirectory().register(mWatchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            mCurrentDirectory = mListView.getDirectory();
        } catch (IOException e) {
            DialogHelper.showException(e);
        }
        mWatchThread = new Thread(() -> {
            while (true) {
                try {
                    WatchKey watchKey = mWatchService.take();
                    watchKey.pollEvents();
                    updateUI();
                    watchKey.reset();
                } catch (InterruptedException e) {
                    DialogHelper.showException(e);
                }
            }
        });
        mWatchThread.start();
    }

    public void changeObservableDirectory(Path newDirectory) {
        if (mCurrentDirectory.equals(newDirectory)) return;
        mWatchKey.cancel();
        try {
            mWatchKey = newDirectory.register(mWatchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            mCurrentDirectory = newDirectory;
        } catch (IOException e) {
            DialogHelper.showException(e);
        }
    }

    private void updateUI() {
        Platform.runLater(() -> mListView.refresh());
    }
}
