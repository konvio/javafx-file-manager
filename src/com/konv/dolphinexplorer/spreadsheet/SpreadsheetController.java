package com.konv.dolphinexplorer.spreadsheet;

import com.konv.dolphinexplorer.DialogHelper;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.GridChange;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class SpreadsheetController implements EventHandler<GridChange> {
    private GridBase mGridBase;
    private BigInteger[][] mValues;
    private String[][] mExpressions;
    private Tokenizer mTokenizer;

    public SpreadsheetController(GridBase gridBase) {
        mGridBase = gridBase;
        mValues = new BigInteger[mGridBase.getRowCount()][mGridBase.getColumnCount()];
        mExpressions = new String[mGridBase.getRowCount()][mGridBase.getColumnCount()];
        mTokenizer = new Tokenizer();
    }

    @Override
    public void handle(GridChange event) {
        int row = event.getRow();
        int column = event.getColumn();
        List<Tokenizer.Token> tokensStream = mTokenizer.tokenize((String) event.getNewValue());
        if (tokensStream == null) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid input", "Some arguments are not recognized");
        } else if (validateSyntax(tokensStream)) {
            mExpressions[row][column] = (String) event.getNewValue();
            mValues[row][column] = evaluate(mTokenizer.tokenize((String) event.getNewValue()));
        }
        showValues();
    }

    private void showExpressions() {
        mGridBase.removeEventHandler(GridChange.GRID_CHANGE_EVENT, this);
        for (int row = 0; row < mGridBase.getRowCount(); ++row) {
            for (int column = 0; column < mGridBase.getColumnCount(); ++column) {
                mGridBase.setCellValue(row, column, mExpressions[row][column]);
            }
        }
        mGridBase.addEventHandler(GridChange.GRID_CHANGE_EVENT, this);
    }

    private void showValues() {
        mGridBase.removeEventHandler(GridChange.GRID_CHANGE_EVENT, this);
        for (int row = 0; row < mGridBase.getRowCount(); ++row) {
            for (int column = 0; column < mGridBase.getColumnCount(); ++column) {
                mGridBase.setCellValue(row, column, mValues[row][column]);
            }
        }
        mGridBase.addEventHandler(GridChange.GRID_CHANGE_EVENT, this);
    }

    private BigInteger evaluate(List<Tokenizer.Token> tokensStream) {
        tokensStream.add(0, new Tokenizer.Token(Tokenizer.TokenType.BRACEOPEN, "("));
        tokensStream.add(new Tokenizer.Token(Tokenizer.TokenType.BRACECLOSE, ")"));
        LinkedList<Tokenizer.Token> outputStack = new LinkedList<>();
        LinkedList<Tokenizer.Token> operatorStack = new LinkedList<>();
        for (Tokenizer.Token token : tokensStream) {
            switch (token.type) {
                case NUMBER:
                    outputStack.addLast(token);
                    break;
                case BINARYOP:
                    operatorStack.addLast(token);
                    break;
                case BRACEOPEN:
                    outputStack.addLast(token);
                    break;
                case BRACECLOSE:
                    int openBraceIndex = outputStack.lastIndexOf(new Tokenizer.Token(Tokenizer.TokenType.BRACEOPEN, "("));
                    int operatorsNumber = outputStack.size() - openBraceIndex - 2;
                    for (int i = 0; i < operatorsNumber; ++i) {
                        String secondOperand = outputStack.removeLast().data;
                        String firstOperand = outputStack.removeLast().data;
                        String operator = operatorStack.removeLast().data;
                        String result = evaluate(firstOperand, secondOperand, operator).toString();
                        outputStack.addLast(new Tokenizer.Token(Tokenizer.TokenType.NUMBER, result));
                    }
                    outputStack.removeLastOccurrence(new Tokenizer.Token(Tokenizer.TokenType.BRACEOPEN, "("));
                    break;
            }
        }
        if (outputStack.isEmpty()) return BigInteger.ZERO;
        else return new BigInteger(outputStack.getLast().data);
    }

    private BigInteger evaluate(String firstOperand, String secondOperand, String operator) {
        BigInteger a = new BigInteger(firstOperand);
        BigInteger b = new BigInteger(secondOperand);
        switch (operator) {
            case "+":
                return a.add(b);
            case "-":
                return a.subtract(b);
            case "*":
                return a.multiply(b);
            case "/":
                return a.divide(b);
            default:
                return null;
        }
    }

    private boolean validateSyntax(List<Tokenizer.Token> tokensStream) {
        if (!SyntaxAnalyzer.isOperatorsBetweenOperands(tokensStream)) {
            DialogHelper.showAlert(Alert.AlertType.INFORMATION, "Spreadsheet", "Invalid input", "It seems like " +
                    "binary operator are not between two operands.");
            return false;
        } else if (!SyntaxAnalyzer.isBracesBalansed(tokensStream)) {
            DialogHelper.showAlert(Alert.AlertType.INFORMATION, "Spreadsheet", "Invalid input", "It seems like " +
                    "braces are not balanced");
            return false;
        } else if (!SyntaxAnalyzer.isBracesProperlyPositioned(tokensStream)) {
            DialogHelper.showAlert(Alert.AlertType.INFORMATION, "Spreadsheet", "Invalid input", "It seems like some braces are in wrong place");
            return false;
        }
        return true;
    }

    public String getExpression(int row, int column) {
        return mExpressions[row][column];
    }
}
