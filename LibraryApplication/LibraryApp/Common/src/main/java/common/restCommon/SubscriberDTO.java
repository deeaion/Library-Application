package common.restCommon;

import common.model.CredentialsDTO;
import common.model.Subscriber;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SubscriberDTO {
    private CredentialsDTO credentials;
    private String dateOfSubscription;
    private String uniqueCode;
    private long id;
    private String cpn;
    private String gender;
    private String firstName;

    @Override
    public String toString() {
        return "SubscriberDTO{" +
                "credentials=" + credentials +
                ", dateOfSubscription='" + dateOfSubscription + '\'' +
                ", uniqueCode='" + uniqueCode + '\'' +
                ", id=" + id +
                ", cpn='" + cpn + '\'' +
                ", gender='" + gender + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    private String lastName;
    private String birthDate;
    private String address;
    private String phoneNumber;

    public SubscriberDTO() {
    }

    public SubscriberDTO(CredentialsDTO credentials, String dateOfSubscription, String uniqueCode, long id, String cpn, String firstName, String lastName, String birthDate, String address, String phoneNumber,
                         String gender ) {
        this.credentials = credentials;
        this.dateOfSubscription = dateOfSubscription;
        this.uniqueCode = uniqueCode;
        this.id = id;
        this.cpn = cpn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender=gender;
    }

    public static List<Subscriber> toSubscribers(List<SubscriberDTO> subscriberDTOS) {
        List<Subscriber> subscribers=new ArrayList<>();
        for (SubscriberDTO subscriberDTO:subscriberDTOS) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Subscriber subscriber=new Subscriber(subscriberDTO.uniqueCode,subscriberDTO.firstName,subscriberDTO.lastName, LocalDateTime.parse(subscriberDTO.birthDate,formatter),subscriberDTO.gender,subscriberDTO.address,subscriberDTO.phoneNumber,subscriberDTO.getCpn(), subscriberDTO.getCredentials(),LocalDateTime.parse(subscriberDTO.getDateOfSubscription(),formatter));
            subscriber.setId(subscriberDTO.getId());
            subscribers.add(subscriber);

        }
        return subscribers;
    }

    // Getters and Setters
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCpn() {
        return cpn;
    }

    public void setCpn(String cpn) {
        this.cpn = cpn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
