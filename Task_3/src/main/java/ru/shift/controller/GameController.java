package ru.shift.controller;


import ru.shift.model.IGame;
import ru.shift.view.*;

public class GameController implements
        CellEventListener,
        GameTypeListener {

    private final IGame game;

    public GameController(IGame game,
                          GameView view,
                          SettingsWindow settingsWin) {
        this.game = game;

        view.setCellListener(this);
        settingsWin.setGameTypeListener(this);
        game.start(GameType.NOVICE);
    }

    @Override
    public void onMouseClick(int x, int y, ButtonType bt) {
        if (game.isGameOver()) return;
        switch (bt) {
            case LEFT_BUTTON  -> game.openCell(x, y);
            case RIGHT_BUTTON -> game.markCell(x, y);
            case MIDDLE_BUTTON-> game.chord(x, y);
        }
    }

    @Override
    public void onGameTypeChanged(GameType type) {
        game.start(type);
    }

    public void restartGame() {
        game.start(game.getGameType());
    }
}
