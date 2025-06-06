package ru.shift.controller;

import ru.shift.model.IGame;
import ru.shift.service.GameTimer;
import ru.shift.service.HighScoreService;
import ru.shift.view.*;

public class HighScoreController implements WinListener {

    private final HighScoreService service;
    private final IGame game;
    private final MainWindow view;
    private final HighScoresWindow scoresWindow;

    public HighScoreController(HighScoreService service,
                               IGame game,
                               GameTimer timer,
                               MainWindow view) {
        this.service = service;
        this.game    = game;
        this.view    = view;
        this.scoresWindow = new HighScoresWindow(view, service);

        view.setWinListener(this);

        game.addListener(timer);
        timer.addListener(view);
    }

    @Override
    public void onGameWon(int seconds) {
        boolean isRecord = service.isNewHighScore(game.getGameType(), seconds);

        if (isRecord) {
            RecordsWindow rw = new RecordsWindow(view);
            rw.setNameListener(name ->
                    service.addHighScore(game.getGameType(), name, seconds));
            rw.setVisible(true);
        }

        WinWindow w = new WinWindow(view);
        w.setNewGameListener(e -> game.start(game.getGameType()));
        w.setExitListener(e -> System.exit(0));
        w.setVisible(true);
    }

    public void showHighScoresTable() {
        scoresWindow.updateScores();
        scoresWindow.setVisible(true);
    }
}
