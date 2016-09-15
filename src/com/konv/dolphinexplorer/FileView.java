package com.konv.dolphinexplorer;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Path;

public class FileView extends HBox {

    private ListView mLeftPane;
    private ListView mRightPane;

    public FileView() {
        File[] roots = File.listRoots();
        String leftPanePath = roots[0].getPath();
        String rightPanePath = roots.length > 1 ? roots[1].getPath() : leftPanePath;

        mLeftPane = new ListView(leftPanePath);
        mRightPane = new ListView(rightPanePath);

        VBox leftView = new VBox(mLeftPane.getTextField(), mLeftPane);
        VBox rightView = new VBox(mRightPane.getTextField(), mRightPane);

        VBox.setVgrow(mLeftPane, Priority.ALWAYS);
        VBox.setVgrow(mRightPane, Priority.ALWAYS);
        HBox.setHgrow(leftView, Priority.ALWAYS);
        HBox.setHgrow(rightView, Priority.ALWAYS);

        getChildren().addAll(leftView, rightView);
    }

    public void copy() {
        if (mLeftPane.isFocused()) {
            Path source = mLeftPane.getSelection();
            Path target = mRightPane.getDirectory();
            FileHelper.copy(source, target);
        } else if (mRightPane.isFocused()) {
            Path source = mRightPane.getSelection();
            Path target = mLeftPane.getDirectory();
            FileHelper.copy(source, target);
        }
    }

    public void move() {
        if (mLeftPane.isFocused()) {
            Path source = mLeftPane.getSelection();
            Path target = mRightPane.getDirectory();
            FileHelper.move(source, target);
        } else if (mRightPane.isFocused()) {
            Path source = mRightPane.getSelection();
            Path target = mLeftPane.getDirectory();
            FileHelper.move(source, target);
        }
    }

    public void delete() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane != null) FileHelper.delete(focusedPane.getSelection());
    }

    public void rename() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane != null) FileHelper.rename(focusedPane.getSelection());
    }

    public void createDirectory() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane != null) FileHelper.createDirectory(focusedPane.getDirectory());
    }

    public void createFile() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane != null) FileHelper.createFile(focusedPane.getDirectory());
    }

    private ListView getFocusedPane() {
        if (mLeftPane.isFocused()) {
            return mLeftPane;
        } else if (mRightPane.isFocused()) {
            return mRightPane;
        } else {
            return null;
        }
    }
}
