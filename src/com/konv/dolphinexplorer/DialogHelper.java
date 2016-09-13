package com.konv.dolphinexplorer;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType;

public class DialogHelper {

    public static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    @Nullable
    public static String showTextInputDialog(String title, String header, String content, String hint) {
        TextInputDialog dialog = new TextInputDialog(hint);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.isPresent() ? result.get() : null;
    }

    public static void showException(Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(e.toString());

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String exceptionText = printWriter.toString();

        Label label = new Label("Exception stacktrace:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expandableContent = new GridPane();
        expandableContent.setMaxWidth(Double.MAX_VALUE);
        expandableContent.add(label, 0, 0);
        expandableContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expandableContent);

        alert.showAndWait();
    }
}
