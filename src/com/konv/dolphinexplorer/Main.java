package com.konv.dolphinexplorer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static final KeyCombination SHORTCUT_COPY = new KeyCodeCombination(KeyCode.F5);
    private static final KeyCombination SHORTCUT_MOVE = new KeyCodeCombination(KeyCode.F6);
    private static final KeyCombination SHORTCUT_DELETE = new KeyCodeCombination(KeyCode.DELETE);
    private static final KeyCombination SHORTCUT_NEW_FILE = new KeyCodeCombination(KeyCode.N,
            KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination SHORTCUT_NEW_DIRECTORY = new KeyCodeCombination(KeyCode.N,
            KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN);
    private static final KeyCombination SHORTCUT_RENAME = new KeyCodeCombination(KeyCode.F6, KeyCombination.SHIFT_DOWN);
    private static final KeyCombination SHORTCUT_FOCUS_TEXT_FIELD = new KeyCodeCombination(KeyCode.D, KeyCombination.SHIFT_DOWN);
    private static final KeyCombination SHORTCUT_HTML_EDITOR = new KeyCodeCombination(KeyCode.F3);

    private FileView mFileView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();

        mFileView = new FileView();

        VBox.setVgrow(mFileView, Priority.ALWAYS);

        root.getChildren().addAll(getMenuBar(), mFileView, getToolBar());

        Scene scene = new Scene(root, 700, 500);

        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (SHORTCUT_DELETE.match(e)) {
                mFileView.delete();
            } else if (SHORTCUT_NEW_FILE.match(e)) {
                mFileView.createFile();
            } else if (SHORTCUT_NEW_DIRECTORY.match(e)) {
                mFileView.createDirectory();
            } else if (SHORTCUT_RENAME.match(e)) {
                mFileView.rename();
            } else if (SHORTCUT_COPY.match(e)) {
                mFileView.copy();
            } else if (SHORTCUT_MOVE.match(e)) {
                mFileView.move();
            } else if (SHORTCUT_FOCUS_TEXT_FIELD.match(e)) {
                mFileView.focusTextField();
            } else if (SHORTCUT_HTML_EDITOR.match(e)) {
                mFileView.openHtml();
            }
        });

        primaryStage.setTitle("Dolphin Explorer");
        primaryStage.setScene(scene);
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
        newFile.setOnAction(e -> mFileView.createFile());
        newFile.setAccelerator(SHORTCUT_NEW_FILE);

        MenuItem newFolder = new MenuItem("New Folder     ");
        newFolder.setOnAction(e -> mFileView.createDirectory());
        newFolder.setAccelerator(SHORTCUT_NEW_DIRECTORY);

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(e -> mFileView.rename());
        renameItem.setAccelerator(SHORTCUT_RENAME);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> mFileView.delete());
        deleteItem.setAccelerator(SHORTCUT_DELETE);

        fileMenu.getItems().addAll(newFile, newFolder, renameItem, deleteItem);

        //Create helpMenu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setOnAction(e -> DialogHelper.showAlert(Alert.AlertType.INFORMATION, "About", null,
                "Dolphin Explorer\n\n" + "Copyright © 2016 by Vitaliy Kononenko\nK-24")
        );
        helpMenu.getItems().addAll(aboutMenuItem);

        return new MenuBar(fileMenu, helpMenu);
    }

    private ToolBar getToolBar() {
        Label labelOpenAsText = new Label("F3 Edit HTML");
        labelOpenAsText.setOnMouseClicked(e -> mFileView.openHtml());

        Label labelCountWords = new Label("Count words");
        labelCountWords.setOnMouseClicked(e -> mFileView.countWords());

        Label labelCopy = new Label("F5 Copy");
        labelCopy.setOnMouseClicked(e -> mFileView.copy());

        Label labelMove = new Label("F6 Move");
        labelMove.setOnMouseClicked(e -> mFileView.move());

        return new ToolBar(labelCountWords, new Separator(),labelOpenAsText, new Separator(), labelCopy, new Separator(), labelMove);
    }
}
