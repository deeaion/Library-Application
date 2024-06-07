package common.restCommon;

public class PersonDTO {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String address;
    private String phone;
    private String CPN;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCPN() {
        return CPN;
    }

    public void setCPN(String CPN) {
        this.CPN = CPN;
    }

    public PersonDTO(String firstName, String lastName, String birthDate, String gender, String address, String phone, String CPN) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.CPN = CPN;
    }

    public PersonDTO() {
    }
// Getters and setters
}
