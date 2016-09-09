package com.konv.dolphinexplorer;

import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FilePane extends VBox {

    private ListView<String> listView;

    public FilePane() {
        super();
        listView = new ListView<>();
        getChildren().add(listView);
        setVgrow(listView, Priority.ALWAYS);
    }
}
