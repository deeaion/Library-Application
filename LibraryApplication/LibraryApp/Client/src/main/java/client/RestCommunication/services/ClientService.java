package client.RestCommunication.services;

import client.ViewControllers.librarian.LibrarianMainController;
import client.ViewControllers.subscriber.SubscriberViewBookController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.model.BasketItem;
import common.model.BookInfo;
import common.model.CredentialsDTO;
import common.model.Enums.BookType;
import common.model.Enums.Genre;
import common.restCommon.BasketItemDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientService {
    private static final String BASE_URL = "http://localhost:55555/api/client";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ClientService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Map<BookType, List<BookInfo>> getTopBooksCategories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/top-books-categories"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                throw new RuntimeException("Failed to fetch top books categories");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch top books categories", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookInfo> filterBooksByCriteria(List<String> criteriaa, List<String> values) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/filter-books"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(Map.of("criterias", criteriaa, "values", values))))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<BookInfo>>() {});
            } else {
                throw new RuntimeException("Failed to fetch books by criteria: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch books by criteria", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookInfo> searchBooks(String search) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/search-books?searchContent=" + search))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                throw new RuntimeException("Failed to fetch books by criteria");
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException("Failed to search books", e);
        }
    }

    public void logout() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/logout"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to log out");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to log out", e);
        }
    }

    public void finishOrder(String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/finish-order?username=" + username))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to finish order: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to finish order", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNrOfItemsInStock(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/stock/" + id))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Integer.class);
            } else {
                throw new RuntimeException("Failed to fetch number of items in stock: " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException("Failed to fetch number of items in stock", e);
        }
    }

    public int getQuantityOfBookInBasket(Long bookInfoId, String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/basket/quantity?bookInfoId=" + bookInfoId + "&username=" + username))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Integer.class);
            } else {
                throw new RuntimeException("Failed to fetch quantity of book in basket: " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException("Failed to fetch quantity of book in basket", e);
        }
    }

    public int updateBasketItemQuantity(BasketItemDTO basketItemDTO, int quantity, String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/update-basket-item-quantity?quantity=" + quantity + "&username=" + username))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(basketItemDTO)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Integer.class);
            } else {
                throw new RuntimeException("Failed to update basket item quantity: " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException("Failed to update basket item quantity", e);
        }
    }

    public int getBooksInBasket(String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/basket/" + username))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Integer.class);
            } else {
                throw new RuntimeException("Failed to fetch number of books in basket: " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException("Failed to fetch number of books in basket", e);
        }
    }

    public void addBookToBasket(BookInfo bookInfo, int nrOfUnits, String username) {
        try {
            // Convert the BookInfo object to JSON
            String bookJson = objectMapper.writeValueAsString(bookInfo);

            // Create the request URI
            URI uri = new URI(BASE_URL + "/add-book-to-basket?nrOfCopies=" + nrOfUnits + "&username=" + username);

            // Build the POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bookJson))
                    .build();

            // Send the request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to add book to basket: " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException("Failed to add book to basket", e);
        }
    }

    public int getNrOfItemsInBasket(String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/basketSize/" + username))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch number of items in basket", e);
        }
    }

    public List<BasketItemDTO> getBasketItems(CredentialsDTO credentials) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/basket/" + credentials.getUsername()))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<BasketItemDTO>>() {});
            } else {
                throw new RuntimeException("Failed to fetch basket items: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch basket items", e);
        }
    }

    public void removeBookFromBasket(BasketItemDTO book, String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/remove-book-from-basket?username=" + username))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(book)))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to remove book from basket: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to remove book from basket", e);
        }
    }

    public void modifyBasketItem(BasketItemDTO selectedItem, String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/update-book-quantity?quantity=" + selectedItem.getQuantity() + "&username=" + username))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(selectedItem)))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to modify basket item: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to modify basket item", e);
        }
    }

    public void finishRent(CredentialsDTO credentials) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/finish-order?username=" + credentials.getUsername()))
                    .POST(HttpRequest.BodyPublishers.noBody()) // Change to POST method
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to finish rent: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to finish rent", e);
        }
    }
}
