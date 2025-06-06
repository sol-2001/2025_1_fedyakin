package ru.shift.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.JsonUtil;
import ru.shift.Message;
import ru.shift.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ChatServer server;
    private final Socket socket;

    private String nickname = null;

    private final BufferedReader in;
    private final PrintWriter out;

    ClientHandler(ChatServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Protocol.CHARSET_NAME));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Protocol.CHARSET_NAME), true);
    }

    public void run() {
        try {
            handShake();
            String json;
            while ((json = in.readLine()) != null) {
                Message msg = JsonUtil.fromJson(json);

                switch (Objects.requireNonNull(msg).getType()) {
                    case CHAT -> server.broadcast(msg);
                    case USER_LEAVE -> {
                        return;
                    }
                    default -> send(Message.error("Неизвестный тип сообщения"));
                }
            }
        } catch (IOException e) {
            log.info("Соединение с {} оборвалось: {}", nickname, e.getMessage());
        } finally {
            close();
        }
    }

    private void handShake() throws IOException {
        Message first = readMessage();

        String desiredNick = first.getSender();

        if (server.registerNickname(desiredNick)) {
            nickname = desiredNick;
            send(Message.nickAccepted(nickname));
            server.addClient(this);
            server.sendExistingUsers(this);
            server.broadcast(Message.userJoin(nickname));
        } else {
            send(Message.nickRejected("Ник '" + desiredNick + "' уже занят. Попробуйте другой"));
        }
    }

    private Message readMessage() throws IOException {
        return JsonUtil.fromJson(in.readLine());
    }

    public void send(Message msg) {
        out.println(JsonUtil.toJson(msg));
    }

    public void close() {

        GracefulCloser.closeQuietly(socket);

        if (nickname != null) {
            server.broadcast(Message.userLeave(nickname));
            server.unregisterNickname(nickname);
            server.removeClient(this);
        }
    }

    public String getNickname() {
        return nickname;
    }
}
