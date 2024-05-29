package server.service.restHelping;



import server.model.CredentialsDTO;

import java.util.List;

public class SubscriberDTO {
    private CredentialsDTO credentials;
    public SubscriberDTO() {
    }

    private String dateOfSubscription;

    private String uniqueCode;
    private long id;

    private List<BasketItemDTO> shoppingBasket;
    private List<RentalDTO> currentRentals;

    public List<RentalDTO> getCurrentRentals() {
        return currentRentals;
    }

    public void setCurrentRentals(List<RentalDTO> currentRentals) {
        this.currentRentals = currentRentals;
    }

    public List<RentalDTO> getPreviousRentals() {
        return previousRentals;
    }

    public void setPreviousRentals(List<RentalDTO> previousRentals) {
        this.previousRentals = previousRentals;
    }

    private List<RentalDTO> previousRentals;

    public SubscriberDTO(Long id,CredentialsDTO credentials, String dateOfSubscription, String uniqueCode, List<BasketItemDTO> shoppingBasket) {
        this.credentials = credentials;
        this.dateOfSubscription = dateOfSubscription;
        this.uniqueCode = uniqueCode;
        this.shoppingBasket = shoppingBasket;
        this.id=id;
    }

    public CredentialsDTO getCredentials() {
        return credentials;
    }

    public void setCredentials(CredentialsDTO credentials) {
        this.credentials = credentials;
    }

    public String getDateOfSubscription() {
        return dateOfSubscription;
    }

    public void setDateOfSubscription(String dateOfSubscription) {
        this.dateOfSubscription = dateOfSubscription;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public List<BasketItemDTO> getShoppingBasket() {
        return shoppingBasket;
    }

    public void setShoppingBasket(List<BasketItemDTO> shoppingBasket) {
        this.shoppingBasket = shoppingBasket;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
