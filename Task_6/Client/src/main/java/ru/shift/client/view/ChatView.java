package ru.shift.client.view;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.shift.Message;
import ru.shift.client.controller.ChatClientController;
import ru.shift.client.model.ChatClientConnection;
import ru.shift.client.model.ChatModel;
import ru.shift.client.model.IChatClientConnection;

@NoArgsConstructor
public class ChatView implements IChatView {
    @FXML
    public Label nickErrorLabel;
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField nickField;
    @FXML
    private Button connectButton;

    @FXML
    private TextArea messageArea;
    @FXML
    private ListView<String> usersView;

    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    @Setter
    private ChatModel model;
    @Setter
    private IChatClientConnection connection;
    @Setter
    private ChatClientController controller;

    @Override
    public String getHost() {
        return hostField.getText().trim();
    }

    @Override
    public String getPort() {
        return portField.getText().trim();
    }

    @Override
    public String getNick() {
        return nickField.getText().trim();
    }

    @Override
    public String getInputText() {
        return inputField.getText().trim();
    }

    @Override
    public void clearInput() {
        inputField.clear();
    }

    @FXML
    private void initialize() {

    }

    public void setupBindings() {
        if (model == null || connection == null) {
            throw new IllegalStateException("Model или connection не заданы");
        }
        usersView.setItems(model.getUsers());

        model.getMessages().addListener((ListChangeListener<Message>) s -> {
            while (s.next()) {
                if (s.wasAdded()) {
                    s.getAddedSubList().forEach(m -> {
                        String line = format(m);
                        if (line != null) {
                            messageArea.appendText(line + "\n");
                        }
                    });
                }
            }
        });

        connection.phaseProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == ChatClientConnection.Phase.CHATTING) {
                        model.clearChat();
                        messageArea.clear();
                    }
                });

        nickErrorLabel.textProperty().bind(connection.nickErrorProperty());
        nickErrorLabel.visibleProperty().bind(
                connection.phaseProperty().isEqualTo(ChatClientConnection.Phase.WAITING_NICK)
                        .and(connection.nickErrorProperty().isNotEmpty()));
        nickErrorLabel.managedProperty().bind(nickErrorLabel.visibleProperty());

        connectButton.disableProperty().bind(
                connection.phaseProperty().isEqualTo(ChatClientConnection.Phase.CONNECTING).or(
                        connection.phaseProperty().isEqualTo(ChatClientConnection.Phase.CHATTING)));

        sendButton.disableProperty().bind(
                connection.phaseProperty().isNotEqualTo(ChatClientConnection.Phase.CHATTING));

        inputField.disableProperty().bind(sendButton.disableProperty());
        inputField.setOnAction(e -> controller.onSendButtonClicked());

        hostField.disableProperty().bind(
                connection.phaseProperty().isEqualTo(ChatClientConnection.Phase.CHATTING));
        portField.disableProperty().bind(
                connection.phaseProperty().isEqualTo(ChatClientConnection.Phase.CHATTING));
        nickField.disableProperty().bind(
                connection.phaseProperty().isEqualTo(ChatClientConnection.Phase.CHATTING));
    }

    @FXML
    private void onConnect() {
        if (controller != null) {
            controller.onConnectButtonClicked();
        }
    }

    @FXML
    private void onSend() {
        if (controller != null) {
            controller.onSendButtonClicked();
        }
    }

    private String format(Message m) {
        return switch (m.getType()) {
            case NICK_REQUEST, NICK_ACCEPTED, NICK_REJECTED -> null;
            case CHAT -> String.format("[%s] %s: %s", time(m), m.getSender(), m.getContent());
            case USER_JOIN, USER_LEAVE -> "*** " + m.getContent() + " ***";
            case ERROR -> "!!! " + m.getContent();
        };
    }

    private String time(Message m) {
        return java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(java.time.ZoneId.systemDefault())
                .format(java.time.Instant.ofEpochMilli(m.getTimestamp()));
    }
}
