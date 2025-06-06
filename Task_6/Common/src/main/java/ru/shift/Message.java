package ru.shift;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Message {

    private static final String SERVER = "Server";
    private final MessageType type;
    private final String sender;
    private final String content;
    private final long timestamp;

    @JsonCreator
    public Message(@JsonProperty("type") MessageType type,
                   @JsonProperty("sender") String sender,
                   @JsonProperty("content") String content,
                   @JsonProperty("timestamp") Long timestamp) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.timestamp = ((timestamp != null) ? timestamp : Instant.now().toEpochMilli());
    }

    public static Message chat(String sender, String text) {
        return new Message(MessageType.CHAT, sender, text, null);
    }

    public static Message userJoin(String user) {
        return new Message(MessageType.USER_JOIN, SERVER,
                String.format("%s присоединился к чату", user), null);
    }

    public static Message userLeave(String user) {
        return new Message(MessageType.USER_LEAVE, SERVER,
                String.format("%s покинул чат", user), null);
    }

    public static Message error(String text) {
        return new Message(MessageType.ERROR, SERVER, text, null);
    }

    public static Message nickRequest(String user) {
        return new Message(MessageType.NICK_REQUEST, user, null, null);
    }

    public static Message nickAccepted(String user) {
        return new Message(MessageType.NICK_ACCEPTED, user, null, null);
    }

    public static Message nickRejected(String cause) {
        return new Message(MessageType.NICK_REJECTED, SERVER, cause, null);
    }
}
