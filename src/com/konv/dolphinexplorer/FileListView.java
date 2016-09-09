package com.konv.dolphinexplorer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.Arrays;

public class FileListView extends ListView<String> {

    private String path;
    private ObservableList<String> children;

    public FileListView(String path) {
        super();
        this.path = path;
        children = FXCollections.observableArrayList();
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
}
