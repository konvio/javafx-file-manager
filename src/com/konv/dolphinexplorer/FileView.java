package com.konv.dolphinexplorer;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class FileView extends HBox {

    private static final String ACTION_SELECT = "select";
    private static final String ACTION_COPY = "copy";
    private static final String ACTION_MOVE = "move";
    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_OPEN = "open";

    private ListView mLeftPane;
    private ListView mRightPane;

    private TextEditor mTextEditor;

    public FileView() {
        File[] roots = File.listRoots();
        String leftPanePath = roots[0].getPath();
        String rightPanePath = roots.length > 1 ? roots[1].getPath() : leftPanePath;

        mLeftPane = new ListView(leftPanePath);
        mRightPane = new ListView(rightPanePath);
        mTextEditor = new TextEditor();

        mLeftPane.getTextField().setOnAction(e -> onTextEntered(mLeftPane.getTextField()));
        mRightPane.getTextField().setOnAction(e -> onTextEntered(mRightPane.getTextField()));

        VBox leftView = new VBox(mLeftPane.getTextField(), mLeftPane);
        VBox rightView = new VBox(mRightPane.getTextField(), mRightPane);
        mLeftPane.setFocusTraversable(true);

        VBox.setVgrow(mLeftPane, Priority.ALWAYS);
        VBox.setVgrow(mRightPane, Priority.ALWAYS);
        HBox.setHgrow(leftView, Priority.ALWAYS);
        HBox.setHgrow(rightView, Priority.ALWAYS);

        getChildren().addAll(leftView, rightView);
    }

    public void copy() {
        if (mLeftPane.isFocused()) {
            List<Path> source = mLeftPane.getSelection();
            Path target = mRightPane.getDirectory();
            FileHelper.copy(source, target);
        } else if (mRightPane.isFocused()) {
            List<Path> source = mRightPane.getSelection();
            Path target = mLeftPane.getDirectory();
            FileHelper.copy(source, target);
        }
    }

    public void move() {
        if (mLeftPane.isFocused()) {
            List<Path> source = mLeftPane.getSelection();
            Path target = mRightPane.getDirectory();
            FileHelper.move(source, target);
        } else if (mRightPane.isFocused()) {
            List<Path> source = mRightPane.getSelection();
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
        if (focusedPane != null) {
            List<Path> selection = focusedPane.getSelection();
            if (selection.size() == 1) FileHelper.rename(selection.get(0));
        }
    }

    public void createDirectory() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane != null) FileHelper.createDirectory(focusedPane.getDirectory());
    }

    public void createFile() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane != null) FileHelper.createFile(focusedPane.getDirectory());
    }

    public void focusTextField() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane != null) focusedPane.getTextField().requestFocus();
    }

    public void openHtml() {
        ListView focusedPane = getFocusedPane();
        if (focusedPane == null) return;
        List<Path> selection = focusedPane.getSelection();
        if (selection.size() != 1) return;
        File file = selection.get(0).toFile();
        mTextEditor.open(file);
    }

    private ListView getFocusedPane() {
        if (mLeftPane.isFocused() || mLeftPane.getTextField().isFocused()) {
            return mLeftPane;
        } else if (mRightPane.isFocused() || mRightPane.getTextField().isFocused()) {
            return mRightPane;
        } else {
            return null;
        }
    }

    private ListView getFocusedPane(TextField textField) {
        if (textField == mLeftPane.getTextField()) {
            return mLeftPane;
        } else {
            return mRightPane;
        }
    }

    private void onTextEntered(TextField textField) {
        ListView focusedPane = getFocusedPane(textField);
        String command = textField.getText().trim();
        File file = new File(command);
        if (file.exists()) {
            focusedPane.openFile(file);
        } else if (command.startsWith(ACTION_SELECT)) {
            String regex = command.substring(ACTION_SELECT.length()).trim();
            focusedPane.select(regex);
        } else if (command.startsWith(ACTION_COPY)) {
            String regex = command.substring(ACTION_COPY.length()).trim();
            focusedPane.select(regex);
            copy();
        } else if (command.startsWith(ACTION_MOVE)) {
            String regex = command.substring(ACTION_MOVE.length()).trim();
            focusedPane.select(regex);
            move();
        } else if (command.startsWith(ACTION_DELETE)) {
            String regex = command.substring(ACTION_DELETE.length()).trim();
            focusedPane.select(regex);
            delete();
        }
        textField.setText(focusedPane.getDirectory().toString());
        focusedPane.requestFocus();
    }
}
