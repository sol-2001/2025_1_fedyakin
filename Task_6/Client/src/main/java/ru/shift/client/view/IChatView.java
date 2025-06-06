package ru.shift.client.view;

public interface IChatView {
    String getHost();
    String getPort();
    String getNick();
    String getInputText();
    void clearInput();
}
