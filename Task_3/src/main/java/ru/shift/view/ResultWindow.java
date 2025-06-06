package ru.shift.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class ResultWindow extends JDialog {
    private ActionListener newGameListener;
    private ActionListener exitListener;

    private static final Dimension WINDOW_SIZE = new Dimension(300, 130);
    private static final Dimension BUTTON_SIZE = new Dimension(100, 25);
    private static final Insets EXIT_BUTTON_INSETS = new Insets(15, 5, 0, 0);
    private static final Insets NEW_GAME_BUTTON_INSETS = new Insets(15, 0, 0, 0);


    public ResultWindow(JFrame owner, String title, String resultText) {
        super(owner, title, true);
        initUI(resultText);
    }

    private void initUI(String resultText) {
        GridBagLayout layout = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        contentPane.add(createResultLabel(layout, resultText));
        contentPane.add(createNewGameButton(layout));
        contentPane.add(createExitButton(layout));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(WINDOW_SIZE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public void setNewGameListener(ActionListener newGameListener) {
        this.newGameListener = newGameListener;
    }

    public void setExitListener(ActionListener exitListener) {
        this.exitListener = exitListener;
    }

    private JLabel createResultLabel(GridBagLayout layout, String text) {
        JLabel label = new JLabel(text);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        layout.setConstraints(label, gbc);
        return label;
    }

    private JButton createNewGameButton(GridBagLayout layout) {
        JButton newGameButton = createBaseButton("New game", e -> {
            dispose();
            if (newGameListener != null) {
                newGameListener.actionPerformed(e);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = NEW_GAME_BUTTON_INSETS;
        layout.setConstraints(newGameButton, gbc);

        return newGameButton;
    }

    private JButton createExitButton(GridBagLayout layout) {
        JButton exitButton = createBaseButton("Exit", e -> {
            dispose();
            if (exitListener != null) {
                exitListener.actionPerformed(e);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = EXIT_BUTTON_INSETS;
        layout.setConstraints(exitButton, gbc);

        return exitButton;
    }

    private JButton createBaseButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setPreferredSize(BUTTON_SIZE);
        return button;
    }

}
