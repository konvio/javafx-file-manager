package com.konv.dolphinexplorer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private FileListView leftPane;
    private FileListView rightPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();

        leftPane = new FileListView("C://");
        rightPane = new FileListView("D://");
        HBox hBox = new HBox(leftPane, rightPane);

        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        VBox.setVgrow(hBox, Priority.ALWAYS);

        root.getChildren().addAll(getMenuBar(), hBox);
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
}
