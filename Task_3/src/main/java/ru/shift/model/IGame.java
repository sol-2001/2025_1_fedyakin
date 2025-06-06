package ru.shift.model;


import ru.shift.view.GameListener;
import ru.shift.view.GameType;

//перенести
public interface IGame {
    void start(GameType type);

    GameType getGameType();

    void openCell(int x, int y);

    void markCell(int x, int y);

    void chord(int x, int y);

    int getRemainingMines();

    boolean isGameOver();

    int getRows();

    int getCols();

    Cell getCell(int x, int y);

    void addListener(GameListener l);

}
