package com.konv.dolphinexplorer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class ListView extends javafx.scene.control.ListView<String> {

    private File mDirectory;
    private TextField mTextField;
    private ObservableList<String> mChildrenList;

    private WatchServiceHelper mWatchServiceHelper;

    public ListView(String path) {
        super();
        mDirectory = new File(path);

        mChildrenList = FXCollections.observableArrayList();
        setItems(mChildrenList);

        mTextField = new TextField();
        mTextField.setStyle("-fx-font-size: 10px;");
        mTextField.setOnAction(e -> goToFile(mTextField.getText()));

        setOnKeyPressed(key -> {
            switch (key.getCode()) {
                case ENTER:
                    if (isFocused()) {
                        navigate(getSelectionModel().getSelectedItem());
                    }
                    break;
                case BACK_SPACE:
                    back();
                    break;
            }
        });
        mWatchServiceHelper = new WatchServiceHelper(this);
        refresh();
    }

    public void refresh() {
        showList(getCurrentFilesList());
        mTextField.setText(mDirectory.getAbsolutePath());
        mWatchServiceHelper.changeObservableDirectory(mDirectory.toPath());
    }

    public TextField getTextField() {
        return mTextField;
    }

    public Path getSelection() {
        String name = getSelectionModel().getSelectedItem();
        if (name != null) {
            return mDirectory.toPath().resolve(name);
        } else {
            return null;
        }
    }

    public Path getDirectory() {
        return mDirectory.toPath();
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

    private void goToFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            refresh();
            return;
        }
        if (file.isDirectory()) {
            mDirectory = file;
            refresh();
        } else if (file.isFile()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                DialogHelper.showException(e);
            }
        }
    }

    private void navigate(String name) {
        String selectedPath = mDirectory.getAbsolutePath() + File.separator + name;
        File selectedFile = new File(selectedPath);
        if (selectedFile.isDirectory()) {
            mDirectory = selectedFile;
            refresh();
        } else {
            try {
                Desktop.getDesktop().open(selectedFile);
            } catch (Exception e) {
                DialogHelper.showException(e);
            }
        }
    }

    private void back() {
        File parent = mDirectory.getParentFile();
        if (parent != null) {
            mDirectory = parent;
            if (mDirectory.exists()) {
                refresh();
            } else {
                back();
            }
        }
    }
}
