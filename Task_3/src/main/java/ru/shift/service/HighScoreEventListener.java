package ru.shift.service;

public interface HighScoreEventListener {
    void onHighScoreFileRecreated(String details);
    void onHighScoreSaveFailed(String details);
}
