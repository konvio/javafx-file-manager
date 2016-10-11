package com.konv.dolphinexplorer.spreadsheet;

import javafx.event.EventHandler;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.GridChange;

public class SpreadsheetController implements EventHandler<GridChange> {
    private GridBase mGridBase;
    String a = "mGridBase";
    private String[][] mValues;
    private String[][] mExpressions;

    public SpreadsheetController(GridBase gridBase) {
        mGridBase = gridBase;
        mValues = new String[mGridBase.getRowCount()][mGridBase.getColumnCount()];
        mExpressions = new String[mGridBase.getRowCount()][mGridBase.getColumnCount()];
    }

    @Override
    public void handle(GridChange event) {
        for (int row = 0; row < mGridBase.getRowCount(); ++row) {
            for (int
                 column = 0;
                 column < mGridBase.getColumnCount(); ++column) {
                mGridBase.setCellValue(row, column, event.getNewValue());
            }
        }
        showValues();
    }

    private void showValues() {
        for (int row = 0; row < mGridBase.getRowCount(); ++row) {
            for (int column = 0; column < mGridBase.getColumnCount(); ++column) {
                mGridBase.setCellValue(row, column, mValues[row][column]);
            }
        }
    }
}
