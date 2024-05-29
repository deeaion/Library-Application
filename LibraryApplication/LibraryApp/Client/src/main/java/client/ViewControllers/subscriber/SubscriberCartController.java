package client.ViewControllers.subscriber;

import client.RestCommunication.services.ClientService;
import client.ViewControllers.MessageAlert;
import common.model.BookInfo;
import common.model.CredentialsDTO;
import common.restCommon.BasketItemDTO;
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
    private TableView<BasketItemDTO> basketTable;

    @FXML
    private AnchorPane btnBack;

    @FXML
    private ListView<BasketItemDTO> basketList;

    private Stage stage;
    private CredentialsDTO credentials;
    private ClientService clientService;

    @FXML
    void handleFinishRent(MouseEvent event) {
        CompletableFuture.runAsync(() -> {
            try {
                clientService.finishRent(credentials);
                Platform.runLater(this::setItems);
            } catch (Exception e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
                return;
            }
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Success", "Rent finished successfully!");

        });
    }

    @FXML
    void handleGoMain(MouseEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/subscriber/subscriberMain-view.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
            return;
        }
        SubscriberMainController controller = fxmlLoader.getController();
        controller.setSubscriber(stage, credentials);
        stage.setTitle("Subscriber");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void modifyNrOfUnits(ActionEvent event) {
        BasketItemDTO selectedItem = basketList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectedItem.setQuantity(selectedItem.getQuantity());
            CompletableFuture.runAsync(() -> {
                try {
                    clientService.modifyBasketItem(selectedItem, credentials.getUsername());
                    Platform.runLater(this::setItems);
                } catch (Exception e) {
                    MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
                    return;
                }
            });
        }
    }

    public void setSubscriber(Stage stage, CredentialsDTO credentials, ClientService clientService) {
        this.stage = stage;
        this.credentials = credentials;
        this.clientService = clientService;
        setItems();
    }

    private void setItems() {
        basketList.getItems().clear();
        CompletableFuture.runAsync(() -> {
            try {
                List<BasketItemDTO> books = clientService.getBasketItems(credentials);
                Platform.runLater(() -> setList(books));
            } catch (Exception e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
                return;
            }
        });
    }

    private void setList(List<BasketItemDTO> books) {
        Platform.runLater(() -> {
            basketList.setItems(FXCollections.observableArrayList(books));
            basketList.setCellFactory(new Callback<ListView<BasketItemDTO>, ListCell<BasketItemDTO>>() {
                @Override
                public ListCell<BasketItemDTO> call(ListView<BasketItemDTO> listView) {
                    return new ListCell<BasketItemDTO>() {
                        @Override
                        protected void updateItem(BasketItemDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                BookInfo bookInfo = item.getBook();

                                Button button = new Button("X");
                                button.setOnAction(e -> handleRemoveBookFromBasket(item));

                                Label titleLabel = new Label(bookInfo.getTitle());
                                Label authorLabel = new Label(bookInfo.getAuthor());

                                VBox vBox = new VBox(titleLabel, authorLabel);
                                Spinner<Integer> spinner = new Spinner<>(1, 100, item.getQuantity());
                                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                                    if (item != null) {
                                        item.setQuantity(newValue);
                                        modifyNrOfUnits(new ActionEvent());
                                    }
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
        });
    }

    private void handleRemoveBookFromBasket(BasketItemDTO book) {
        CompletableFuture.runAsync(() -> {
            try {
                clientService.removeBookFromBasket(book, credentials.getUsername());
                Platform.runLater(this::setItems);
            } catch (Exception e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
                return;
            }
        });
    }
}
