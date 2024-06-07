package client.RestCommunication.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.restCommon.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LibrarianClientService {
    private static final String BASE_URL = "http://localhost:55555/api/librarian";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LibrarianClientService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public SubscriberDTO getSubscriber(String username) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/subscribers/" + username))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println(response.body());

            return objectMapper.readValue(response.body(), SubscriberDTO.class);
        } else {
            throw new RuntimeException("Failed to fetch subscriber");
        }
    }

    public void finishRetrievingRental(FinishRentalRequest request) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/rentals-finish"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            System.out.println(response.body());

            throw new RuntimeException("Failed to finish retrieving rental");
        }
    }

    public List<RentalDTO> getSubscriberCurrentRentals(String username) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/rentals-current/" + username))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println(response.body());
            return objectMapper.readValue(response.body(), new TypeReference<List<RentalDTO>>() {});
        } else {
            throw new RuntimeException("Failed to fetch current rentals");
        }
    }

    public List<RentalDTO> getSubscriberRentalsHistory(String username) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/rentals-history?username=" + username))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println(response.body());

            return objectMapper.readValue(response.body(), new TypeReference<List<RentalDTO>>() {});
        } else {
            throw new RuntimeException("Failed to fetch rental history, status code: " + response.statusCode());
        }
    }

    public List<BasketItemDTO> getSubscriberBasket(String username) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/basket/" + username))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println(response.body());


            return objectMapper.readValue(response.body(), new TypeReference<List<BasketItemDTO>>() {});
        } else {
            throw new RuntimeException("Failed to fetch basket items");
        }
    }
}
