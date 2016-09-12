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
        primaryStage.setTitle("Dolphin Explorer");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("dolphin.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private MenuBar getMenuBar() {
        Menu fileMenu = new Menu("_File");
        MenuItem exitMenuItem = new MenuItem("E_xit");
        fileMenu.getItems().addAll(exitMenuItem);

        Menu toolsMenu = new Menu("_Tools");
        MenuItem textProcessMenuItem = new MenuItem("Process Text");
        toolsMenu.getItems().addAll(textProcessMenuItem);

        Menu helpMenu = new Menu("_Help");
        MenuItem aboutMenuItem = new MenuItem("About");
        helpMenu.getItems().addAll(aboutMenuItem);

        return new MenuBar(fileMenu, toolsMenu, helpMenu);
    }

    private ToolBar getToolBar() {
        Label labelRefresh = new Label("F2 Refresh");
        Label labelOpen = new Label("F3 Open");
        Label labelNewFile = new Label("F4 NewFile");
        Label labelCopy = new Label("F5 Copy");
        Label labelMove = new Label("F6 Move");
        Label labelNewFolder = new Label("F7 NewFolder");
        Label labelDelete = new Label("F8 Delete");
        Label labelExit = new Label("F9 Exit");
        return new ToolBar(labelRefresh, new Separator(), labelOpen, new Separator(), labelNewFile, new Separator(),
                labelCopy, new Separator(), labelMove, new Separator(), labelNewFolder, new Separator(), labelDelete,
                new Separator(), labelExit);
    }
}
