package ru.shift.view;

public enum GameType {
    NOVICE(9, 9, 10),
    MEDIUM(16, 16, 40),
    EXPERT(16, 30, 99);

    private final int rows, cols, mines;

    GameType(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMines() {
        return mines;
    }
}
