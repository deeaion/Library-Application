package client.ViewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SignUpController {
    private String URI_WebSocket = "ws://localhost:8080/websocket-endpoint";
    private String URI_REST = "http://localhost:55555/api/client/register";
    private Stage stage;

    public void setSignUp(Stage stage) {
        this.stage = stage;
        int i=16;
        sendRegistrationRequest("username", "password", "password", "email", "firstName", "lastName", "cpn", "address", "phone","2024-05-19 00:00:00","gender");
    }

//    private void initWebSocket() {
//        if (clientWebSocket == null) {
//            try {
//                clientWebSocket = new ClientWebSocket(new URI(URI_WebSocket), this::handleMessage);
//                clientWebSocket.connect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnConfirm;

    @FXML
    private DatePicker pickerBirthday;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCPN;

    @FXML
    private PasswordField txtConfPas1;

    @FXML
    private TextField txtFName;

    @FXML
    private TextField txtFName1;

    @FXML
    private TextField txtLName;

    @FXML
    private PasswordField txtPas1;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtUsername1;

    @FXML
    private TextField txtGender;

    @FXML
    void handleCancel(ActionEvent event) {
        switchToLogIn();
    }

    private void switchToLogIn() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/logIn-view.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogInController logInController = loader.getController();
        logInController.setLogIn(stage);
        stage.setScene(new Scene(root));
    }

    @FXML
    void handleSave(ActionEvent event) {
        String username = txtUsername1.getText();
        String password = txtPas1.getText();
        String confPassword = txtConfPas1.getText();
        String email = txtFName1.getText();
        String firstName = txtFName.getText();
        String lastName = txtLName.getText();
        String cpn = txtCPN.getText();
        LocalDate birthday = pickerBirthday.getValue();

        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String gender = txtGender.getText();

        if (username.isEmpty() || password.isEmpty() || confPassword.isEmpty() || email.isEmpty() ||
                firstName.isEmpty() || lastName.isEmpty() || cpn.isEmpty() || address.isEmpty() ||
                phone.isEmpty() || birthday == null) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Please fill in all the fields");
            return;
        }

        if (!password.equals(confPassword)) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Passwords do not match");
            return;
        }
        LocalDateTime birthdayTimestamp = birthday.atStartOfDay(); // Converts LocalDate to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedBirthday = birthdayTimestamp.format(formatter);
        sendRegistrationRequest(username, password, confPassword, email, firstName, lastName, cpn, address, phone, formattedBirthday, gender);

    }

    private void sendRegistrationRequest(String username, String password, String confPassword, String email, String firstName, String lastName, String cpn, String address, String phone, String birthday, String gender) {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String requestBody = objectMapper.writeValueAsString(new RegistrationData(username, password, confPassword, email, firstName, lastName, cpn, address, phone, birthday, gender));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URI_REST))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .exceptionally(e -> {
                        MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Could not register");
                        return null;
                    });
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Success", "Registration successful");

        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Could not register");
            return;
        }
    }

    private void handleMessage(String s) {
        // Handle WebSocket messages
    }

    private static class RegistrationData {
        public String username;
        public String password;
        public String conf_password;
        public String email;
        public String firstName;
        public String lastName;
        public String cpn;
        public String address;
        public String phone;
        public String birthday;
        public String gender;

        public RegistrationData(String username, String password, String conf_password, String email, String firstName, String lastName, String cpn, String address, String phone, String birthday, String gender) {
            this.username = username;
            this.password = password;
            this.conf_password = conf_password;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.cpn = cpn;
            this.address = address;
            this.phone = phone;
            this.birthday = birthday;
            this.gender = gender;
        }
    }
}
