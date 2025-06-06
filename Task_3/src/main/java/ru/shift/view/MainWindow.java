package ru.shift.view;

import ru.shift.service.HighScoreEventListener;
import ru.shift.service.TimerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.List;

public class MainWindow extends JFrame implements GameView, TimerListener, GameListener, HighScoreEventListener {
    private final Container contentPane;
    private final GridBagLayout mainLayout;
    private final Insets TIMER_LABEL_INSETS = new Insets(0, 5, 0, 0);
    private final Insets TIMER_IMAGE_INSETS = new Insets(0, 20, 0, 0);
    private final Insets BOMB_COUNTER_INSETS = new Insets(0, 0, 0, 0);
    private final Insets BOMB_COUNTER_IMAGE_INSETS = new Insets(0, 5, 0, 20);
    private final Insets BUTTON_PUNEL_INSETS = new Insets(20, 20, 5, 20);




    private JMenuItem newGameMenu;
    private JMenuItem highScoresMenu;
    private JMenuItem settingsMenu;
    private JMenuItem exitMenu;

    private CellEventListener listener;

    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;

    private Runnable onNewGameRequested;
    private Runnable onExitRequested;

    private WinListener winListener;

    public MainWindow() {
        super("Miner");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        createMenu();

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);

        contentPane.setBackground(new Color(144, 158, 184));
    }
    @Override
    public void createField(int rows, int cols) {
        createGameField(rows, cols);
    }

    @Override
    public void setCell(int x, int y, GameImage image) {
        setCellImage(x, y, image);
    }

    @Override
    public void setMinesLeft(int value) {
        setBombsCount(value);
    }


    @Override
    public void onTick(int seconds) {
        SwingUtilities.invokeLater(() -> setTimerValue(seconds));
    }

    @Override
    public void onCellsChanged(List<CellUpdate> updates) {
        for (CellUpdate cell : updates) {
            setCell(cell.x(), cell.y(), toImg(cell));
        }
    }

    @Override
    public void onMinesLeftChanged(int minesLeft) {
        setMinesLeft(minesLeft);
    }

    @Override
    public void onGameLost() {
        SwingUtilities.invokeLater(() -> {
            LoseWindow w = new LoseWindow(this);
            w.setNewGameListener(e -> {
                if (onNewGameRequested != null) onNewGameRequested.run();
            });
            w.setExitListener(e -> {
                if (onExitRequested != null) onExitRequested.run();
            });
            w.setVisible(true);
        });
    }

    @Override
    public void onGameWon() {
        int time = Integer.parseInt(timerLabel.getText());
        if (winListener != null) {
            winListener.onGameWon(time);
        }
    }

    @Override
    public void onGameStarted(int rows, int cols, int minesLeft) {
        createField(rows, cols);
        setMinesLeft(minesLeft);

    }

    private GameImage toImg(CellUpdate c) {
        switch (c.state()) {
            case CLOSED:
                return GameImage.CLOSED;
            case MARKED:
                return GameImage.MARKED;
            default:
                if (c.hasMine()) return GameImage.BOMB;
                int n = c.adjacentMines();
                return n == 0 ? GameImage.EMPTY : GameImage.valueOf("NUM_" + n);
        }
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        gameMenu.add(newGameMenu = new JMenuItem("New Game"));
        gameMenu.addSeparator();
        gameMenu.add(highScoresMenu = new JMenuItem("High Scores"));
        gameMenu.add(settingsMenu = new JMenuItem("Settings"));
        gameMenu.addSeparator();
        gameMenu.add(exitMenu = new JMenuItem("Exit"));

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    public void setNewGameRequestListener(Runnable r) {
        this.onNewGameRequested = r;
    }

    public void setExitRequestListener(Runnable r) {
        this.onExitRequested = r;
    }

    public void setNewGameMenuAction(ActionListener listener) {
        newGameMenu.addActionListener(listener);
    }

    public void setHighScoresMenuAction(ActionListener listener) {
        highScoresMenu.addActionListener(listener);
    }

    public void setSettingsMenuAction(ActionListener listener) {
        settingsMenu.addActionListener(listener);
    }

    public void setExitMenuAction(ActionListener listener) {
        exitMenu.addActionListener(listener);
    }

    public void setCellListener(CellEventListener listener) {
        this.listener = listener;
    }

    public void setCellImage(int x, int y, GameImage gameImage) {
        cellButtons[y][x].setIcon(gameImage.getImageIcon());
    }

    public void setBombsCount(int bombsCount) {
        bombsCounterLabel.setText(String.valueOf(bombsCount));
    }

    public void setTimerValue(int value) {
        if (timerLabel != null) {
            timerLabel.setText(String.valueOf(value));
        }
    }

    public void setWinListener(WinListener l) {
        this.winListener = l;
    }

    public void createGameField(int rowsCount, int colsCount) {
        contentPane.removeAll();
        setPreferredSize(new Dimension(20 * colsCount + 70, 20 * rowsCount + 110));

        addButtonsPanel(createButtonsPanel(rowsCount, colsCount));
        addTimerImage();
        addTimerLabel(timerLabel = new JLabel("0"));
        addBombCounter(bombsCounterLabel = new JLabel("0"));
        addBombCounterImage();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButtonsPanel(int numberOfRows, int numberOfCols) {
        cellButtons = new JButton[numberOfRows][numberOfCols];
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(20 * numberOfCols, 20 * numberOfRows));
        buttonsPanel.setLayout(new GridLayout(numberOfRows, numberOfCols, 0, 0));

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                final int x = col;
                final int y = row;

                cellButtons[y][x] = new JButton(GameImage.CLOSED.getImageIcon());
                cellButtons[y][x].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (listener == null) {
                            return;
                        }

                        switch (e.getButton()) {
                            case MouseEvent.BUTTON1:
                                listener.onMouseClick(x, y, ButtonType.LEFT_BUTTON);
                                break;
                            case MouseEvent.BUTTON3:
                                listener.onMouseClick(x, y, ButtonType.RIGHT_BUTTON);
                                break;
                            case MouseEvent.BUTTON2:
                                listener.onMouseClick(x, y, ButtonType.MIDDLE_BUTTON);
                                break;
                        }
                    }
                });
                buttonsPanel.add(cellButtons[y][x]);
            }
        }

        return buttonsPanel;
    }

    private void addButtonsPanel(JPanel buttonsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.insets = BUTTON_PUNEL_INSETS;
        mainLayout.setConstraints(buttonsPanel, gbc);
        contentPane.add(buttonsPanel);
    }

    private void addTimerImage() {
        JLabel label = new JLabel(GameImage.TIMER.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = TIMER_IMAGE_INSETS;
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private void addTimerLabel(JLabel timerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = TIMER_LABEL_INSETS;
        mainLayout.setConstraints(timerLabel, gbc);
        contentPane.add(timerLabel);
    }

    private void addBombCounter(JLabel bombsCounterLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.insets = BOMB_COUNTER_INSETS;
        gbc.weightx = 0.7;
        mainLayout.setConstraints(bombsCounterLabel, gbc);
        contentPane.add(bombsCounterLabel);
    }

    private void addBombCounterImage() {
        JLabel label = new JLabel(GameImage.BOMB_ICON.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.insets = BOMB_COUNTER_IMAGE_INSETS;
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    @Override
    public void onHighScoreFileRecreated(String msg) {
        JOptionPane.showMessageDialog(this,
                msg + "\nСоздан новый файл.",
                "Предупреждение", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void onHighScoreSaveFailed(String msg) {
        JOptionPane.showMessageDialog(this,
                msg + "\nРекорд будет утерян.",
                "Ошибка", JOptionPane.WARNING_MESSAGE);
    }
}