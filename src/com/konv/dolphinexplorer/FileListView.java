package com.konv.dolphinexplorer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.awt.*;
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
        File[] listFiles = mDirectory.listFiles(file -> !file.isHidden());

        if (listFiles == null) {
            listFiles = new File[0];
        }

        Arrays.sort(listFiles, (f1, f2) -> {
            if ((f1.isDirectory() && f2.isDirectory()) || (f1.isFile() && f2.isFile())) {
                return f1.compareTo(f2);
            }
            return f1.isDirectory() ? -1 : 1;
        });

        String[] list = new String[listFiles.length];
        for (int i = 0; i < list.length; ++i) {
            list[i] = listFiles[i].getName();
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
        } else {
            try {
                Desktop.getDesktop().open(directory);
            } catch (Exception e) {

            }
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
