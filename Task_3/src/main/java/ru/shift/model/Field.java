package ru.shift.model;

import java.awt.Point;
import java.util.*;

public class Field {
    private final int rows;
    private final int cols;
    private final int totalMines;
    private final Cell[][] cells;
    private boolean minesPlaced = false;
    private boolean gameOver = false;

    public Field(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        this.cells = new Cell[rows][cols];
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < cols; x++)
                cells[y][x] = new Cell();
    }

    public List<Point> openCell(int x, int y) {
        List<Point> changed = new ArrayList<>();
        openCellInternal(x, y, changed);
        return changed;
    }

    public List<Point> chord(int x, int y) {
        List<Point> changed = new ArrayList<>();
        chordInternal(x, y, changed);
        return changed;
    }

    public void placeMines(int safeX, int safeY) {
        if (minesPlaced) return;
        layMines(safeX, safeY);
        minesPlaced = true;
    }

    public void toggleMark(int x, int y) {
        if (!inBounds(x, y) || gameOver) return;

        Cell c = cells[y][x];

        if (c.getState() == CellState.CLOSED) {
            if (getMarkedCount() >= totalMines) {
                return;
            }
            c.setState(CellState.MARKED);
        } else if (c.getState() == CellState.MARKED) {
            c.setState(CellState.CLOSED);
        }
    }

    public int getMarkedCount() {
        int count = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (cells[y][x].getState() == CellState.MARKED) {
                    count++;
                }
            }
        }
        return count;
    }

    public void revealAllMines() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (cells[y][x].hasMine()) {
                    cells[y][x].setState(CellState.OPENED);
                }
            }
        }
    }

    private void openCellInternal(int x, int y, List<Point> changed) {
        if (!inBounds(x, y) || gameOver) return;
        if (!minesPlaced) placeMines(x, y);

        Cell c = cells[y][x];
        if (c.getState() != CellState.CLOSED) return;

        if (c.hasMine()) {
            c.setState(CellState.OPENED);
            changed.add(new Point(x, y));
            revealAllMines();
            gameOver = true;
            return;
        }

        if (c.getAdjacentMines() > 0) {
            c.setState(CellState.OPENED);
            changed.add(new Point(x, y));
            return;
        }

        floodFill(x, y, changed);
    }

    private void chordInternal(int x, int y, List<Point> changed) {
        if (!inBounds(x, y)) return;
        Cell center = cells[y][x];
        if (center.getState() != CellState.OPENED || center.getAdjacentMines() == 0) return;

        int flags = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = x + dx;
                int ny = y + dy;
                if (inBounds(nx, ny) && cells[ny][nx].getState() == CellState.MARKED) {
                    flags++;
                }
            }
        }
        if (flags != center.getAdjacentMines()) return;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                openCellInternal(x + dx, y + dy, changed);
            }
        }
    }

    private void floodFill(int startX, int startY, List<Point> changed) {
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{startX, startY});

        while (!q.isEmpty()) {
            int[] p = q.poll();
            int x = p[0], y = p[1];

            if (!inBounds(x, y)) continue;

            Cell c = cells[y][x];
            if (c.getState() != CellState.CLOSED || c.hasMine()) continue;

            c.setState(CellState.OPENED);
            changed.add(new Point(x, y));

            if (c.getAdjacentMines() > 0) continue;

            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    if (dx != 0 || dy != 0) {
                        q.add(new int[]{x + dx, y + dy});
                    }
                }
            }
        }
    }

    private void layMines(int safeX, int safeY) {
        List<Point> candidates = getCandidatePositions(safeX, safeY);
        Collections.shuffle(candidates);

        for (int i = 0; i < totalMines; i++) {
            Point p = candidates.get(i);
            cells[p.y][p.x].setMine(true);
        }

        calculateAdjacentCounts();
    }

    private List<Point> getCandidatePositions(int safeX, int safeY) {
        List<Point> list = new ArrayList<>(rows * cols);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (Math.abs(x - safeX) <= 1 && Math.abs(y - safeY) <= 1) {
                    continue;
                }
                list.add(new Point(x, y));
            }
        }
        return list;
    }

    private void calculateAdjacentCounts() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (!cells[y][x].hasMine()) continue;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx == 0 && dy == 0) continue;
                        int nx = x + dx, ny = y + dy;
                        if (inBounds(nx, ny) && !cells[ny][nx].hasMine()) {
                            cells[ny][nx].incrementAdjacentMines();
                        }
                    }
                }
            }
        }
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    public Cell getCell(int x, int y) {
        return cells[y][x];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWin() {
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < cols; x++) {
                Cell c = cells[y][x];
                if (!c.hasMine() && c.getState() != CellState.OPENED) {
                    return false;
                }
            }
        return true;
    }
}