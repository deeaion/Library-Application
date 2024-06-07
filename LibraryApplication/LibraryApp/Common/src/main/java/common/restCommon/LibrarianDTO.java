package common.restCommon;

import common.model.CredentialsDTO;


public class LibrarianDTO {
    private CredentialsDTO credentials;
    private String dateOfHiring;
    private PersonDTO person;
    private String uniqueCode;

    public LibrarianDTO() {
    }

    public LibrarianDTO(CredentialsDTO credentials, String dateOfHiring, PersonDTO person, String uniqueCode) {
        this.credentials = credentials;
        this.dateOfHiring = dateOfHiring;
        this.person = person;
        this.uniqueCode = uniqueCode;
    }
// Getters and setters

    @Override
    public String toString() {
        return "LibrarianDTO{" +
                "credentials=" + credentials +
                ", dateOfHiring=" + dateOfHiring +
                ", person=" + person +
                ", uniqueCode='" + uniqueCode + '\'' +
                '}';
    }

    public CredentialsDTO getCredentials() {
        return credentials;
    }

    public void setCredentials(CredentialsDTO credentials) {
        this.credentials = credentials;
    }

    public String getDateOfHiring() {
        return dateOfHiring;
    }

    public void setDateOfHiring(String dateOfHiring) {
        this.dateOfHiring = dateOfHiring;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
