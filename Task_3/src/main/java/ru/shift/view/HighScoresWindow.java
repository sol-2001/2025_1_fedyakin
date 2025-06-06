package ru.shift.view;

import ru.shift.service.HighScoreService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoresWindow extends JDialog {
    private static final int MAX_RECORDS = 3;

    private final JLabel[] noviceLabels = new JLabel[MAX_RECORDS];
    private final JLabel[] mediumLabels = new JLabel[MAX_RECORDS];
    private final JLabel[] expertLabels = new JLabel[MAX_RECORDS];

    private final HighScoreService highScoreService;

    public HighScoresWindow(JFrame owner, HighScoreService highScoreService) {
        super(owner, "High Scores", true);
        this.highScoreService = highScoreService;

        GridBagLayout layout = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        int gridY = 0;

        contentPane.add(createTypeLabel("Novice:", layout, gridY++));
        for (int i = 0; i < MAX_RECORDS; i++) {
            noviceLabels[i] = createRecordLabel(layout, gridY++);
            contentPane.add(noviceLabels[i]);
        }

        contentPane.add(createTypeLabel("Medium:", layout, gridY++));
        for (int i = 0; i < MAX_RECORDS; i++) {
            mediumLabels[i] = createRecordLabel(layout, gridY++);
            contentPane.add(mediumLabels[i]);
        }

        contentPane.add(createTypeLabel("Expert:", layout, gridY++));
        for (int i = 0; i < MAX_RECORDS; i++) {
            expertLabels[i] = createRecordLabel(layout, gridY++);
            contentPane.add(expertLabels[i]);
        }

        contentPane.add(createCloseButton(layout, gridY));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(250, 50 + gridY * 25));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        updateScores();
    }

    public void updateScores() {
        List<String> novice = highScoreService.getHighScores(GameType.NOVICE);
        for (int i = 0; i < MAX_RECORDS; i++) {
            noviceLabels[i].setText(novice.get(i));
        }
        List<String> medium = highScoreService.getHighScores(GameType.MEDIUM);
        for (int i = 0; i < MAX_RECORDS; i++) {
            mediumLabels[i].setText(medium.get(i));
        }
        List<String> expert = highScoreService.getHighScores(GameType.EXPERT);
        for (int i = 0; i < MAX_RECORDS; i++) {
            expertLabels[i].setText(expert.get(i));
        }
    }

    private JLabel createTypeLabel(String text, GridBagLayout layout, int gridY) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.insets = new Insets(5, 5, 0, 5);
        layout.setConstraints(label, gbc);
        return label;
    }

    private JLabel createRecordLabel(GridBagLayout layout, int gridY) {
        JLabel label = new JLabel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.insets = new Insets(0, 20, 0, 5);
        layout.setConstraints(label, gbc);
        return label;
    }

    private JButton createCloseButton(GridBagLayout layout, int gridY) {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        okButton.setPreferredSize(new Dimension(80, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.insets = new Insets(10, 0, 10, 0);
        layout.setConstraints(okButton, gbc);
        return okButton;
    }
}
