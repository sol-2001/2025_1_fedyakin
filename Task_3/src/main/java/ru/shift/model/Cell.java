package ru.shift.model;

public class Cell {
    private boolean hasMine;
    private CellState state;
    private int adjacentMines;

    public Cell() {
        hasMine = false;
        state = CellState.CLOSED;
        adjacentMines = 0;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public void setMine(boolean m) {
        hasMine = m;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState s) {
        state = s;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void incrementAdjacentMines() {
        adjacentMines++;
    }
}