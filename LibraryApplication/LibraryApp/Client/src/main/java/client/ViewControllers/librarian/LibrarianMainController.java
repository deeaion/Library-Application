package client.ViewControllers.librarian;

import client.RestCommunication.ClientWebSocket;
import common.model.CredentialsDTO;
import javafx.stage.Stage;

public class LibrarianMainController {
    private Stage stage;
    private CredentialsDTO credentials;
    private ClientWebSocket clientWebSocket;
    public void setLibrarian(Stage stage, CredentialsDTO credentials, ClientWebSocket clientWebSocket) {
        this.stage = stage;
        this.credentials = credentials;
        this.clientWebSocket = clientWebSocket;
    }
}
