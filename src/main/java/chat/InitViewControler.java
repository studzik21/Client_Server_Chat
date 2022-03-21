package chat;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.EventObject;

public class InitViewControler {
    Stage stage;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button joinButton;

    @FXML
    private TextField nameInputTextField;

    public void initialize(){


        joinButton.setOnAction(actionEvent -> {
            String name = nameInputTextField.getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatView.fxml"));
            Parent root=null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatViewControler controler = loader.getController();
            controler.setStage(stage);
            try {
                controler.setName(name);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            anchorPane.getChildren().setAll(root);

        });
       // nameInputTextField.setText("Nick");
    }
    public void setStage(Stage stage) {
        this.stage = stage;

    }
    public InitViewControler getController(){
        return this;
    }


}