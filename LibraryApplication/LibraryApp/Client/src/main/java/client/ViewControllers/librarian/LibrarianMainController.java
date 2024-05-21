package client.ViewControllers.librarian;

import client.RestCommunication.ClientWebSocket;
import common.model.CredentialsDTO;
import javafx.stage.Stage;

public class LibrarianMainController {
    private Stage stage;
    private CredentialsDTO credentials;
    public void setLibrarian(Stage stage, CredentialsDTO credentials) {
        this.stage = stage;
        this.credentials = credentials;
    }
}
