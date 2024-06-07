package client.RestCommunication.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.model.Book;
import common.model.BookInfo;
import common.model.Subscriber;
import common.restCommon.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class AdminService {
    private static final String BASE_URL = "http://localhost:55555/api/admin";
    private final CloseableHttpClient httpClient= HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public AdminService() {
    }

    public List<Subscriber> getAllSubscribers() throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/subscribers");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String response = EntityUtils.toString(client.execute(request).getEntity());
            List<SubscriberDTO> subscriberDTOS=objectMapper.readValue(response, new TypeReference<List<SubscriberDTO>>() {});
            List<Subscriber> subscribers=SubscriberDTO.toSubscribers(subscriberDTOS);
            return subscribers;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void deleteSubscriber(String username) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + "/subscribers/" + username);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(request);
        }
    }

// In AdminService.java

    public LibrarianDTO addLibrarian(LibrarianDTO librarianDTO) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/librarians");
        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(librarianDTO));
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpResponse response = client.execute(request);
            String responseString = EntityUtils.toString(response.getEntity());

            // Debug: Print the response from the server
            System.out.println("Response from server: " + responseString);

            return objectMapper.readValue(responseString, LibrarianDTO.class);
        }
    }


    public void deleteLibrarian(String username) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + "/librarians/" + username);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(request);
        }
    }

    public void updateLibrarian(String username, LibrarianDTO librarianDTO) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/librarians/" + username);
        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(librarianDTO));
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(request);
        }
    }

    public List<LibrarianDTO> getAllLibrarians() throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/librarians");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String response = EntityUtils.toString(client.execute(request).getEntity());
            return objectMapper.readValue(response, new TypeReference<List<LibrarianDTO>>() {});
        }
    }

    public List<BookInfo> getAllBooks() throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/books");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String response = EntityUtils.toString(client.execute(request).getEntity());
            return objectMapper.readValue(response, new TypeReference<List<BookInfo>>() {});
        }
    }

    public void addBookInfo(BookInfoDTO bookInfoDTO) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/books");
        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(bookInfoDTO));
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(request);
        }
    }

    public void deleteBookInfo(String isbc) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + "/books/" + isbc);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(request);
        }
    }

    /*
        @GetMapping("/books/{id_bookInfo}")
    public ResponseEntity<?> getBookUnits(@PathVariable Long id_bookInfo) {
        List<Book> books = serviceAdmin.findBookUnits(id_bookInfo);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
     */
    public List<Book> getBookUnits(Long id_bookInfo) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/books/" + id_bookInfo);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String response = EntityUtils.toString(client.execute(request).getEntity());
            return objectMapper.readValue(response, new TypeReference<List<Book>>() {});
        }
    }

    public void updateBookInfo(String isbc, BookInfoDTO bookInfoDTO) throws IOException {
        HttpPut request = new HttpPut(BASE_URL + "/books/" + isbc); // Use HttpPut instead of HttpPost
        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(bookInfoDTO));
        System.out.println("Sending: " + objectMapper.writeValueAsString(bookInfoDTO)); // Log the JSON payload
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(request);
        }
    }

}
