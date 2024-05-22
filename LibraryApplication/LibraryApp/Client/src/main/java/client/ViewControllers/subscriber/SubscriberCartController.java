package client.ViewControllers.subscriber;

import client.RestCommunication.services.ClientService;
import common.model.BasketItem;
import common.model.BookInfo;
import common.model.Credentials;
import common.model.CredentialsDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SubscriberCartController {

    @FXML
    private TableView<BasketItem> basketTable;


    @FXML
    private AnchorPane btnBack;


    @FXML
    private ListView<BasketItem> basketList;

    private Stage stage;
    private CredentialsDTO credentials;

    @FXML
    void handleFinishRent(MouseEvent event) {

    }

    @FXML
    void handleGoMain(MouseEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/subscriber/subscriberMain-view.fxml"));
        Parent root= null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SubscriberMainController controller = fxmlLoader.<SubscriberMainController>getController();
        controller.setSubscriber(stage, credentials);
        stage.setTitle("Subscriber");
        stage.setScene(new Scene(root ));
        stage.show();

    }

    @FXML
    void modifyNrOfUnits(ActionEvent event) {

    }
    private ClientService clientService ;

    public void setSubscriber(Stage stage, CredentialsDTO credentials,ClientService clientService) {
        this.stage = stage;
        this.credentials = credentials;
        this.clientService = clientService;

        setItems();
    }

    private void setItems() {
        basketList.getItems().clear();
        CompletableFuture.runAsync(() -> {
            try {

                List<BasketItem> books = clientService.getBasketItems(credentials);
                Platform.runLater(() -> setList(books));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
    ObservableList<BasketItem> listBasket= FXCollections.observableArrayList();
    private void setList(List<BasketItem> books) {
        basketList.setItems(FXCollections.observableArrayList(books));
        basketList.setCellFactory(new Callback<ListView<BasketItem>, ListCell<BasketItem>>() {
            @Override
            public ListCell<BasketItem> call(ListView<BasketItem> listView) {
                return new ListCell<BasketItem>() {
                    @Override
                    protected void updateItem(BasketItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            BookInfo bookInfo = item.getBook();

                            Button button = new Button("X");

                            button.setOnAction(e -> handleRemoveBookFromBasket(item.getBook()));
                            // Add action to button if needed

                            Label titleLabel = new Label(bookInfo.getTitle());
                            Label authorLabel = new Label(bookInfo.getAuthor());

                            VBox vBox = new VBox(titleLabel, authorLabel);
                            Spinner<Integer> spinner = new Spinner<>(1, 100, item.getQuantity());
                            spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                                item.setQuantity(newValue);
                                modifyNrOfUnits(new ActionEvent());
                            });

                            HBox hBox = new HBox(button, vBox, spinner);
                            setGraphic(hBox);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    private void handleRemoveBookFromBasket(BookInfo book) {
        CompletableFuture.runAsync(() -> {
            try {
                clientService.removeBookFromBasket(book, credentials.getUsername());
                setItems();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

