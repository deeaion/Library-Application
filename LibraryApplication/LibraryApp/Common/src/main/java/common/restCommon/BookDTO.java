package common.restCommon;

import common.model.Enums.StateOfRental;

public class BookDTO {
    private Long id;
    private BookInfoDTO bookInfo;
    private String uniqueCode;
    private StateOfRental state;

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", bookInfo=" + bookInfo +
                ", uniqueCode='" + uniqueCode + '\'' +
                ", state=" + state +
                '}';
    }
// Getters and setters

    public BookDTO() {
    }

    public BookDTO(Long id, BookInfoDTO bookInfo, String uniqueCode, StateOfRental state) {
        this.id = id;
        this.bookInfo = bookInfo;
        this.uniqueCode = uniqueCode;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookInfoDTO getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfoDTO bookInfo) {
        this.bookInfo = bookInfo;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public StateOfRental getState() {
        return state;
    }

    public void setState(StateOfRental state) {
        this.state = state;
    }
}
