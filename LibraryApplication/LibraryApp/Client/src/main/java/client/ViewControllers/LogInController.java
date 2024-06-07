package client.ViewControllers;

import client.ViewControllers.admin.AdminMainController;
import client.ViewControllers.librarian.LibrarianMainController;
import client.ViewControllers.subscriber.SubscriberMainController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.model.CredentialsDTO;
import common.model.restHelping.LogInRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kong.unirest.Unirest;

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
    private String URI_WebSocket = "ws://localhost:8080/websocket-endpoint";
    private String URI_REST = "http://localhost:55555/api/common/login";
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
    CredentialsDTO credentials;
    @FXML
    void handleLogIn(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        Unirest.post(URI_REST)
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
    private void openLibrarianView() {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/librarian/librarianMain-view.fxml"));
        Parent root= null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LibrarianMainController controller = fxmlLoader.<LibrarianMainController>getController();
        controller.setLibrarian(stage, credentials);
        System.out.println(credentials);
        stage.setTitle("Librarian");
        stage.setScene(new Scene(root ));
        stage.show();
    }
    private void openMainView() {
        if(credentials.getType().equals("librarian")) {
            openLibrarianView();
        } if(credentials.getType().equals("subscriber")) {
            openUserView();
        }
        if (credentials.getType().equals("admin")) {
            openAdminView();
        }


    }

    private void openAdminView() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/admin/adminMain-view.fxml"));
        Parent root= null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
           MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
            return;

        }
        AdminMainController controller = fxmlLoader.<AdminMainController>getController();
        controller.setAdmin(stage, credentials);
        stage.setTitle("Admin");
        stage.setScene(new Scene(root ));
        stage.show();

    }

    private void openUserView() {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/subscriber/subscriberMain-view.fxml"));
        Parent root= null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SubscriberMainController controller = fxmlLoader.<SubscriberMainController>getController();
        controller.setSubscriber(stage, credentials);
        stage.setTitle("Subscriber");
        stage.setScene(new Scene(root ));
        stage.show();

    }

    @FXML
    void handleSignUp(ActionEvent event) {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/signUp-view.fxml"));
        Parent root= null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SignUpController controller = fxmlLoader.<SignUpController>getController();
        controller.setSignUp(stage);
        stage.setTitle("Sign Up");
        stage.setScene(new Scene(root ));
        stage.show();

    }

    public void setLogIn(Stage primaryStage) {
        this.stage = primaryStage;
    }
}
