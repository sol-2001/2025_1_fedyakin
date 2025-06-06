package ru.shift.client.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import ru.shift.Message;

public class ChatModel {
    @Getter
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    @Getter
    private final ObservableList<String> users = FXCollections.observableArrayList();

    private final ReadOnlyStringWrapper userName = new ReadOnlyStringWrapper("");

    public ReadOnlyStringProperty userNameProperty() {
        return userName;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addUser(String nick) {
        if (!users.contains(nick)) {
            users.add(nick);
        }
    }

    public void removeUser(String nick) {
        users.remove(nick);
    }

    public void clearChat() {
        messages.clear();
    }

    public void setUserName(String name) {
        userName.set(name);
    }
}
