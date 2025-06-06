package ru.shift.server;

public class ChatServerMain {
    public static void main(String[] args) {
        ChatServer server = new ChatServer(ServerConfig.get());

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop, "shutdown-hook"));
        server.start();
    }
}
