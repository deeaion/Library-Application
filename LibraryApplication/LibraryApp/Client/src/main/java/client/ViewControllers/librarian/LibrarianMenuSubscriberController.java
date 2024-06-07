package client.ViewControllers.librarian;

import client.RestCommunication.services.LibrarianClientService;
import common.model.CredentialsDTO;
import common.restCommon.SubscriberDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrarianMenuSubscriberController {

    @FXML
    private HBox btnShowInfo;

    @FXML
    private HBox btnShowRentals;

    private Stage stage;
    private CredentialsDTO credentials;
    private SubscriberDTO subscriberDTO;
    private LibrarianClientService librarianClientService;
    private BorderPane parentBorderPane;

    public void setContext(Stage stage, CredentialsDTO credentials, SubscriberDTO subscriberDTO, BorderPane borderLibrarian) {
        this.stage = stage;
        this.credentials = credentials;
        this.subscriberDTO = subscriberDTO;
        this.librarianClientService = new LibrarianClientService();
        this.parentBorderPane= borderLibrarian;
    }

    @FXML
    void handleShowInfo() {
        loadDetailView("/views/librarian/librarianShowSubscriber-view.fxml");
    }

    @FXML
    void handleShowRentals() {
        loadDetailView("/views/librarian/librarianShowDetail-view.fxml");
    }

    private void loadDetailView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof LibrarianShowSubscriberController) {
                ((LibrarianShowSubscriberController) controller).setContext(subscriberDTO, librarianClientService,parentBorderPane,credentials);
            } else if (controller instanceof LibrarianShowDetailController) {
                ((LibrarianShowDetailController) controller).setContext(subscriberDTO, librarianClientService,parentBorderPane,credentials);
            }

            parentBorderPane = (BorderPane) stage.getScene().getRoot();
            parentBorderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
