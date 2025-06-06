package ru.shift.app;

import ru.shift.controller.GameController;
import ru.shift.controller.HighScoreController;
import ru.shift.model.Game;
import ru.shift.model.IGame;
import ru.shift.service.GameTimer;
import ru.shift.service.HighScoreService;
import ru.shift.view.MainWindow;
import ru.shift.view.SettingsWindow;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            SettingsWindow settingsWin = new SettingsWindow(mainWindow);

            IGame game = new Game();
            GameTimer timerService = new GameTimer(game);

            HighScoreService highScoreService = new HighScoreService(mainWindow);
            HighScoreController  highScoreController =
                    new HighScoreController(highScoreService, game, timerService, mainWindow);


            game.addListener(mainWindow);

            GameController gameController = new GameController(
                    game,
                    mainWindow,
                    settingsWin
            );

            mainWindow.setNewGameMenuAction(e -> gameController.restartGame());
            mainWindow.setSettingsMenuAction(e -> settingsWin.setVisible(true));
            mainWindow.setExitMenuAction(e -> System.exit(0));
            mainWindow.setNewGameRequestListener(gameController::restartGame);
            mainWindow.setExitRequestListener(() -> System.exit(0));

            mainWindow.setHighScoresMenuAction(e ->
                    highScoreController.showHighScoresTable()
            );

            mainWindow.setVisible(true);
        });
    }
}