package ru.shift.service;

import ru.shift.view.GameType;

import java.io.*;
import java.util.*;

public class HighScoreService {
    private static final int MAX_SCORES = 3;
    private static final String FILE_NAME = "highscoeres.txt";
    private static final String UNKNOWN_PLAYER = "Unknown";
    private static final int UNKNOWN_TIME = 999;

    private final List<HighScoreEventListener> listeners = new ArrayList<>();

    private final Map<GameType, List<HighScore>> highScores = new EnumMap<>(GameType.class);

    public HighScoreService(HighScoreEventListener listener) {
        listeners.add(listener);
        for (GameType type : GameType.values()) {
            List<HighScore> list = new ArrayList<>();
            for (int i = 0; i < MAX_SCORES; i++) {
                list.add(new HighScore(UNKNOWN_PLAYER, UNKNOWN_TIME));
            }
            highScores.put(type, list);
        }
        try {
            loadFromFile();
        } catch (IOException ioe) {
            try {
                saveToFile();
                fireFileRecreated(ioe.getMessage());
            } catch (IOException e) {
                //Тут даже не знаю что дальше :)
            }
        }
    }

    private void loadFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addScoreFromLine(line.trim());
            }
        } catch (IOException ioe) {
            throw new IOException("Ошибка чтения файла с рекордами.", ioe);
        }

        for (GameType t : GameType.values()) {
            normalize(highScores.get(t));
        }
    }

    private void normalize(List<HighScore> list) {
        list.sort(Comparator.comparingInt(h -> h.time));

        while (list.size() < MAX_SCORES) {
            list.add(new HighScore(UNKNOWN_PLAYER, UNKNOWN_TIME));
        }

        if (list.size() > MAX_SCORES) {
            list.subList(MAX_SCORES, list.size()).clear();
        }
    }

    private void addScoreFromLine(String line) {
        if (line.isEmpty()) return;

        int equalPos   = line.indexOf('=');
        int dashPos = line.lastIndexOf('-');
        if (equalPos < 1 || dashPos < equalPos + 2) return;

        GameType type;
        try {
            type = GameType.valueOf(line.substring(0, equalPos).trim());
        } catch (IllegalArgumentException e) {
            return;
        }

        String name = line.substring(equalPos + 1, dashPos).trim();
        int time;
        try {
            time = Integer.parseInt(line.substring(dashPos + 1).trim());
        } catch (NumberFormatException e) {
            return;
        }

        highScores.get(type).add(new HighScore(name, time));
    }

    public List<String> getHighScores(GameType type) {
        List<String> result = new ArrayList<>();
        for (HighScore hs : highScores.get(type)) {
            result.add(hs.name + " - " + hs.time);
        }
        return result;
    }

    public boolean isNewHighScore(GameType type, int time) {
        List<HighScore> list = highScores.get(type);
        return time < list.get(list.size() - 1).time;
    }

    public void addHighScore(GameType type, String name, int time) {
        if (!isNewHighScore(type, time)) return;
        if (name == null || name.trim().isEmpty()) {
            name = UNKNOWN_PLAYER;
        }

        List<HighScore> list = new ArrayList<>(highScores.get(type));
        list.add(new HighScore(name, time));
        list.sort(Comparator.comparingInt(hs -> hs.time));

        if (list.size() > MAX_SCORES) {
            list = list.subList(0, MAX_SCORES);
        }

        highScores.put(type, list);
        try {
            saveToFile();
        } catch (IOException ioe) {
            fireSaveFailed(ioe.getMessage());
        }
    }

    private void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (GameType type : GameType.values()) {
                for (HighScore hs : highScores.get(type)) {
                    writer.write(type.name() + "=" + hs.name + "-" + hs.time);
                    writer.newLine();
                }
            }
        } catch (IOException ioe) {
            throw new IOException("Не удалось сохранить файл c рекордами.", ioe);
        }
    }

    private void fireFileRecreated(String msg) {
        for (HighScoreEventListener l : listeners)
            l.onHighScoreFileRecreated(msg);
    }

    private void fireSaveFailed(String msg) {
        for (HighScoreEventListener l : listeners)
            l.onHighScoreSaveFailed(msg);
    }

    private record HighScore(String name, int time) {
    }
}
