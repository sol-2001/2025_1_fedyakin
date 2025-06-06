package ru.shift.view;

import java.util.List;

public interface GameListener {
    void onCellsChanged(List<CellUpdate> changed);
    void onMinesLeftChanged(int minesLeft);
    void onGameLost();
    void onGameWon();
    void onGameStarted(int rows, int cols, int minesLeft);
}
