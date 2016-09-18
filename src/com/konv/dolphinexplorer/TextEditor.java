package com.konv.dolphinexplorer;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class TextEditor extends HTMLEditor {

    private Stage mStage;
    private File mFile;

    public TextEditor() {

        VBox root = new VBox();

        Menu fileMenu = new Menu("File");
        MenuItem saveHtmlMenuItem = new MenuItem("Save as HTML");
        saveHtmlMenuItem.setOnAction(e -> saveHtml());
        MenuItem closeMenuItem = new MenuItem("Close");
        closeMenuItem.setOnAction(e -> close());
        fileMenu.getItems().addAll(saveHtmlMenuItem, closeMenuItem);

        root.getChildren().addAll(new MenuBar(fileMenu), this);

        mStage = new Stage();
        Scene scene = new Scene(root);
        mStage.setScene(scene);
        mStage.getIcons().add(new Image(Main.class.getResourceAsStream("dolphin.png")));
    }

    public void open(File file) {
        if (file.toString().endsWith(".html")) {
            try {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                mFile = file;
                setHtmlText(content);
                mStage.setTitle(file.getPath());
                mStage.show();
            } catch (IOException e) {
                mFile = null;
                mStage.close();
            }
        }
    }

    public void saveHtml() {
        if (mFile != null) {
            try (PrintWriter printWriter = new PrintWriter(mFile)) {
                printWriter.write(getHtmlText());
            } catch (Exception e) {
                mFile = null;
            }
        }
    }

    public void close() {
        boolean save = DialogHelper.showConfirmationDialog(mFile.getName(), null, "Save changes?");
        if (save) saveHtml();
        mStage.hide();
    }
}
