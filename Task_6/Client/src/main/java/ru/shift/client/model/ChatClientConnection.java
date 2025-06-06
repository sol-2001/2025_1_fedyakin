package ru.shift.client.model;

import javafx.application.Platform;
import javafx.beans.property.*;
import ru.shift.JsonUtil;
import ru.shift.Message;
import ru.shift.Protocol;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClientConnection implements IChatClientConnection {
    private static final int TIMEOUT = 3000;
    private static final String THREAD_NAME = "socket-reader";
    private static final String WHITESPACE_REGEX = "\\s+";
    private static final int MAX_SPLIT_PARTS = 2;

    public enum Phase {
        CONNECTING,
        WAITING_NICK,
        CHATTING,
        DISCONNECTED,
    }

    private final ChatModel model;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private final BooleanProperty connected = new SimpleBooleanProperty(false);
    private final ObjectProperty<Phase> phase = new SimpleObjectProperty<>(Phase.DISCONNECTED);

    private final StringProperty nickError = new SimpleStringProperty("");

    public ChatClientConnection(ChatModel model) {
        this.model = model;
    }

    public void connect(String host, String port, String nick) {
        try {

            if (executor.isShutdown()) {
                executor = Executors.newSingleThreadExecutor();
            }

            int validPort = Integer.parseInt(port.trim());

            if (!validNick(nick)) {
                error("Неверный формат имени");
                phase.set(Phase.DISCONNECTED);
                return;
            }

            phase.set(Phase.CONNECTING);

            SocketAddress address = new InetSocketAddress(host, validPort);
            socket = new Socket();
            socket.connect(address, TIMEOUT);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Protocol.CHARSET_NAME));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Protocol.CHARSET_NAME), true);

            Thread reader = new Thread(this::read, THREAD_NAME);
            reader.setDaemon(true);
            reader.start();

            String json = JsonUtil.toJson(Message.nickRequest(nick));
            if (json == null) {
                error("Ошибка при формировании запроса ника");
                phase.set(Phase.DISCONNECTED);
                disconnect();
                return;
            }

            send(Message.nickRequest(nick));

        } catch (UnknownHostException uhe) {
            error("Неверный хост " + uhe.getMessage());
            phase.set(Phase.DISCONNECTED);
            connected.set(false);
        } catch (NumberFormatException nfe) {
            error("Неверный формат порта");
            phase.set(Phase.DISCONNECTED);
            connected.set(false);
        } catch (IOException e) {
            error("Не удалось подключиться: " + e.getMessage());
            phase.set(Phase.DISCONNECTED);
            connected.set(false);
        }
    }

    private boolean validNick(String nick) {
        return !nick.trim().isEmpty();
    }

    public void sendChat(String text) {
        Message chat = Message.chat(model.userNameProperty().get(), text);
        executor.submit(() -> send(chat));
    }

    public StringProperty nickErrorProperty() {
        return nickError;
    }

    public void disconnect() {
        if (socket == null || socket.isClosed()) {
            return;
        }
        try {
            socket.close();
        } catch (IOException ignored) {
        }
        executor.shutdownNow();
    }

    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase;
    }

    private void read() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                Message msg = JsonUtil.fromJson(line);
                if (msg == null) {
                    error("Ошибка при обработке входящего сообщения");
                    continue;
                }
                handleIncoming(msg);
            }

            Platform.runLater(() ->
                    model.addMessage(Message.error("Соединение закрыто сервером")));
            connected.set(false);
            phase.set(Phase.DISCONNECTED);
        } catch (IOException e) {
            error("Соединение прервано: " + e.getMessage());
        }
    }

    private void handleIncoming(Message msg) {
        switch (msg.getType()) {
            case CHAT -> Platform.runLater(() -> model.addMessage(msg));
            case USER_JOIN -> Platform.runLater(() -> {
                model.addUser(extractNick(msg));
                model.addMessage(msg);
            });
            case USER_LEAVE -> Platform.runLater(() -> {
                model.removeUser(extractNick(msg));
                model.addMessage(msg);
            });
            case NICK_ACCEPTED -> Platform.runLater(() -> {
                phase.set(Phase.CHATTING);
                model.setUserName(msg.getSender());
                connected.set(true);
                nickError.set("");
            });
            case NICK_REJECTED -> Platform.runLater(() -> {
                phase.set(Phase.WAITING_NICK);
                connected.set(false);
                disconnect();
                nickError.set(msg.getContent());
            });
            case ERROR -> Platform.runLater(() -> error(msg.getContent()));
            default -> Platform.runLater(() -> error("Неизвестный тип сообщения: " + msg.getType()));
        }
    }

    private String extractNick(Message msg) {
        String[] parts = msg.getContent().split(WHITESPACE_REGEX, MAX_SPLIT_PARTS);
        return parts[0];
    }

    private void send(Message msg) {
        String json = JsonUtil.toJson(msg);
        if (json == null) {
            error("Ошибка при отправке сообщения");
            return;
        }
        out.println(json);
    }

    private void error(String text) {
        Platform.runLater(() -> model.addMessage(Message.error(text)));
    }
}
