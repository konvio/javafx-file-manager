package com.konv.dolphinexplorer.spreadsheet;

import com.konv.dolphinexplorer.DialogHelper;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.GridChange;
import org.jgrapht.graph.DefaultEdge;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SpreadsheetController implements EventHandler<GridChange> {
    private GridBase mGridBase;
    private Graph mGraph;
    private Tokenizer mTokenizer;

    public SpreadsheetController(GridBase gridBase) {
        mGridBase = gridBase;
        mGraph = new Graph(gridBase.getRowCount(), gridBase.getColumnCount());
        mTokenizer = new Tokenizer();
    }

    @Override
    public void handle(GridChange event) {
        Cell cell = mGraph.getCell(event.getRow(), event.getColumn());
        String formula = (String) event.getNewValue();
        List<Tokenizer.Token> tokensStream = mTokenizer.tokenize(formula);
        if (tokensStream == null) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid input", "Some arguments are not recognized");
        } else if (isSyntaxValid(tokensStream)) {
            cell.setFormula(formula);
            mGraph.resolveDependencies(cell);
            mGraph.evaluate();
        }
        showValues();
    }

    private void showExpressions() {
        mGridBase.removeEventHandler(GridChange.GRID_CHANGE_EVENT, this);
        for (int row = 0; row < mGridBase.getRowCount(); ++row) {
            for (int column = 0; column < mGridBase.getColumnCount(); ++column) {
                mGridBase.setCellValue(row, column, mGraph.getCell(row, column));
            }
        }
        mGridBase.addEventHandler(GridChange.GRID_CHANGE_EVENT, this);
    }

    private void showValues() {
        mGridBase.removeEventHandler(GridChange.GRID_CHANGE_EVENT, this);
        for (int row = 0; row < mGridBase.getRowCount(); ++row) {
            for (int column = 0; column < mGridBase.getColumnCount(); ++column) {
                mGridBase.setCellValue(row, column, mGraph.getCell(row, column));
            }
        }
        mGridBase.addEventHandler(GridChange.GRID_CHANGE_EVENT, this);
    }

    private boolean isSyntaxValid(List<Tokenizer.Token> tokensStream) {
        if (!SyntaxAnalyzer.isOperatorsBetweenOperands(tokensStream)) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid input",
                    "Binary operator is not between two operands");
            return false;
        } else if (!SyntaxAnalyzer.isBracesBalansed(tokensStream)) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid input", "Braces are not balanced");
            return false;
        } else if (!SyntaxAnalyzer.isBracesProperlyPositioned(tokensStream)) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid input", "Braces incorrectly place");
            return false;
        }
        return true;
    }

    public Cell getCell(int row, int column) {
        return mGraph.getCell(row, column);
    }
}
