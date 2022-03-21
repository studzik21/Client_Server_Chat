package chat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;

public class ChatViewControler {
    Thread udpThread;
    Thread tcpThread;
    Thread mulicastThread;
    Socket socket = null;
    DatagramSocket datagramSocket=null;
    MulticastSocket multicastSocket;
    String nick;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button mulitcastButton;

    @FXML
    private Button sendButton;

    @FXML
    private Button tcpButton;

    @FXML
    private TextArea textArea;

    @FXML
    private Button udpButton;


    @FXML
    private TextArea userInputTextArea;

    Stage stage;
    String name;
    Client client;
    public void initialize() throws IOException, InterruptedException {

        sendButton.setOnAction(actionEvent -> {
            client.send(userInputTextArea.getText());
            userInputTextArea.clear();
        });
        udpButton.setOnAction(actionEvent -> {
            client.changeMode(CommunicationType.UDP);
        });
        tcpButton.setOnAction(actionEvent -> {
            client.changeMode(CommunicationType.TCP);
        });
        mulitcastButton.setOnAction(actionEvent -> {
            client.changeMode(CommunicationType.MULTICAST);
        });
    }
    public void setStage(Stage stage){

        this.stage=stage;

        this.stage.setOnCloseRequest(windowEvent -> {
            client.endClient(false);
        });
    }
    public void setName(String name) throws IOException, InterruptedException {
        this.name= name;
        this.client = new Client(textArea,name);
        client.main();
    }


}
