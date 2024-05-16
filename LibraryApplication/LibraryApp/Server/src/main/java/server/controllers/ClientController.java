package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.model.BookInfo;
import server.model.BasketItem;
import server.model.Enums.BookType;
import server.model.Enums.Genre;
import server.service.IServiceClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private IServiceClient serviceClient;

    @PostMapping("/register")
    public void register(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String conf_password,
                         @RequestParam String email,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String cpn,
                         @RequestParam String address,
                         @RequestParam String phone,
                         @RequestParam String birthday,
                         @RequestParam String gender) {
        serviceClient.register(username, password, conf_password, email, firstName, lastName, cpn, address, phone, birthday, gender);
    }

    @GetMapping("/top-books-categories")
    public Map<Genre, List<BookInfo>> getTopBooksCategories() {
        return serviceClient.getTopBooksCategories();
    }

    @GetMapping("/filter-books")
    public List<BookInfo> filterBooksByCriteria(@RequestParam String criteria, @RequestParam String value) {
        return serviceClient.filterBooksBYCriteria(criteria, value);
    }

    @GetMapping("/search-books")
    public List<BookInfo> searchBooks(@RequestParam String searchContent) {
        return serviceClient.searchBooks(searchContent);
    }

    @GetMapping("/basket-items")
    public List<BasketItem> getBasketItems(@RequestParam String username) {
        return serviceClient.getBasketItems(username);
    }

    @GetMapping("/top-books-category")
    public List<BookInfo> getTopBooksForCategory(@RequestParam BookType bookType) {
        return serviceClient.getTopBooksForCategory(bookType);
    }

    @GetMapping("/books-genre")
    public List<BookInfo> getBooksForGenre(@RequestParam Genre genre) {
        return serviceClient.getBooksForGenre(genre);
    }

    @GetMapping("/books-type")
    public List<BookInfo> getBooksForType(@RequestParam BookType bookType) {
        return serviceClient.getBooksForType(bookType);
    }

    @PostMapping("/add-book-to-basket")
    public void addBookToBasket(@RequestBody BookInfo book, @RequestParam int nrOfCopies, @RequestParam String username) {
        serviceClient.addBookToBasket(book, nrOfCopies, username);
    }

    @PostMapping("/remove-book-from-basket")
    public void removeBookFromBasket(@RequestBody BookInfo book, @RequestParam String username) {
        serviceClient.removeBookFromBasket(book, username);
    }

    @PostMapping("/update-book-quantity")
    public void updateBookQuantity(@RequestBody BookInfo book, @RequestParam int quantity, @RequestParam String username) {
        serviceClient.updateBookQuantity(book, quantity, username);
    }
}
