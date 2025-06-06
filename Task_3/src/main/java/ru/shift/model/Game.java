package ru.shift.model;


import ru.shift.view.CellUpdate;
import ru.shift.view.GameListener;
import ru.shift.view.GameType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Game implements IGame {

    private Field field;
    private GameType type;
    private boolean minesPlaced = false;
    private final List<GameListener> listeners = new ArrayList<>();

    public Game() {
        start(GameType.NOVICE);
    }

    @Override
    public void start(GameType type) {
        this.type = type;
        this.minesPlaced = false;
        this.field = new Field(type.getRows(), type.getCols(), type.getMines());
        listeners.forEach(l -> l.onGameStarted(
                type.getRows(),
                type.getCols(),
                type.getMines()));
    }

    @Override
    public void openCell(int x, int y) {
        if (!minesPlaced) {
            field.placeMines(x, y);
            minesPlaced = true;
        }
        List<Point> changed = field.openCell(x, y);
        fireCellsChanged(changed);
        fireMinesLeftChanged();
        checkWinLose();
    }

    @Override
    public void markCell(int x, int y) {
        field.toggleMark(x, y);
        fireCellsChanged(List.of(new Point(x, y)));
        fireMinesLeftChanged();
    }

    @Override
    public void chord(int x, int y) {
        List<Point> changed = field.chord(x, y);
        fireCellsChanged(changed);
        checkWinLose();
    }

    @Override
    public int getRemainingMines() {
        return type.getMines() - field.getMarkedCount();
    }

    @Override
    public boolean isGameOver() {
        return field.isGameOver();
    }

    @Override
    public int getRows() {
        return field.getRows();
    }

    @Override
    public int getCols() {
        return field.getCols();
    }

    @Override
    public Cell getCell(int x, int y) {
        return field.getCell(x, y);
    }

    @Override
    public void addListener(GameListener l) {
        listeners.add(l);
    }

    public GameType getGameType() {
        return type;
    }

    private void fireCellsChanged(List<Point> pts) {
        if (pts.isEmpty()) return;
        List<CellUpdate> updates = new ArrayList<>(pts.size());
        for (Point p : pts) {
            Cell cell = field.getCell(p.x, p.y);
            updates.add(new CellUpdate(
                    p.x,
                    p.y,
                    cell.getState(),
                    cell.getAdjacentMines(),
                    cell.hasMine()
            ));
        }
        for (GameListener l : listeners) {
            l.onCellsChanged(updates);
        }
    }

    private void fireMinesLeftChanged() {
        int left = getRemainingMines();
        for (GameListener l : listeners) {
            l.onMinesLeftChanged(left);
        }
    }

    private void revealMinesToUi() {
        List<Point> mines = new ArrayList<>();
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                if (field.getCell(x, y).hasMine()) {
                    mines.add(new Point(x, y));
                }
            }
        }
        fireCellsChanged(mines);
    }

    private void checkWinLose() {

        if (field.isGameOver()) {
            field.revealAllMines();
            revealMinesToUi();
            listeners.forEach(GameListener::onGameLost);
        } else if (field.isWin()) {
            field.revealAllMines();
            revealMinesToUi();
            listeners.forEach(GameListener::onGameWon);
        }
    }
}