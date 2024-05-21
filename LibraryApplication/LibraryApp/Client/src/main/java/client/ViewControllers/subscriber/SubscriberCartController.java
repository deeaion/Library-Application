package client.ViewControllers.subscriber;

import common.model.BasketItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class SubscriberCartController {

    @FXML
    private TableView<?> basketTable;

    @FXML
    private AnchorPane btnBack;

    @FXML
    private TableColumn<BasketItem, HBox> colProdus;

    @FXML
    private TableColumn<BasketItem,HBox> colUnits;


    @FXML
    void handleFinishRent(MouseEvent event) {

    }

    @FXML
    void handleGoMain(MouseEvent event) {

    }

    @FXML
    void modifyNrOfUnits(ActionEvent event) {

    }

}
