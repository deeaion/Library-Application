package server.model.Validators;

import org.springframework.stereotype.Component;
import server.model.Person;
import server.model.Validators.Validator;
import server.model.Validators.ValidatorException;

import java.time.LocalDateTime;
@Component
public class PersonValidator implements Validator<Person> {

    @Override
    public void validate(Person person) throws ValidatorException {
        if (person == null) {
            throw new ValidatorException("Person cannot be null");
        }

        if (person.getFirstName() == null || person.getFirstName().isEmpty()) {
            throw new ValidatorException("First name cannot be null or empty");
        }

        if (person.getLastName() == null || person.getLastName().isEmpty()) {
            throw new ValidatorException("Last name cannot be null or empty");
        }

//        if (person.getBirthDay() == null || person.getBirthDay().isAfter(LocalDateTime.now())) {
//            throw new ValidatorException("Invalid birth date");
//        }

        if (person.getGender() == null || person.getGender().isEmpty()) {
            throw new ValidatorException("Gender cannot be null or empty");
        }

        if (person.getAddress() == null || person.getAddress().isEmpty()) {
            throw new ValidatorException("Address cannot be null or empty");
        }

        if (person.getPhone() == null || person.getPhone().isEmpty()) {
            throw new ValidatorException("Phone number cannot be null or empty");
        }

        if (person.getCpn() == null || person.getCpn().isEmpty()) {
            throw new ValidatorException("CPN cannot be null or empty");
        }
    }
}
