package client.ViewControllers.librarian;

import client.RestCommunication.services.LibrarianClientService;
import client.RestCommunication.webSocket.WebSocketMessageListener;
import client.RestCommunication.webSocket.librarian.LibrarianNotificationManager;
import client.ViewControllers.MessageAlert;
import common.model.Book;
import common.model.CredentialsDTO;
import common.restCommon.BasketItemDTO;
import common.restCommon.BookDTO;
import common.restCommon.FinishRentalRequest;
import common.restCommon.RentalDTO;
import common.restCommon.SubscriberDTO;
import common.model.Enums.StateOfRental;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LibrarianShowDetailController implements WebSocketMessageListener {

    @FXML
    private VBox boxRentalBooks;

    @FXML
    private Button btnFinishRental;

    @FXML
    private Label dateStartedAt;

    @FXML
    private Label txtEndAt;

    private SubscriberDTO subscriberDTO;

    private ObservableList<RentalDTO> nonRetrievedRentalsOL = FXCollections.observableArrayList();
    private ObservableList<RentalDTO> retrievedRentalsOL = FXCollections.observableArrayList();
    private ObservableList<BasketItemDTO> basketItemsOL = FXCollections.observableArrayList();

    private LibrarianClientService service;

    @FXML
    private AnchorPane listPane;

    @FXML
    private ComboBox<String> listType;

    private ListView<RentalDTO> rentalListView;
    private ListView<BasketItemDTO> basketListView;
    private CredentialsDTO credentials;

    @FXML
    void changeListType(ActionEvent event) {
        listPane.getChildren().clear();
        String selectedType = listType.getValue();
        if (selectedType.equals("Current Rentals")) {
            rentalListView = new ListView<>(nonRetrievedRentalsOL);
            rentalListView.setOnMouseClicked(this::handleShowRentals);
            rentalListView.setCellFactory(param -> new ListCell<RentalDTO>() {
                @Override
                protected void updateItem(RentalDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            });
            listPane.getChildren().add(rentalListView);
        } else if (selectedType.equals("Previous Rentals")) {
            rentalListView = new ListView<>(retrievedRentalsOL);
            rentalListView.setOnMouseClicked(this::handleShowPrevRentals);
            rentalListView.setCellFactory(param -> new ListCell<RentalDTO>() {
                @Override
                protected void updateItem(RentalDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            });
            listPane.getChildren().add(rentalListView);
        } else if (selectedType.equals("Borrowing Cart")) {
            basketListView = new ListView<>(basketItemsOL);
            basketListView.setOnMouseClicked(this::handleShowCart);
            basketListView.setCellFactory(param -> new ListCell<BasketItemDTO>() {
                @Override
                protected void updateItem(BasketItemDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            });
            listPane.getChildren().add(basketListView);
        }
    }

    @FXML
    public void initialize() {
        listType.getItems().addAll("Current Rentals", "Previous Rentals", "Borrowing Cart");
        listType.setValue("Current Rentals");
        setFirst();
    }

    private void setLists() {
        Platform.runLater(() -> {
            try {
                basketItemsOL.setAll(service.getSubscriberBasket(subscriberDTO.getCredentials().getUsername()));
                nonRetrievedRentalsOL.setAll(service.getSubscriberCurrentRentals(subscriberDTO.getCredentials().getUsername()));
                retrievedRentalsOL.setAll(service.getSubscriberRentalsHistory(subscriberDTO.getCredentials().getUsername()));
            } catch (Exception e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
                System.out.println(e.getMessage());
                return;
            }
        });
    }

    private void setHBoxesRentals(List<Book> books, boolean isModifiable) {
        boxRentalBooks.getChildren().clear();
        for (Book book : books) {
            HBox hBox = createRentalHBox(book, isModifiable);
            boxRentalBooks.getChildren().add(hBox);
        }
    }

    private HBox createRentalHBox(Book book, boolean isModifiable) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(79.0);
        hBox.setPrefWidth(396.0);
        hBox.setStyle("-fx-background-color: #ffebd0; -fx-border-color: #7d5769; -fx-border-width: 3;");

        VBox vBox = new VBox();
        vBox.setPrefHeight(72.0);
        vBox.setPrefWidth(290.0);

        Label titleLabel = new Label("Title: " + book.getBookInfo().getTitle());
        titleLabel.setFont(new javafx.scene.text.Font("Agency FB", 23.0));
        titleLabel.setPrefHeight(22.0);
        titleLabel.setPrefWidth(212.0);

        Label isbnLabel = new Label("ISBC: " + book.getBookInfo().getIsbn());
        isbnLabel.setFont(new javafx.scene.text.Font("Agency FB", 18.0));
        isbnLabel.setPrefHeight(22.0);
        isbnLabel.setPrefWidth(212.0);

        Label uniqueCodeLabel = new Label("UNIC: " + book.getUniqueCode());
        uniqueCodeLabel.setFont(new javafx.scene.text.Font("Agency FB", 18.0));
        uniqueCodeLabel.setPrefHeight(22.0);
        uniqueCodeLabel.setPrefWidth(212.0);

        vBox.getChildren().addAll(titleLabel, isbnLabel, uniqueCodeLabel);

        MenuButton menuButton = new MenuButton("Mark as");
        menuButton.setPrefHeight(33.0);
        menuButton.setPrefWidth(133.0);

        MenuItem retrievedItem = new MenuItem("Mark as Retrieved");
        MenuItem lostItem = new MenuItem("Mark as Lost");

        retrievedItem.setOnAction(event -> book.setState(StateOfRental.NOT_RENTED));
        lostItem.setOnAction(event -> book.setState(StateOfRental.LOST));

        menuButton.getItems().addAll(retrievedItem, lostItem);
        menuButton.setDisable(!isModifiable);

        hBox.getChildren().addAll(vBox, menuButton);

        return hBox;
    }

    private void setHBoxesBasket(List<BasketItemDTO> items) {
        boxRentalBooks.getChildren().clear();
        for (BasketItemDTO item : items) {
            HBox hBox = createBasketHBox(item);
            boxRentalBooks.getChildren().add(hBox);
        }
    }

    private HBox createBasketHBox(BasketItemDTO item) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(79.0);
        hBox.setPrefWidth(396.0);
        hBox.setStyle("-fx-background-color: #ffebd0; -fx-border-color: #7d5769; -fx-border-width: 3;");

        VBox vBox = new VBox();
        vBox.setPrefHeight(72.0);
        vBox.setPrefWidth(290.0);

        Label titleLabel = new Label("Title: " + item.getBook().getTitle());
        titleLabel.setFont(new javafx.scene.text.Font("Agency FB", 23.0));
        titleLabel.setPrefHeight(22.0);
        titleLabel.setPrefWidth(212.0);

        Label isbnLabel = new Label("ISBC: " + item.getBook().getIsbn());
        isbnLabel.setFont(new javafx.scene.text.Font("Agency FB", 18.0));
        isbnLabel.setPrefHeight(22.0);
        isbnLabel.setPrefWidth(212.0);

        Label quantityLabel = new Label("Quantity: " + item.getQuantity());
        quantityLabel.setFont(new javafx.scene.text.Font("Agency FB", 18.0));
        quantityLabel.setPrefHeight(22.0);
        quantityLabel.setPrefWidth(212.0);

        vBox.getChildren().addAll(titleLabel, isbnLabel, quantityLabel);

        hBox.getChildren().add(vBox);

        return hBox;
    }

    @FXML
    void handleFinishRental(ActionEvent event) {
        try {
            List<BookDTO> rentalsToUpdate = new ArrayList<>();
            for (RentalDTO rental : nonRetrievedRentalsOL) {
                for (Book book : rental.getBooks()) {
                    if (book.getState() == null) {
                        book.setState(StateOfRental.LOST);
                    }
                    BookDTO bookDTO = new BookDTO(book.getId(), null, book.getUniqueCode(), book.getState());
                    rentalsToUpdate.add(bookDTO);
                }
            }
            RentalDTO rental = rentalListView.getSelectionModel().getSelectedItem();
            FinishRentalRequest request = new FinishRentalRequest(rentalsToUpdate,rental.getId() , credentials.getId());
            service.finishRetrievingRental(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContext(SubscriberDTO subscriberDTO, LibrarianClientService service, BorderPane border, CredentialsDTO credentials) throws Exception {
        this.subscriberDTO = subscriberDTO;
        this.service = service;
        setFirst();
        setLists();
        changeListType(null); // Set initial list view
        this.credentials = credentials;
        System.out.println(credentials.getId());
        LibrarianNotificationManager.getInstance().addListener(this);
    }

    public void handleShowPrevRentals(MouseEvent mouseEvent) {
        RentalDTO selectedRental = rentalListView.getSelectionModel().getSelectedItem();
        if (selectedRental != null) {
            setHBoxesRentals(selectedRental.getBooks(), false);
        }
    }

    public void handleShowCart(MouseEvent mouseEvent) {
        setHBoxesBasket(basketItemsOL);
    }

    private void setFirst() {
        listType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> changeListType(null));
    }

    public void handleShowRentals(MouseEvent mouseEvent) {
        RentalDTO selectedRental = rentalListView.getSelectionModel().getSelectedItem();
        if (selectedRental != null) {
            setHBoxesRentals(selectedRental.getBooks(), true);
        }
    }

    @Override
    public void onMessageReceived(String message) {
        setLists();

    }
}
