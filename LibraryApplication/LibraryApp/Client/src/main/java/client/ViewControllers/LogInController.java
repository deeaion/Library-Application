package client.ViewControllers;

import client.RestCommunication.ClientWebSocket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.model.Credentials;
import common.model.CredentialsDTO;
import common.model.restHelping.LogInRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kong.unirest.Unirest;

import java.net.URI;
import java.net.URISyntaxException;

public class LogInController {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnLogIn;

    @FXML
    private Button btnSignUp;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;
    private ClientWebSocket clientWebSocket;
    private Stage stage;

    @FXML
    void handleCancel(ActionEvent event) {
        stage.close();
        //close program as well

    }
    @FXML
    public void initialize() {
//        try {
//            clientWebSocket = new ClientWebSocket(new URI("ws://localhost:8080/websocket-endpoint"), this::handleMessage);
//            clientWebSocket.connect();
//
//        } catch (URISyntaxException e) {
//            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Cannot connect to server");
//        }
    }

    private void handleMessage(String s) {
    }
    Credentials credentials;
    @FXML
    void handleLogIn(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        Unirest.post("http://localhost:55555/api/common/login")
                .header("Content-Type", "application/json")
                .body(new LogInRequest(username, password))
                .asStringAsync(response -> {
                    Platform.runLater(() -> {
                        if (response.getStatus() == 200) {
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                this.credentials = mapper.readValue(response.getBody(), CredentialsDTO.class);
                            } catch (JsonProcessingException e) {
                                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Cannot parse credentials");
                            }
                            System.out.println("Logged in");
                            openMainView();
                        } else {
                            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Invalid username or password");
                            System.out.println(response.getStatus());
                        }
                    });
                });

    }

    private void openMainView() {

    }

    @FXML
    void handleSignUp(ActionEvent event) {

    }

}
