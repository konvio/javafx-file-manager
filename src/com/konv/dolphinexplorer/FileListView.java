package com.konv.dolphinexplorer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.Arrays;

public class FileListView extends ListView<String> {

    private File directory;
    private String path;
    private String home;
    private ObservableList<String> children;

    public FileListView(String path) {
        super();
        this.path = path;
        home = path;
        children = FXCollections.observableArrayList();
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
        setItems(children);
        showList(getCurrentFilesList());
    }

    private String[] getCurrentFilesList() {
        String[] list = (new File(path)).list();
        if (list != null) {
            Arrays.sort(list);
        }
        return list;
    }

    private void showList(String[] list) {
        if (list != null) {
            children.clear();
            children.addAll(list);
        }
    }

    private void gotoDirectory(String directory) {
        path = directory;
        showList(getCurrentFilesList());
    }

    private void navigate(String name) {
        String directoryPath = path + "\\" + name;
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            path = directory.getPath();
            showList(getCurrentFilesList());
        }
    }

    private void back() {
        if (path.lastIndexOf("/") != 0) {
            gotoDirectory(path.substring(0, path.lastIndexOf("\\")));
        } else {
            gotoDirectory(home);
        }
    }
}
