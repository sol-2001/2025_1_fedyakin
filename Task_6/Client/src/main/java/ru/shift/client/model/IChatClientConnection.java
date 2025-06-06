package ru.shift.client.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;

public interface IChatClientConnection {
    void connect(String host, String port, String nick);

    void disconnect();

    void sendChat(String text);

    ReadOnlyObjectProperty<ChatClientConnection.Phase> phaseProperty();

    StringProperty nickErrorProperty();
}
