package com.kodilla.sudokujavafx.data;

import com.kodilla.sudokujavafx.logic.Validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuBoard implements Cloneable {
    private String name;
    private int numberOfCopy;
    private List<SudokuRow> sudokuBoardList;
    private List<SubBoard> subBoardList = new ArrayList<>(Arrays.asList(
            SubBoard.TOP_LEFT, SubBoard.TOP_CENTER, SubBoard.TOP_RIGHT,
            SubBoard.CENTER_LEFT, SubBoard.CENTER, SubBoard.CENTER_RIGHT,
            SubBoard.BOTTOM_LEFT, SubBoard.BOTTOM_CENTER, SubBoard.BOTTOM_RIGHT));

    public SudokuBoard() {
        sudokuBoardList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            sudokuBoardList.add(new SudokuRow(i));
        }
        setNumberOfCopy(0);
    }

    public SudokuBoard deepCopy() throws CloneNotSupportedException {
        SudokuBoard copyOfBoard = (SudokuBoard) this.clone();
        copyOfBoard.setNumberOfCopy(this.getNumberOfCopy() + 1);
        copyOfBoard.setName(this.getName());
        copyOfBoard.sudokuBoardList = new ArrayList<>();
        for (SudokuRow row : getSudokuBoardList()) {
            //List<SudokuElement> copyOfSudokuElementList = new ArrayList<>();
            SudokuRow copyOfRow = new SudokuRow(row.getRowNumber());
            for (SudokuElement element : row.getSudokuElementsList()) {
                SudokuElement copyOfElement = new SudokuElement(element.getFieldValue(),
                        element.getRowIndex(), element.getColIndex(),
                        element.getAvailableFieldValues(), element.isFixed());
                copyOfRow.getSudokuElementsList().set(element.getColIndex(), copyOfElement);
                //copyOfRow.getSudokuElementsList().add(copyOfElement);
            }
            copyOfBoard.getSudokuBoardList().add(copyOfRow);
        }
        return copyOfBoard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfCopy() {
        return numberOfCopy;
    }

    public void setNumberOfCopy(int numberOfCopy) {
        this.numberOfCopy = numberOfCopy;
    }

    public void setSubBoardList(List<SubBoard> subBoardList) {
        this.subBoardList = subBoardList;
    }

    public void updateSubBoards() {
        SubBoard.TOP_LEFT.addElementsToSubBoardElementsList(SubBoard.TOP_LEFT, this);
        SubBoard.TOP_CENTER.addElementsToSubBoardElementsList(SubBoard.TOP_CENTER, this);
        SubBoard.TOP_RIGHT.addElementsToSubBoardElementsList(SubBoard.TOP_RIGHT, this);
        SubBoard.CENTER_LEFT.addElementsToSubBoardElementsList(SubBoard.CENTER_LEFT, this);
        SubBoard.CENTER.addElementsToSubBoardElementsList(SubBoard.CENTER, this);
        SubBoard.CENTER_RIGHT.addElementsToSubBoardElementsList(SubBoard.CENTER_RIGHT, this);
        SubBoard.BOTTOM_LEFT.addElementsToSubBoardElementsList(SubBoard.BOTTOM_LEFT, this);
        SubBoard.BOTTOM_CENTER.addElementsToSubBoardElementsList(SubBoard.BOTTOM_CENTER, this);
        SubBoard.BOTTOM_RIGHT.addElementsToSubBoardElementsList(SubBoard.BOTTOM_RIGHT, this);
    }

    public SudokuElement getElementFromBoard(int row, int col) {
        return sudokuBoardList.stream()
                .flatMap(r -> r.getSudokuElementsList().stream())
                .filter((e) -> e.getRowIndex() == row && e.getColIndex() == col)
                .findAny().get();
    }

    public void setValueElementFromBoard(int val, int row, int col) {
        getElementFromBoard(row, col).setFieldValue(val);
    }
    public void setValueElementFromBoard(int val, int row, int col, boolean fixed) {
        getElementFromBoard(row, col).setFieldValue(val);
        getElementFromBoard(row, col).setFixed(fixed);
    }

    public List<Integer> getRowValues(int rowIndex) {
        return getSudokuBoardList().get(rowIndex)
                .getSudokuElementsList().stream()
                .map(e -> e.getFieldValue())
                .collect(Collectors.toList());
    }
    public List<Integer> getColValues(int colIndex) {
        return getSudokuBoardList().stream()
                .flatMap(r -> r.getSudokuElementsList().stream())
                .filter(e -> e.getColIndex() == colIndex)
                .map(v -> v.getFieldValue())
                .collect(Collectors.toList());
    }
    public List<Integer> getSubBoardValues(int rowIndex, int colIndex) {
        updateSubBoards();
        if (rowIndex < 3 && colIndex < 3) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 0)
                    .filter(sr -> sr.getSubBoardRowIndex() == 0)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex < 3 && colIndex >= 3 && colIndex < 6) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 1)
                    .filter(sr -> sr.getSubBoardRowIndex() == 0)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex < 3 && colIndex >= 6) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 2)
                    .filter(sr -> sr.getSubBoardRowIndex() == 0)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex >= 3 && rowIndex < 6 && colIndex < 3) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 0)
                    .filter(sr -> sr.getSubBoardRowIndex() == 1)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex >= 3 && rowIndex < 6 && colIndex >= 3 && colIndex < 6) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 1)
                    .filter(sr -> sr.getSubBoardRowIndex() == 1)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex >= 3 && rowIndex < 6 && colIndex >= 6) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 2)
                    .filter(sr -> sr.getSubBoardRowIndex() == 1)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex >= 6 && colIndex < 3) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 0)
                    .filter(sr -> sr.getSubBoardRowIndex() == 2)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex >= 6 && colIndex >= 3 && colIndex < 6) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 1)
                    .filter(sr -> sr.getSubBoardRowIndex() == 2)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else if (rowIndex >= 6 && colIndex >= 6) {
            return getSubBoardList().stream()
                    .filter(sc -> sc.getSubBoardColIndex() == 2)
                    .filter(sr -> sr.getSubBoardRowIndex() == 2)
                    .flatMap(s -> s.getSubBoardElementsList().stream())
                    .map(e -> e.getFieldValue())
                    .collect(Collectors.toList());
        }
        else {
            return null;
        }
    }
    public void calculateBoard() {
        getSudokuBoardList().stream()
                .flatMap(l -> l.getSudokuElementsList().stream())
                .filter(v -> v.getFieldValue() == 0)
                .forEach(e -> e.calculateAvailableFieldValues(this));
    }

    @Override
    public String toString() {
        String boardString = "";
        for (SudokuRow row : sudokuBoardList) {
            boardString = boardString + row.toString() + "\n";
        }
        return boardString;
    }

    public List<SudokuRow> getSudokuBoardList() {
        return sudokuBoardList;
    }

    public void setSudokuBoardList(List<SudokuRow> sudokuBoardList) {
        this.sudokuBoardList = sudokuBoardList;
    }

    public void newRandomBoard() {

    }

    public void setBoardManually() {

    }
    public SudokuBoard setBoardFromString(String text) throws IOException {
        SudokuBoard newBoard = new SudokuBoard();
        int i = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (Validator.INSTANCE.checkFieldTextToInt(Character.toString(text.charAt(i)))) {
                    int elementValue = Integer.parseInt(Character.toString(text.charAt(i)));
                    boolean fixed = true;
                    if (elementValue == 0) fixed = false;
                    newBoard.setValueElementFromBoard(elementValue, row, col, fixed);
                    //System.out.println("elementValue: " + elementValue);
                    //System.out.println("i: " + i);
                    i++;
                } else {
                    throw new IOException("*** invalid element value at [" + i + "] ***");
                }
            }
        }
        return newBoard;
    }

    public SudokuBoard loadBoard(File file) {
        System.out.println(file);
        try {
            System.out.println("Loading the board");
            String textFromFile = Files.readString(file.toPath());
            String newString = "";
            for (char c : textFromFile.toCharArray()) {
                if (Validator.INSTANCE.getAvailableCharacterForSudoku().contains(c)) {
                    newString = newString + c;
                }
            }
            System.out.println(newString);
            SudokuBoard newBoard = setBoardFromString(newString);
            newBoard.setName(file.getName());
            newBoard.setNumberOfCopy(0);
            System.out.println("board");
            System.out.println(newBoard);
            //GameProcessor.INSTANCE.setBoard(newBoard);
            //System.out.println("Successful loaded board: \n" + GameProcessor.INSTANCE.getBoard());
            return newBoard;
        } catch (IOException e) {
            System.out.println("Exception:" + e);
            return null;
        }
    }
    public void saveBoard(File file, SudokuBoard board) throws IOException {
        byte[] bytes = board.toString().getBytes();
        Files.write(file.toPath(), bytes);
    }
    public SudokuBoard clearBoard() {
        SudokuBoard board = new SudokuBoard();
        System.out.println(board);
        return board;
    }

    public List<SubBoard> getSubBoardList() {
        return subBoardList;
    }

    public List<SudokuElement> getUnsolvedSudokuElements() {
        return getSudokuBoardList().stream()
                .flatMap(l -> l.getSudokuElementsList().stream())
                .filter(e -> e.getFieldValue() == 0)
                .collect(Collectors.toList());
    }

    public int getNumberOfSolutions() {
        calculateBoard();
        List<Integer> numberAvailableFieldValuesForElements = getSudokuBoardList().stream()
                .flatMap(b -> b.getSudokuElementsList().stream())
                .filter(x -> !x.isFixed())
                .filter(y ->  y.getFieldValue() == 0)
                .map(e -> e.getAvailableFieldValues())
                .map(s -> s.size())
                .collect(Collectors.toList());

        return numberAvailableFieldValuesForElements.stream()
                .mapToInt(v -> v)
                .min().orElse(-1);         //TODO if -1 means sudoku solved, if 0 - sudoku unable to solve
    }

    public boolean isBoardCorrect() {
        return getSudokuBoardList().stream()
                .flatMap(r -> r.getSudokuElementsList().stream())
                .filter(v -> v.getFieldValue() != 0)
                .map(e -> e.isElementValueCorrect())
                .noneMatch(p -> p == false);
    }

    public boolean isBoardSolved() {
        List<SudokuElement> unsolvedElements = getUnsolvedSudokuElements();
        if ((unsolvedElements.size() == 0) && (isBoardCorrect())) {
            return true;
        } else {
            return false;
        }
    }
}
