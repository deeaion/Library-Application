package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.BookInfo;
import server.model.BasketItem;
import server.model.Enums.BookType;
import server.model.Enums.Genre;
import server.service.IServiceClient;
import server.service.restHelping.BasketItemDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private IServiceClient serviceClient;


    @PostMapping("/finish-order")
    public ResponseEntity<Void> finishOrder(@RequestParam String username) {
        try
        {
            serviceClient.finishOrder(username);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationData data) {
        serviceClient.register(data.username, data.password, data.conf_password, data.email, data.firstName, data.lastName, data.cpn, data.address, data.phone, data.birthday, data.gender);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/basketSize/{username}")
    public int numberOfItemsInBasket(@PathVariable String username)
    {
        int nrOfItems = serviceClient.numberOfItemsInBasket(username);
        return nrOfItems;
    }
    @GetMapping("/stock/{id}")
    public ResponseEntity<Integer> getNrOfItemsInStock(@PathVariable Long id) {
        int nrOfItemsInStock = serviceClient.getNrOfItemsInStock(id);
        return ResponseEntity.ok(nrOfItemsInStock);
    }

    @GetMapping("/basket/quantity")
    public ResponseEntity<Integer> getQuantityOfBookInBasket(
            @RequestParam Long bookInfoId,
            @RequestParam String username) {
        BookInfo bookInfo = new BookInfo(); // You need to fetch BookInfo object by id
        bookInfo.setId(bookInfoId);
        int quantity = serviceClient.getQuantityOfBookInBasket(bookInfo, username);
        return ResponseEntity.ok(quantity);
    }

    @GetMapping("/basket/{username}")
    public ResponseEntity<List<BasketItemDTO>> getBooksInBasket(@PathVariable String username) {
        List<BasketItemDTO> booksInBasket = serviceClient.getBasketItems(username);
        return ResponseEntity.ok(booksInBasket);
    }
    @GetMapping("/top-books-categories")
    public ResponseEntity<Map<BookType, List<BookInfo>>> getTopBooksCategories() {
        return ResponseEntity.ok(serviceClient.getTopBooksCategories());
    }

    @PostMapping("/filter-books")
    public List<BookInfo> filterBooksByCriteria(@RequestBody FilterRequest filterRequest) {
        return serviceClient.filterBooksBYCriteria(filterRequest.getCriterias(), filterRequest.getValues());
    }


    @GetMapping("/search-books")
    public ResponseEntity<List<BookInfo>> searchBooks(@RequestParam String searchContent) {
        return ResponseEntity.ok(serviceClient.searchBooks(searchContent));
    }

    @GetMapping("/basket-items")
    public ResponseEntity<List<BasketItemDTO>> getBasketItems(@RequestParam String username) {
        return ResponseEntity.ok(serviceClient.getBasketItems(username));
    }

    @GetMapping("/top-books-category")
    public ResponseEntity<List<BookInfo>> getTopBooksForCategory(@RequestParam BookType bookType) {
        return ResponseEntity.ok(serviceClient.getTopBooksForCategory(bookType));
    }

    @GetMapping("/books-genre")
    public ResponseEntity<List<BookInfo>> getBooksForGenre(@RequestParam Genre genre) {
        return ResponseEntity.ok(serviceClient.getBooksForGenre(genre));
    }

    @GetMapping("/books-type")
    public ResponseEntity<List<BookInfo>> getBooksForType(@RequestParam BookType bookType) {
        return ResponseEntity.ok(serviceClient.getBooksForType(bookType));
    }

    @PostMapping("/add-book-to-basket")
    public ResponseEntity<Void> addBookToBasket(@RequestBody BookInfo book, @RequestParam int nrOfCopies, @RequestParam String username) {
        serviceClient.addBookToBasket(book, nrOfCopies, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove-book-from-basket")
    public ResponseEntity<Void> removeBookFromBasket(@RequestBody BasketItemDTO book,  @RequestParam String username) {
        serviceClient.removeBookFromBasket(book, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-book-quantity")
    public ResponseEntity<Void> updateBookQuantity(@RequestBody BasketItemDTO book, @RequestParam int quantity, @RequestParam String username) {
        serviceClient.updateBookQuantity(book, quantity, username);
        return ResponseEntity.ok().build();
    }

    private static class RegistrationData {
        public String username;
        public String password;
        public String conf_password;
        public String email;
        public String firstName;
        public String lastName;
        public String cpn;
        public String address;
        public String phone;
        public String birthday;
        public String gender;
    }
}
