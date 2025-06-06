module shift.client {
    requires shift.common;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    requires static lombok;

    opens ru.shift.client to javafx.graphics;
    opens ru.shift.client.view to javafx.fxml;
}