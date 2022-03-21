package chat;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    Stage stage;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader anchorPane = new FXMLLoader(getClass().getResource("/initView.fxml"));
        Parent root = anchorPane.load();
        Scene scene = new Scene(root);
        InitViewControler controler = anchorPane.getController();
        controler.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Client view");
        stage.show();

    }

}
