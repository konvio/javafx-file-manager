package com.konv.dolphinexplorer.spreadsheet;

import com.konv.dolphinexplorer.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.*;

public class TableStage extends Stage {

    private SpreadsheetView mSpreadsheet;
    private SpreadsheetController mSpreadsheetController;
    private GridBase mGridBase;
    private int mRowCount = 99;
    private int mColumnCount = 26;

    public TableStage() {
        initUi();
    }

    private void initUi() {
        VBox root = new VBox();
        TextField textField = new TextField();

        initSpreadsheet();
        VBox.setVgrow(mSpreadsheet, Priority.ALWAYS);

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().add(new Label("Label"));
        root.getChildren().addAll(toolBar, mSpreadsheet);
        setScene(new Scene(root, 840, 600));
        getIcons().add(new Image(Main.class.getResourceAsStream("dolphin.png")));
    }

    private void initSpreadsheet() {
        mGridBase = new GridBase(mRowCount, mColumnCount);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        mSpreadsheetController = new SpreadsheetController(mGridBase);
        for (int row = 0; row < mGridBase.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < mGridBase.getColumnCount(); ++column) {
                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1, ""));
            }
            rows.add(list);
        }
        mGridBase.setRows(rows);
        mGridBase.addEventHandler(GridChange.GRID_CHANGE_EVENT, mSpreadsheetController);
        mSpreadsheet = new SpreadsheetView(mGridBase);
        for (SpreadsheetColumn c : mSpreadsheet.getColumns()) c.setPrefWidth(90);
    }
}
