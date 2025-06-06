package ru.shift.client.controller;

import ru.shift.client.model.ChatModel;
import ru.shift.client.model.IChatClientConnection;
import ru.shift.client.view.IChatView;

public class ChatClientController {

    private final ChatModel model;
    private final IChatClientConnection connection;
    private final IChatView view;

    public ChatClientController(IChatView view, ChatModel model, IChatClientConnection connection) {
        this.view = view;
        this.model = model;
        this.connection = connection;
    }

    public void onConnectButtonClicked() {
        String host = view.getHost();
        String port = view.getPort();
        String nick = view.getNick();
        connection.connect(host, port, nick);
    }

    public void onSendButtonClicked() {
        String text = view.getInputText();
        if (!text.isEmpty()) {
            connection.sendChat(text);
            view.clearInput();
        }
    }

    public void shutdown() {
        connection.disconnect();
    }
}
