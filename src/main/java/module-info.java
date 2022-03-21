module chat {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.unsupported;
    exports chat to javafx.graphics;
    opens chat to javafx.fxml;
}