package client.ViewControllers.admin;

import client.RestCommunication.services.AdminService;
import client.RestCommunication.services.LibrarianClientService;
import client.ViewControllers.MessageAlert;
import common.model.Book;
import common.model.BookInfo;
import common.model.CredentialsDTO;
import common.model.Enums.BookType;
import common.model.Enums.Genre;
import common.restCommon.BookInfoDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdminBooksController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModify;

    @FXML
    private TableColumn<BookInfo, String> colAuthor;

    @FXML
    private TableColumn<BookInfo, String> colCopies;

    @FXML
    private TableColumn<BookInfo, String> colDesc;

    @FXML
    private TableColumn<BookInfo,Genre> colGenre;

    @FXML
    private TableColumn<BookInfo, String> colISBC;

    @FXML
    private TableColumn<BookInfo, String> colImg;

    @FXML
    private TableColumn<BookInfo, String> colLanguage;

    @FXML
    private TableColumn<BookInfo, String> colPublisher;

    @FXML
    private TableColumn<BookInfo, String> colTitle;

    @FXML
    private TableColumn<BookInfo, BookType> colType;

    @FXML
    private ComboBox<Genre> comboGenre;

    @FXML
    private ComboBox<BookType> comboType;

    @FXML
    private ListView<Book> listBooks;

    @FXML
    private Label nrOfUnits;

    @FXML
    private Spinner<Integer> spinCopies;

    @FXML
    private TableView<BookInfo> tblBook;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtDesc;

    @FXML
    private TextField txtISBC;

    @FXML
    private TextField txtLanguage;

    @FXML
    private TextField txtPublisher;

    @FXML
    private TextField txtTitle;
    private ObservableList<BookInfo> bookInfoObservableList= FXCollections.observableArrayList();
    private ObservableList<Book> bookObservableList= FXCollections.observableArrayList();

    // instantiate table
    private void setTable()
    {
        colAuthor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        colCopies.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNr_of_copies())));
        colDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colGenre.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getGenre()));
        colISBC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));
        colImg.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImg()));
        colLanguage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLanguage()));
        colPublisher.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublisher()));
        colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        colType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        tblBook.setItems(bookInfoObservableList);
        //set on select action
        tblBook.setOnMouseClicked(event -> {
            this.bookInfoSelected=tblBook.getSelectionModel().getSelectedItem();
            setFields();

        });
    }

    private void setFields() {
        //set all fields to current selected
        BookInfo bookInfo = tblBook.getSelectionModel().getSelectedItem();
        if(bookInfo!=null)
        {
            txtTitle.setText(bookInfo.getTitle());
            txtISBC.setText(bookInfo.getIsbn());
            txtAuthor.setText(bookInfo.getAuthor());
            txtPublisher.setText(bookInfo.getPublisher());
            txtLanguage.setText(bookInfo.getLanguage());
            txtDesc.setText(bookInfo.getDescription());
            txtImg.setText(bookInfo.getImg());
            spinCopies.getValueFactory().setValue(bookInfo.getNr_of_copies());
            comboGenre.setValue(bookInfo.getGenre());
            comboType.setValue(bookInfo.getType());
            //set the list
            refreshListBookInfo();

        }

    }

    private void refreshTable() throws IOException {
        List<BookInfo> bookInfos = service.getAllBooks();
        bookInfoObservableList.setAll(bookInfos);
    }

    private void refreshListBookInfo()
    {
        // well here i need to request from server for the current cell
        // selected in the table to get the books
        // and then refresh the list
        Platform.runLater(
                () -> {
                    BookInfo bookInfo = tblBook.getSelectionModel().getSelectedItem();
                    if(bookInfo!=null)
                    {
                        List<Book> books = null;
                        try {
                            books = service.getBookUnits(bookInfo.getId());
                        } catch (IOException e) {
                            System.out.println("Error getting book units");
                            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error getting book units");
                        }
                        bookObservableList.setAll(books);
                        nrOfUnits.setText(String.valueOf(books.size()));
                    }
                }
        );

    }

    private void setList()
    {
        listBooks.setItems(bookObservableList);
    }

    @FXML
    private TextField txtImg;
    @FXML
    void handleAddBook(ActionEvent event) {
        // get the values from the fields
        String title = txtTitle.getText();
        String isbc = txtISBC.getText();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        String language = txtLanguage.getText();
        String description = txtDesc.getText();
        String image = txtImg.getText();
        int copies = spinCopies.getValue();
        Genre genre = comboGenre.getValue();
        BookType type = comboType.getValue();
        // call the service to add the book
        Platform.runLater(
                () -> {
                    try {
                        BookInfoDTO bookInfoDTO =new BookInfoDTO(title,isbc,author,publisher,language,type.toString(),description,image,copies,genre.toString());
                        service.addBookInfo(bookInfoDTO);
                        // refresh the table
                        //convert to bookinfo
                        refreshTable();
                    } catch (IOException e) {
                        System.out.println("Error adding book");
                        MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error adding book");
                    }
                }
        );



    }
    private void setSpinner()
    {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        spinCopies.setValueFactory(valueFactory);
    }

    @FXML
    void handleClearFields(ActionEvent event) {
        txtTitle.clear();
        txtISBC.clear();
        txtAuthor.clear();
        txtPublisher.clear();
        txtLanguage.clear();
        txtDesc.clear();
        txtImg.clear();
        spinCopies.getValueFactory().setValue(0);
        comboGenre.setValue(null);
        comboType.setValue(null);
    }

    @FXML
    void handleDeleteBook(ActionEvent event) {
        // get the selected book
        BookInfo bookInfo = tblBook.getSelectionModel().getSelectedItem();
        if(bookInfo!=null)
        {
            // call the service to delete the book
            Platform.runLater(
                    () -> {
                        try {
                            service.deleteBookInfo(bookInfo.getIsbn());
                            // refresh the table
                            refreshTable();
                        } catch (IOException e) {
                            System.out.println("Error deleting book");
                            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error deleting book");
                        }
                    }
            );
        }

    }
    BookInfo bookInfoSelected;
    @FXML
    void handleModifyBook(ActionEvent event) {
        Platform.runLater(
                ()->{
                    BookInfo bookInfo = tblBook.getSelectionModel().getSelectedItem();
                    if(bookInfo!=null)
                    {
                        String title = txtTitle.getText();
                        String isbc = txtISBC.getText();
                        String author = txtAuthor.getText();
                        String publisher = txtPublisher.getText();
                        String language = txtLanguage.getText();
                        String description = txtDesc.getText();
                        String image = txtImg.getText();
                        int copies = spinCopies.getValue();
                        Genre genre = comboGenre.getValue();
                        BookType type = comboType.getValue();
                        BookInfoDTO bookInfoDTO = new BookInfoDTO(title,isbc,author,publisher,language,type.toString(),description,image,copies,genre.toString());
                        bookInfoDTO.setId(bookInfo.getId());
                        Platform.runLater(
                                ()->
                                {
                                    try {
                                        service.updateBookInfo(isbc,bookInfoDTO);
                                        refreshTable();
                                    } catch (IOException e) {
                                        System.out.println("Error updating book");
                                        MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error updating book");
                                    }
                                }
                        );


                    }
                }
        );

    }

    private Stage stage;
    private CredentialsDTO credentials;
    private AdminService service;

    private void setCombos()
    {
        comboGenre.getItems().setAll(Genre.values());
        comboType.getItems().setAll(BookType.values());
    }
    public void setContext(Stage stage, CredentialsDTO credentials, AdminService adminClientService) {
        this.stage = stage;
        this.credentials = credentials;
        this.service=adminClientService;
        try {
            refreshTable();
        } catch (IOException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error refreshing table");
            return;
        }
        setTable();
        setSpinner();
        setList();
        setCombos();
        refreshListBookInfo();
    }
}
