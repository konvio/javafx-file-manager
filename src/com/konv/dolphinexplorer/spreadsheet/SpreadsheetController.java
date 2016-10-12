package com.konv.dolphinexplorer.spreadsheet;

import com.konv.dolphinexplorer.DialogHelper;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.GridChange;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class SpreadsheetController implements EventHandler<GridChange> {
    private GridBase mGridBase;
    private BigInteger[][] mValues;
    private String[][] mExpressions;
    private DirectedGraph<Cell, DefaultEdge> mDependencyGraph;
    private DirectedSimpleCycles<Cell, DefaultEdge> mCycleDetector;
    private Tokenizer mTokenizer;

    public SpreadsheetController(GridBase gridBase) {
        mGridBase = gridBase;
        mValues = new BigInteger[mGridBase.getRowCount()][mGridBase.getColumnCount()];
        mExpressions = new String[mGridBase.getRowCount()][mGridBase.getColumnCount()];
        mTokenizer = new Tokenizer();
        mDependencyGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
        mCycleDetector = new TarjanSimpleCycles<>(mDependencyGraph);
    }

    @Override
    public void handle(GridChange event) {
        int row = event.getRow();
        int column = event.getColumn();
        List<Tokenizer.Token> tokensStream = mTokenizer.tokenize((String) event.getNewValue());
        if (tokensStream == null) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid input", "Some arguments are not recognized");
        } else if (validateSyntax(tokensStream, new Cell(event.getRow(), event.getColumn()))) {
            addDependencies(tokensStream, new Cell(event.getRow(), event.getColumn()));
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
                case REFERENCE:
                    Cell cell = new Cell(token.data);
                    outputStack.addLast(new Tokenizer.Token(Tokenizer.TokenType.NUMBER, cell.getValue().toString()));
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

    private boolean validateSyntax(List<Tokenizer.Token> tokensStream,  Cell cell) {
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
        } else if (!checkEmptyReferences(tokensStream)) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid input", "Undefined reference found");
            return false;
        } else if (!checkCircularReferences(tokensStream, cell)) {
            DialogHelper.showAlert(Alert.AlertType.WARNING, "Spreadsheet", "Invalid", "Cyclic references found");
        }
        return true;
    }

    private boolean checkEmptyReferences(List<Tokenizer.Token> tokensStream) {
        for (Tokenizer.Token token : tokensStream) {
            if (token.type == Tokenizer.TokenType.REFERENCE) {
                Cell cell = new Cell(token.data);
                if (!cell.isEvaluated()) return false;
            }
        }
        return true;
    }

    private boolean checkCircularReferences(List<Tokenizer.Token> tokensStream, Cell cell) {
        DirectedGraph<Cell, DefaultEdge> graph = (DirectedGraph<Cell, DefaultEdge>)
                ((SimpleDirectedGraph<Cell, DefaultEdge>) mDependencyGraph).clone();

        graph.removeAllEdges(graph.outgoingEdgesOf(cell));
        for (Tokenizer.Token token : tokensStream) {
            if (token.type == Tokenizer.TokenType.REFERENCE) {
                mDependencyGraph.addEdge(cell, new Cell(token.data));
            }
        }
        mCycleDetector.setGraph(graph);
        boolean containsCycle = mCycleDetector.findSimpleCycles().size() > 0;
        mCycleDetector.setGraph(mDependencyGraph);
        return containsCycle;
    }

    private void addDependencies(List<Tokenizer.Token> tokensStream, Cell cell) {
        mDependencyGraph.removeAllEdges(mDependencyGraph.outgoingEdgesOf(cell));
        for (Tokenizer.Token token : tokensStream) {
            if (token.type == Tokenizer.TokenType.REFERENCE) {
                mDependencyGraph.addEdge(cell, new Cell(token.data));
            }
        }
    }

    public String getTextRepresentation(int row, int column) {
        Cell cell = new Cell(row, column);
        return cell.getExpression();
    }

    public class Cell {
        private int mRow;
        private int mColumn;

        public Cell(String reference) {
            mColumn = reference.charAt(0) - 'A';
            mRow = Integer.parseInt(reference.substring(1)) - 1;
        }

        public Cell(int row, int column) {
            mColumn = column;
            mRow = row;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Cell) {
                Cell other = (Cell) obj;
                return mRow == other.mRow && mColumn == other.mColumn;
            } else {
                return false;
            }
        }

        public BigInteger getValue() {
            return mValues[mRow][mColumn];
        }

        public String getExpression() {
            return mExpressions[mRow][mColumn];
        }

        public boolean isEvaluated() {
            return mValues[mRow][mColumn] != null;
        }
    }
}
