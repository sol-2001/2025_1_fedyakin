package ru.shift.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.shift.client.controller.ChatClientController;
import ru.shift.client.model.ChatClientConnection;
import ru.shift.client.model.ChatModel;
import ru.shift.client.model.IChatClientConnection;
import ru.shift.client.view.ChatView;

public class ChatClientApp extends Application {

    private static final String VIEW_FILE_PATH = "/chatView.fxml";
    private static final String TITLE = "Chat";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FILE_PATH));
        Scene scene = new Scene(loader.load());

        ChatModel model = new ChatModel();
        IChatClientConnection connection = new ChatClientConnection(model);
        ChatView chatView = loader.getController();
        ChatClientController controller = new ChatClientController(chatView, model, connection);

        chatView.setController(controller);
        chatView.setModel(model);
        chatView.setConnection(connection);
        chatView.setupBindings();

        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> controller.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
