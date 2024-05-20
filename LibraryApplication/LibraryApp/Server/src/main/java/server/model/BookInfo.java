package server.model;

import jakarta.persistence.*;
import server.model.Enums.BookType;
import server.model.Enums.Genre;

import java.util.Objects;

@Entity
@Table(name="bookinformation")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "bookinfo_id"))
})
public class BookInfo extends Identifiable<Long> {
    @Column(name = "title")
    private String title;
    @Column(name = "isbn")

    private String isbn;
    @Column(name = "publisher")
    private String publisher;
    @Column(name="author")
    private String author;
    @Column(name="language")
    private String language;
    @Column(name="description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "main_genre")
    private Genre genre;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BookType type;
    @Column(name="img_url")
    private String img;
    @Column(name = "nr_of_copies")
    private int nr_of_copies;

    public BookInfo( String title, String isbn, String publisher, String author, String language, String description, Genre genre, BookType type, String img, int nr_of_copies) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.author = author;
        this.language = language;
        this.description = description;
        this.genre = genre;
        this.type = type;
        this.img = img;
        this.nr_of_copies = nr_of_copies;
    }

    public BookInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNr_of_copies() {
        return nr_of_copies;
    }

    public void setNr_of_copies(int nr_of_copies) {
        this.nr_of_copies = nr_of_copies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookInfo bookInfo)) return false;
        return getNr_of_copies() == bookInfo.getNr_of_copies() && Objects.equals(getTitle(), bookInfo.getTitle()) && Objects.equals(getIsbn(), bookInfo.getIsbn()) && Objects.equals(getPublisher(), bookInfo.getPublisher()) && Objects.equals(getAuthor(), bookInfo.getAuthor()) && Objects.equals(getLanguage(), bookInfo.getLanguage()) && Objects.equals(getDescription(), bookInfo.getDescription()) && getGenre() == bookInfo.getGenre() && getType() == bookInfo.getType() && Objects.equals(getImg(), bookInfo.getImg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getIsbn(), getPublisher(), getAuthor(), getLanguage(), getDescription(), getGenre(), getType(), getImg(), getNr_of_copies());
    }
}