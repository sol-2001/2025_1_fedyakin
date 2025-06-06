package ru.shift.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ServerConfig cfg;

    private final Set<String> nicknames = ConcurrentHashMap.newKeySet();
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public ChatServer(ServerConfig cfg) {
        this.cfg = cfg;
    }

    public void start() {
        try (ServerSocket socket = new ServerSocket(cfg.getPort())) {
            log.info("Chat-server started on port {}", cfg.getPort());
            this.serverSocket = socket;

            while (!socket.isClosed()) {
                Socket clientSocket = socket.accept();
                ClientHandler handler = new ClientHandler(this, clientSocket);
                pool.submit(handler);
            }
        } catch (IOException e) {
            log.error("Ошибка I/O, завершение работы", e);
        } finally {
            stop();
        }
    }

    public void stop() {
        ClientsSnapshot snapshot = new ClientsSnapshot(clients);
        snapshot.closeAll();

        GracefulCloser.close(serverSocket, pool);
    }

    boolean registerNickname(String nick) {
        return nicknames.add(nick);
    }

    void unregisterNickname(String nick) {
        nicknames.remove(nick);
    }

    void addClient(ClientHandler ch) {
        clients.add(ch);
    }

    void removeClient(ClientHandler ch) {
        clients.remove(ch);
    }

    void broadcast(Message msg) {
        for (ClientHandler ch : clients) {
            ch.send(msg);
        }
    }

    void sendExistingUsers(ClientHandler newUser) {
        for (ClientHandler ch : clients) {
            if (ch != newUser) {
                newUser.send(Message.userJoin(ch.getNickname()));
            }
        }
    }

    private record ClientsSnapshot(CopyOnWriteArrayList<ClientHandler> list) {
        void closeAll() {
            for (ClientHandler ch : list) {
                ch.close();
            }
        }
    }
}

