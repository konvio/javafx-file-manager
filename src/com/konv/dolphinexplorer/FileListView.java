package com.konv.dolphinexplorer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.Arrays;

public class FileListView extends ListView<String> {

    private File mDirectory;
    private ObservableList<String> mChildrenList;

    public FileListView(String path) {
        super();
        mDirectory = new File(path);
        mChildrenList = FXCollections.observableArrayList();
        setOnKeyPressed((key) -> {
            switch (key.getCode()) {
                case ENTER:
                    navigate(getSelectionModel().getSelectedItem());
                    break;
                case BACK_SPACE:
                    back();
                    break;
            }
        });
        setItems(mChildrenList);
        showList(getCurrentFilesList());
    }

    private String[] getCurrentFilesList() {
        String[] list = mDirectory.list();
        if (list != null) {
            Arrays.sort(list);
        }
        return list;
    }

    private void showList(String[] list) {
        if (list != null) {
            mChildrenList.clear();
            mChildrenList.addAll(list);
        } else {
            mChildrenList.clear();
        }
    }

    private void navigate(String name) {
        String directoryPath = mDirectory.getAbsolutePath() + File.separator + name;
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            mDirectory = directory;
            showList(getCurrentFilesList());
        }
    }

    private void back() {
        File parent = mDirectory.getParentFile();
        if (parent != null) {
            mDirectory = parent;
            showList(getCurrentFilesList());
        }
    }
}
