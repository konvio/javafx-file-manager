package com.konv.dolphinexplorer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class Main extends Application {

    private FileListView leftPane;
    private FileListView rightPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();

        File[] roots = File.listRoots();
        String leftPanePath = roots[0].getPath();
        String rightPanePath = roots.length > 1 ? roots[1].getPath() : leftPanePath;

        leftPane = new FileListView(leftPanePath);
        rightPane = new FileListView(rightPanePath);

        VBox leftPaneText = new VBox(leftPane.getTextField(), leftPane);
        VBox rightPaneText = new VBox(rightPane.getTextField(), rightPane);

        VBox.setVgrow(leftPane, Priority.ALWAYS);
        VBox.setVgrow(rightPane, Priority.ALWAYS);

        HBox fileManagerHBox = new HBox(leftPaneText, rightPaneText);
        HBox.setHgrow(leftPaneText, Priority.ALWAYS);
        HBox.setHgrow(rightPaneText, Priority.ALWAYS);
        VBox.setVgrow(fileManagerHBox, Priority.ALWAYS);

        root.getChildren().addAll(getMenuBar(), fileManagerHBox, getToolBar());
        root.setOnKeyPressed(key -> {
            switch (key.getCode()) {
                case F5:
                    copy();
                    break;
                case F6:
                    move();
                    break;
            }
        });
        primaryStage.setTitle("Dolphin Explorer");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("dolphin.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private MenuBar getMenuBar() {
        Menu fileMenu = new Menu("File");

        // Create file menu
        MenuItem newFile = new MenuItem("New File");
        newFile.setOnAction(e -> {
            FileListView focusedPane = getFocusedPane();
            if (focusedPane == null) return;
            focusedPane.createFile();
        });
        newFile.setAccelerator(FileListView.SHORTCUT_NEW_FILE);

        MenuItem newFolder = new MenuItem("New Folder     ");
        newFolder.setOnAction(e -> {
            FileListView focusedPane = getFocusedPane();
            if (focusedPane == null) return;
            focusedPane.createDirectory();
        });
        newFolder.setAccelerator(FileListView.SHORTCUT_NEW_DIRECTORY);

        MenuItem refreshItem = new MenuItem("Refresh");
        refreshItem.setOnAction(e -> {
            leftPane.refresh();
            rightPane.refresh();
        });
        refreshItem.setAccelerator(FileListView.SHORTCUT_REFRESH);

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(e -> {
            FileListView focusedPane = getFocusedPane();
            if (focusedPane != null) {
                focusedPane.rename();
            }
        });
        renameItem.setAccelerator(FileListView.SHORTCUT_RENAME);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            FileListView focusedPane = getFocusedPane();
            if (focusedPane != null) {
                focusedPane.delete();
            }
        });
        deleteItem.setAccelerator(FileListView.SHORTCUT_DELETE);

        fileMenu.getItems().addAll(newFile, newFolder, refreshItem, renameItem, deleteItem);

        //Create helpMenu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setOnAction(e -> {
            DialogHelper.showAlert(Alert.AlertType.INFORMATION, "About", null,
                    "Dolphin Explorer\n\n" + "Copyright © 2016 by Vitaliy Kononenko\nK-24");
        });
        helpMenu.getItems().addAll(aboutMenuItem);

        return new MenuBar(fileMenu, helpMenu);
    }

    private ToolBar getToolBar() {
        Label labelCopy = new Label("F5 Copy");
        labelCopy.setOnMouseClicked(e -> copy());

        Label labelMove = new Label("F6 Move");
        labelMove.setOnMouseClicked(e -> move());

        return new ToolBar(labelCopy, new Separator(), labelMove);
    }

    private void copy() {
        if (leftPane.isFocused()) {
            Path source = leftPane.getSelectedFilePath();
            Path target = rightPane.getDirectoryPath();
            FileSystemHelper.copy(source, target);
            rightPane.refresh();
        } else if (rightPane.isFocused()) {
            Path source = rightPane.getSelectedFilePath();
            Path target = leftPane.getDirectoryPath();
            FileSystemHelper.copy(source, target);
            leftPane.refresh();
        }
    }

    private void move() {
        if (leftPane.isFocused()) {
            Path source = leftPane.getSelectedFilePath();
            Path target = rightPane.getDirectoryPath();
            FileSystemHelper.move(source, target);
            leftPane.refresh();
            rightPane.refresh();
        } else if (rightPane.isFocused()) {
            Path source = rightPane.getSelectedFilePath();
            Path target = leftPane.getDirectoryPath();
            FileSystemHelper.move(source, target);
            leftPane.refresh();
            rightPane.refresh();
        }
    }

    private FileListView getFocusedPane() {
        if (leftPane.isFocused()) {
            return leftPane;
        } else if (rightPane.isFocused()) {
            return rightPane;
        } else {
            return null;
        }
    }
}
