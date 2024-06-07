package client.ViewControllers.admin;

import client.RestCommunication.services.AdminService;
import client.RestCommunication.webSocket.admin.AdminNotificationManager;
import client.RestCommunication.webSocket.admin.AdminWebSocketManager;
import common.model.CredentialsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnLibrarian;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnManageBooks;

    @FXML
    private Button btnManageSubscribers;

    @FXML
    private AnchorPane center;

    private Stage stage;
    private CredentialsDTO credentials;

    @FXML
    void handleLogOut(ActionEvent event) {
        // Implement logout logic here
    }

    @FXML
    void handleManageBooks(ActionEvent event) {
        loadCenter("/views/admin/adminBooks-view.fxml");
    }

    @FXML
    void handleManageLibrarians(ActionEvent event) {
        loadCenter("/views/admin/adminLibrarians-view.fxml");
    }

    @FXML
    void handleManageSubscribers(ActionEvent event) {
        loadCenter("/views/admin/adminSubscribers-view.fxml");
    }
    private AdminService adminService;
    public void setAdmin(Stage stage, CredentialsDTO credentials) {
        this.stage = stage;
        this.credentials = credentials;
        this.adminService = new AdminService();
        AdminWebSocketManager.getInstance().connect();
    }

    private void loadCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();

            Object controller = loader.getController();

            if (controller instanceof AdminBooksController) {
                ((AdminBooksController) controller).setContext(stage, credentials,adminService);
            } else if (controller instanceof AdminLibrariansController) {
                ((AdminLibrariansController) controller).setContext(stage, credentials,adminService);
            } else if (controller instanceof AdminSubscribersController) {
                ((AdminSubscribersController) controller).setContext(stage, credentials,adminService);
            }

            borderPane.setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
