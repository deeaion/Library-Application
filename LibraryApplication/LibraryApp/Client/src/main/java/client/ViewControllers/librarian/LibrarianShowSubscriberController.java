package client.ViewControllers.librarian;

import client.RestCommunication.services.LibrarianClientService;
import common.model.CredentialsDTO;
import common.restCommon.SubscriberDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class LibrarianShowSubscriberController {

    @FXML
    private Label txtAddress;

    @FXML
    private Label txtBDay;

    @FXML
    private Label txtCPN;

    @FXML
    private Label txtDate;

    @FXML
    private Label txtEmail;

    @FXML
    private Label txtFName;

    @FXML
    private Label txtID;

    @FXML
    private Label txtLName;

    @FXML
    private Label txtPhone;

    @FXML
    private Label txtUsername;

    private SubscriberDTO subscriberDTO;
    private CredentialsDTO credentials;

    public void setContext(SubscriberDTO subscriberDTO, LibrarianClientService librarianClientService, BorderPane border, CredentialsDTO credentials) {
        this.subscriberDTO = subscriberDTO;
        txtID.setText(String.valueOf(subscriberDTO.getId()));
        txtUsername.setText(subscriberDTO.getCredentials().getUsername());
        txtEmail.setText(subscriberDTO.getCredentials().getEmail());
        txtFName.setText(subscriberDTO.getFirstName());
        txtLName.setText(subscriberDTO.getLastName());
        txtBDay.setText(subscriberDTO.getBirthDate());
        txtAddress.setText(subscriberDTO.getAddress());
        txtCPN.setText(subscriberDTO.getCpn());
        txtPhone.setText(subscriberDTO.getPhoneNumber());
        txtDate.setText(subscriberDTO.getDateOfSubscription());
        this.credentials = credentials;

    }
}
