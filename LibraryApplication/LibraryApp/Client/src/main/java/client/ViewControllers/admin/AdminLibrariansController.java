package client.ViewControllers.admin;

import client.RestCommunication.services.AdminService;
import client.RestCommunication.webSocket.WebSocketMessageListener;
import client.RestCommunication.webSocket.admin.AdminNotificationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.model.Credentials;
import common.model.CredentialsDTO;
import common.model.Librarian;
import common.restCommon.LibrarianDTO;
import common.restCommon.PersonDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public class AdminLibrariansController implements WebSocketMessageListener {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModify;

    @FXML
    private TableColumn<LibrarianDTO, String> colCPN;

    @FXML
    private TableColumn<LibrarianDTO, String>  colCod;

    @FXML
    private TableColumn<LibrarianDTO, String>  colEmail;

    @FXML
    private TableColumn<LibrarianDTO, String>  colEmployment;

    @FXML
    private TableColumn<LibrarianDTO, String>  colFName;

    @FXML
    private TableColumn<LibrarianDTO, String>  colLName;

    @FXML
    private TableColumn<LibrarianDTO, String>  colUsername;

    @FXML
    private TableView<LibrarianDTO> tblLibrarian;

    @FXML
    private TextField txtAddress;

    @FXML
    private HBox txtBDay;

    @FXML
    private TextField txtCPN;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFName;

    @FXML
    private TextField txtGender;

    @FXML
    private TextField txtLName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtUsername;

    @FXML
    void handleAddLibrarian(ActionEvent event) {

        try {
            Credentials credentials = new Credentials(txtUsername.getText(), "", txtEmail.getText(), "");
            CredentialsDTO credentialsDTO = new CredentialsDTO(credentials, "admin");
            credentialsDTO.setId(0l);
            String date = txtBirthday.getValue().toString();
            PersonDTO personDTO = new PersonDTO(txtFName.getText(), txtLName.getText(),date, txtGender.getText(), txtAddress.getText(), txtPhone.getText(), txtCPN.getText());
            LibrarianDTO librarianDTO = new LibrarianDTO(credentialsDTO, LocalDateTime.now().toString(), personDTO, "");

            // Debug: Print the librarianDTO as JSON
            String jsonPayload = new ObjectMapper().writeValueAsString(librarianDTO);
            System.out.println("Sending JSON payload: " + jsonPayload);


                try {
                    service.addLibrarian(librarianDTO);
                   refreshTable();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    ObservableList<LibrarianDTO> librarianObservableList= FXCollections.observableArrayList();
    private void setTable()
    {
        //set up the columns in the table
        colUsername.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getCredentials().getUsername()));
        colEmail.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getCredentials().getEmail()));
        colFName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getPerson().getFirstName()));
        colLName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getPerson().getLastName()));
        colCPN.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getPerson().getCPN()));
        colEmployment.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getDateOfHiring().toString()));
        colCod.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getUniqueCode()));
        tblLibrarian.setItems(librarianObservableList);


    }
    private void refreshTable() throws IOException {

                    try {
                        librarianObservableList.setAll(service.getAllLibrarians());
                        tblLibrarian.setItems(librarianObservableList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
    @FXML
    void handleDeleteLibrarian(ActionEvent event) {

        String username=tblLibrarian.getSelectionModel().getSelectedItem().getCredentials().getUsername();

                    try {
                        service.deleteLibrarian(username); refreshTable();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



    @FXML
    DatePicker txtBirthday;
    @FXML
    void handleModifyLibrarian(ActionEvent event) {
        String username=tblLibrarian.getSelectionModel().getSelectedItem().getCredentials().getUsername();
         Credentials credentials=new Credentials(txtUsername.getText(),"",txtEmail.getText(),"");
         credentials.setId(0l);
        CredentialsDTO credentialsDTO=new CredentialsDTO(credentials,"admin");
        credentialsDTO.setId(credentials.getId());
        //iau din datapicker data
        String date=txtBirthday.getValue().toString();
        PersonDTO personDTO=new PersonDTO(txtFName.getText(),txtLName.getText(), date,txtGender.getText(),txtAddress.getText(),txtPhone.getText(),txtCPN.getText());
        LibrarianDTO librarianDTO=new LibrarianDTO(credentialsDTO,LocalDateTime.now().toString(),personDTO,"");

                    try {
                        service.updateLibrarian(username,librarianDTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


    }

    private Stage stage;
    private CredentialsDTO credentials;
    private AdminService service;


    public void setContext(Stage stage, CredentialsDTO credentials, AdminService adminClientService) {
        this.stage = stage;
        this.credentials = credentials;
        this.service=adminClientService;
        setTable();
        try {
            refreshTable();
        } catch (IOException e) {
            System.out.println("Error refreshing table");
        }
        try {
            refreshTable();
        } catch (IOException e) {
            System.out.println("Could not refresh!");
            System.out.println(e.getMessage());
        }
        AdminNotificationManager.getInstance().addListener(this);
    }

    @Override
    public void onMessageReceived(String message) {
        Platform.runLater(
                ()->{
                    try {
                        refreshTable();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
