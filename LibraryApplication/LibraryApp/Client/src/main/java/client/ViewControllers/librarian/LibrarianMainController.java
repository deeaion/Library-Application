package client.ViewControllers.librarian;

import client.RestCommunication.services.LibrarianClientService;
import client.RestCommunication.webSocket.librarian.LibrarianWebSocketManager;
import common.model.CredentialsDTO;
import common.restCommon.SubscriberDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrarianMainController {

    @FXML
    private AnchorPane borderCenter;

    @FXML
    private BorderPane borderLibrarian;

    @FXML
    private ImageView btnBack;

    @FXML
    private Button btnSearchSubscriber;

    @FXML
    private Button handleLogOut;

    @FXML
    private TextField txtSubscriberContext;

    @FXML
    private Label txtUserName;

    private LibrarianClientService librarianClientService;
    private Stage stage;
    private CredentialsDTO credentials;

    public LibrarianMainController() {
        this.librarianClientService = new LibrarianClientService();
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String subscriberContext = txtSubscriberContext.getText();
        try {
            SubscriberDTO subscriberDTO = librarianClientService.getSubscriber(subscriberContext);
            if (subscriberDTO != null) {
                loadMenu(subscriberDTO);
            } else {

                txtUserName.setText("Subscriber not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            txtUserName.setText("Error during search!");
        }
    }

    public void setLibrarian(Stage stage, CredentialsDTO credentials) {
        this.stage = stage;
        this.credentials = credentials;
        System.out.println("LibrarianMainController setLibrarian");
        LibrarianWebSocketManager.getInstance().connect();
        txtUserName.setText(credentials.getUsername());

    }
    private void loadMenu(SubscriberDTO subscriberDTO) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/librarian/librarianMenuSubscriber-view.fxml"));
            Node node = loader.load();
            LibrarianMenuSubscriberController controller = loader.getController();
            controller.setContext(stage, credentials, subscriberDTO,borderLibrarian);
            borderLibrarian.setCenter(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
