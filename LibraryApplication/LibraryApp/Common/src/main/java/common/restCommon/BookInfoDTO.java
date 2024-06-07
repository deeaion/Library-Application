package common.restCommon;

public class BookInfoDTO {
    private String title;
    private String isbc;
    private String author;
    private String publisher;
    private String language;

    @Override
    public String toString() {
        return "BookInfoDTO{" +
                "title='" + title + '\'' +
                ", isbc='" + isbc + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", language='" + language + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", copies=" + copies +
                ", id=" + id +
                ", genre='" + genre + '\'' +
                '}';
    }

    private String type;
    private String description;
    private String image;
    private int copies;
    private long id;

    private String genre;

    public BookInfoDTO() {
    }

    public BookInfoDTO(String title, String isbc, String author, String publisher, String language, String type, String description, String image, int copies, String genre) {
        this.title = title;
        this.isbc = isbc;
        this.author = author;
        this.publisher = publisher;
        this.language = language;
        this.type = type;
        this.description = description;
        this.image = image;
        this.copies = copies;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbc() {
        return isbc;
    }

    public void setIsbc(String isbc) {
        this.isbc = isbc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
// Getters and setters
}
