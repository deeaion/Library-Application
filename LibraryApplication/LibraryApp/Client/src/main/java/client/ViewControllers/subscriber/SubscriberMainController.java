package client.ViewControllers.subscriber;

import client.RestCommunication.ClientWebSocket;
import client.RestCommunication.services.ClientService;
import common.model.BookInfo;
import common.model.CredentialsDTO;
import common.model.Enums.BookType;
import common.model.Enums.Genre;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SubscriberMainController {
    private Stage stage;
    private CredentialsDTO credentials;
    private ClientWebSocket clientWebSocket;
    private ClientService clientService;
    public void setSubscriber(Stage stage, CredentialsDTO credentials, ClientWebSocket clientWebSocket) {
        this.stage = stage;
        this.credentials = credentials;
        this.txtNameOfUser.setText(credentials.getUsername());
        this.clientWebSocket = clientWebSocket;
        this.clientService = new ClientService();
        loadTopBooksCategories();
        filterScrollPane.setVisible(false);
    }
    @FXML
    public void handleSearch(MouseEvent event) {
        String searchContent = searchBar.getText();
        CompletableFuture.runAsync(() -> {
            try {
                List<BookInfo> books = clientService.searchBooks(searchContent);
                Platform.runLater(() -> displayMoreBooks(books));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox VBoxMain;

    @FXML
    private Button btnLogOut;

    @FXML
    private ImageView btnSearchAuthor;

    @FXML
    private ImageView btnSearchCategory;

    @FXML
    private ImageView btnSearchGenre;

    @FXML
    private ImageView btnSearchOrigin;

    @FXML
    private ImageView btnSearchTitle;

    @FXML
    private ImageView dropDownBtn;

    @FXML
    private TextField searchBar;

    @FXML
    private ImageView searchBtn;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtCountry;

    @FXML
    private TextField txtGenre;

    @FXML
    private Label txtNameOfUser;

    @FXML
    private Label txtNumberOfItems;

    @FXML
    private TextField txtTitle;
    @FXML
    private ScrollPane filterScrollPane;
    @FXML
    public void handleDropDown(MouseEvent event) {
        if (filterScrollPane.isVisible()) {
            filterScrollPane.setVisible(false);
        } else {
            filterScrollPane.setVisible(true);
        }


    }

    @FXML
    public void handleFilterByCategory(MouseEvent event) {
        List<String> filters=new ArrayList<>();
        List<String > values=new ArrayList<>();
        if (!txtCategory.getText().isEmpty()) {
            filters.add("category");
            values.add(txtCategory.getText());
        }
        if (!txtAuthor.getText().isEmpty()) {
            filters.add("author");
            values.add(txtAuthor.getText());
        }
        if (!txtCountry.getText().isEmpty()) {
            filters.add("country");
            values.add(txtCountry.getText());
        }
        if (!txtGenre.getText().isEmpty()) {
            filters.add("genre");
            values.add(txtGenre.getText());
        }
        if (!txtTitle.getText().isEmpty()) {
            filters.add("title");
            values.add(txtTitle.getText());
        }
        CompletableFuture.runAsync(() -> {
            try {
                List<BookInfo> books = clientService.filterBooksByCriteria(filters, values);
                Platform.runLater(() -> displayMoreBooks(books));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    @FXML
    public void handleFilteredSearch(MouseEvent event) {
        filterScrollPane.setVisible(false);

    }

    @FXML
    public void handleLogOut(ActionEvent event) {
stage.close();
clientService.logout();
        stage.close();

    }
    private void loadTopBooksCategories() {
        CompletableFuture.runAsync(() -> {
            try {
                Map<BookType, List<BookInfo>> topBooksCategories = clientService.getTopBooksCategories();
                Platform.runLater(() -> displayTopBooksCategories(topBooksCategories));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void displayTopBooksCategories(Map<BookType, List<BookInfo>> topBooksCategories) {
        VBoxMain.getChildren().clear();
        List<String> genres =topBooksCategories.keySet().stream().map(BookType::toString).toList();

        genres.forEach(genre -> {
            VBox genreBox = new VBox();
            genreBox.setPrefHeight(291);
            genreBox.setPrefWidth(854);

            Label genreLabel = new Label(genre);
            genreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color:  #fddcb5");
            genreLabel.setPrefWidth(904);
            genreLabel.setPrefHeight(59);

            genreBox.getChildren().add(genreLabel);

            HBox hBox= new HBox();
            hBox.setPrefHeight(245);
            hBox.setPrefWidth(827);

            List<BookInfo> books = topBooksCategories.get(BookType.valueOf(genre)  );

            if (books.isEmpty()) {
                Label noBooksLabel = new Label("None at the moment");
                noBooksLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
               genreBox.getChildren().add(noBooksLabel);
            } else {
                books.stream().limit(3).forEach(book -> {
                    VBox bookBox = new VBox();
                    bookBox.setPrefHeight(245);
                    bookBox.setPrefWidth(200);

                    ImageView bookImage = new ImageView(new Image(book.getImg()));
                    bookImage.setFitWidth(158);
                    bookImage.setFitHeight(200);

                    Label bookTitle = new Label(book.getTitle());
                    bookTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
                    bookTitle.setFont(javafx.scene.text.Font.font("Caviar Dreams", 14));

                    Label bookAuthor = new Label(book.getAuthor());

                    bookBox.getChildren().addAll(bookImage, bookTitle, bookAuthor);
                    hBox.getChildren().add(bookBox);
                });

                if (books.size() > 3) {
                    Button viewMoreButton = new Button("View More");
                    viewMoreButton.setOnAction(e -> loadMoreBooks(BookType.valueOf(genre)));
                    hBox.getChildren().add(viewMoreButton);
                }


            }
                    genreBox.getChildren().add(hBox);
                    VBoxMain.getChildren().add(genreBox);
        }

        );

    }

    private void loadMoreBooks(BookType genre) {
        CompletableFuture.runAsync(() -> {
            try {
                List<BookInfo> books = clientService.filterBooksByCriteria(List.of("type"), List.of(genre.toString()));
                Platform.runLater(() -> displayMoreBooks(books));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void displayMoreBooks(List<BookInfo> books) {
        VBoxMain.getChildren().clear();
       for (int i=0;i<books.size();i+=4){
           HBox hBox= new HBox();
           hBox.setPrefHeight(245);
           hBox.setPrefWidth(827);
           for (int j=i;j<Math.min(i+3,books.size());j++){
               VBox bookBox = new VBox();
               bookBox.setPrefHeight(245);
               bookBox.setPrefWidth(200);

               ImageView bookImage = new ImageView(new Image(books.get(j).getImg()));
               bookImage.setFitWidth(158);
               bookImage.setFitHeight(200);

               Label bookTitle = new Label(books.get(j).getTitle());
               bookTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
               bookTitle.setFont(javafx.scene.text.Font.font("Caviar Dreams", 14));

               Label bookAuthor = new Label(books.get(j).getAuthor());

               bookBox.getChildren().addAll(bookImage, bookTitle, bookAuthor);
               hBox.getChildren().add(bookBox);
           }
           VBoxMain.getChildren().add(hBox);
       }
    }
}
