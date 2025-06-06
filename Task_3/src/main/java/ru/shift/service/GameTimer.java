package ru.shift.service;


import ru.shift.model.CellState;
import ru.shift.view.CellUpdate;
import ru.shift.view.GameListener;
import ru.shift.model.IGame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameTimer implements GameListener {

    private final IGame game;
    private final ScheduledExecutorService es =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "game‑timer");
                t.setDaemon(true);
                return t;
            });

    private ScheduledFuture<?> task;
    private int seconds = 0;
    private final List<TimerListener> listeners = new ArrayList<>();

    public GameTimer(IGame game) {
        this.game = game;
    }

    public void addListener(TimerListener l) {
        listeners.add(l);
    }

    @Override
    public void onCellsChanged(List<CellUpdate> cells) {
        if (task != null) return;
        for (CellUpdate cell : cells) {
            if (game.getCell(cell.x(), cell.y()).getState() == CellState.OPENED) {
                start();
                break;
            }
        }
    }

    @Override
    public void onMinesLeftChanged(int m) {
    }

    @Override
    public void onGameLost() {
        stop();
    }

    @Override
    public void onGameWon() {
        stop();
    }

    @Override
    public void onGameStarted(int rows, int cols, int minesLeft) {
        reset();
    }

    private void start() {
        task = es.scheduleAtFixedRate(() -> {
            seconds++;
            listeners.forEach(l -> l.onTick(seconds));
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
        }
    }

    private void reset() {
        stop();
        seconds = 0;
        listeners.forEach(l -> l.onTick(0));
    }
}
