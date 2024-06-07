package client;

import client.RestCommunication.webSocket.client.ClientWebSocketManager;
import client.ViewControllers.LogInController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends Application  {
    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/logIn-view.fxml"));
        Parent root=fxmlLoader.load();
        LogInController controller = fxmlLoader.<LogInController>getController();
        controller.setLogIn(primaryStage);
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root ));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}
