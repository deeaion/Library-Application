package client.ViewControllers.subscriber;

import client.RestCommunication.ClientWebSocket;
import client.RestCommunication.WebSocketManager;
import client.RestCommunication.WebSocketMessageListener;
import client.RestCommunication.services.ClientService;
import client.ViewControllers.MessageAlert;
import common.model.BookInfo;
import common.model.CredentialsDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SubscriberViewBookController implements WebSocketMessageListener {
    private BookInfo bookInfo;
    private ClientService clientService;
    private CredentialsDTO credentials;

    public void setBook(BookInfo book, ClientService clientService, CredentialsDTO credentials) {
        this.bookInfo = book;
        this.clientService = clientService;
        this.credentials = credentials;
        setComponents();
        setBookInfo();
        WebSocketManager.getInstance().addListener(this);
//        subscribeToNotifications();''

    }
//    private void subscribeToNotifications() {
//        clientWebSocket.getSession().subscribe("/topic/notifications", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return String.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                Platform.runLater(() -> updateSpinnerMax());
//            }
//        });
//    }

    private void updateSpinnerMax() {
        try {
            int countInStock = clientService.getNrOfItemsInStock(bookInfo.getId());
            int countInCart = clientService.getQuantityOfBookInBasket(bookInfo.getId(), credentials.getUsername());
            int max = countInStock - countInCart;
            nrOfUnitsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0));
        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }
    private void setBookInfo() {
        txtTitle.setText(bookInfo.getTitle());
        txtAuthor.setText(bookInfo.getAuthor());
        txtCategory.setText(bookInfo.getType().toString());
        txtDescription.setText(bookInfo.getDescription());
        txtGenre.setText(bookInfo.getGenre().toString());
        txtLanguage.setText(bookInfo.getLanguage());
        txtPublisher.setText(bookInfo.getPublisher());
        txtISBC.getChildren().add(new Label(bookInfo.getIsbn()));
    }

    private void setComponents() {
        //setting spinner
        //imi trebuie sa aflu cate carti sunt in stoc pentru a seta maximul spinnerului
        //imi trebuie sa aflu cate carti sunt in cos pentru a seta maximul spinnerului
        //imi trebuie sa aflu daca cartea este in cos pentru a seta valoarea spinnerului
        int countInStock = 0;
        try {
            countInStock = clientService.getNrOfItemsInStock(bookInfo.getId());
        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
        }
        int countInCart = 0;
        try {
            countInCart = clientService.getQuantityOfBookInBasket(bookInfo.getId(), credentials.getUsername());
        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
        }
        int max = countInStock - countInCart;
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0);
        nrOfUnitsSpinner.setValueFactory(valueFactory);
        //setting image
        ImageView imageView = new ImageView(bookInfo.getImg());
        imgImage.setImage(imageView.getImage());


    }

    @FXML
    private Button btnAddToCart;

    @FXML
    private ImageView imgImage;

    @FXML
    private Spinner<Integer> nrOfUnitsSpinner;

    @FXML
    private Label txtAuthor;

    @FXML
    private Label txtCategory;

    @FXML
    private Label txtDescription;

    @FXML
    private Label txtGenre;

    @FXML
    private HBox txtISBC;

    @FXML
    private Label txtLanguage;

    @FXML
    private Label txtPublisher;

    @FXML
    private Label txtTitle;

    @FXML
    void handleAddToCart(ActionEvent event) {
        int nrOfUnits = nrOfUnitsSpinner.getValue();
        Platform.runLater(()->
        {
            try {
                clientService.addBookToBasket(bookInfo, nrOfUnits, credentials.getUsername());
            } catch (Exception e) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());


            }
        });


}

    @Override
    public void onMessageReceived(String message) {

        Platform.runLater(this::updateSpinnerMax);

    }
}
