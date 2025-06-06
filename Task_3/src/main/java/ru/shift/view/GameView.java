package ru.shift.view;

public interface GameView {
    void createField(int rows, int cols);
    void setCell(int x, int y, GameImage img);
    void setMinesLeft(int n);
    void setCellListener(CellEventListener listener);
}