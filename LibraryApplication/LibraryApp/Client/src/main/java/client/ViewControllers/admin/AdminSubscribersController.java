package client.ViewControllers.admin;

import client.RestCommunication.services.AdminService;
import client.RestCommunication.services.LibrarianClientService;
import client.RestCommunication.webSocket.WebSocketMessageListener;
import client.RestCommunication.webSocket.admin.AdminNotificationManager;
import client.ViewControllers.MessageAlert;
import common.model.CredentialsDTO;
import common.model.Subscriber;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminSubscribersController implements WebSocketMessageListener {

    @FXML
    private Button btnDelete;

    @FXML
    private TableColumn<Subscriber, String> colCPN;

    @FXML
    private TableColumn<Subscriber, String> colCod;

    @FXML
    private TableColumn<Subscriber, String> colDateSub;

    @FXML
    private TableColumn<Subscriber, String> colEmail;

    @FXML
    private TableColumn<Subscriber, String> colFName;

    @FXML
    private TableColumn<Subscriber, String> colLName;

    @FXML
    private TableColumn<Subscriber, String> colUsr;

    @FXML
    private VBox detailsVBox;

    @FXML
    private TableView<Subscriber> tblSubscribers;

    @FXML
    private Label txtAddress;

    @FXML
    private Label txtGender;

    @FXML
    private Label txtPhone;
    private ObservableList<Subscriber> subscriberObservableList= FXCollections.observableArrayList();
    private void setTable()

    {
        colCPN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        colCod.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCredentials().getUsername()));
        colDateSub.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOfSubscription().toString()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCredentials().getEmail()));
        colFName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        colLName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        colUsr.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCredentials().getUsername()));
        tblSubscribers.setItems(subscriberObservableList);
        tblSubscribers.setOnMouseClicked(event -> {
            setSubscriberSelected();
            detailsVBox.setVisible(true);

        });
    }
    private void refreshTable()
    {
        subscriberObservableList.clear();
        try {
            subscriberObservableList.addAll(service.getAllSubscribers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteSubscriber(ActionEvent event) {
        String username = tblSubscribers.getSelectionModel().getSelectedItem().getCredentials().getUsername();
        Platform.runLater(() -> {
            try {
                service.deleteSubscriber(username);
                subscriberObservableList.remove(tblSubscribers.getSelectionModel().getSelectedItem());

            } catch (IOException e) {
                System.out.println("Error deleting subscriber");
            }
        });

    }
    private void setSubscriberSelected()
    {
        tblSubscribers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Subscriber subscriber = tblSubscribers.getSelectionModel().getSelectedItem();
                txtAddress.setText(subscriber.getAddress());
                txtGender.setText(subscriber.getGender());
                txtPhone.setText(subscriber.getPhone());
            }
        });
    }

    private Stage stage;
    private CredentialsDTO credentials;
    private AdminService service;


    public void setContext(Stage stage, CredentialsDTO credentials, AdminService adminClientService) {
        this.stage = stage;
        this.credentials = credentials;
        this.service=adminClientService;
        AdminNotificationManager.getInstance().addListener(this);
        setTable();
        refreshTable();
    }

    @Override
    public void onMessageReceived(String message) {
        Platform.runLater(() -> {
            refreshTable();
        });
    }
}
