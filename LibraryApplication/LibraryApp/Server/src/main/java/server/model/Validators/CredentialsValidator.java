package server.model.Validators;

import org.springframework.stereotype.Component;
import server.model.Credentials;
import server.model.Validators.Validator;
import server.model.Validators.ValidatorException;
@Component

public class CredentialsValidator implements Validator<Credentials> {

    @Override
    public void validate(Credentials credentials) throws ValidatorException {
        if (credentials == null) {
            throw new ValidatorException("Credentials cannot be null");
        }

        if (credentials.getUsername() == null || credentials.getUsername().isEmpty()) {
            throw new ValidatorException("Username cannot be null or empty");
        }

        if (credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
            throw new ValidatorException("Password cannot be null or empty");
        }

        if (credentials.getEmail() == null || credentials.getEmail().isEmpty() || !isValidEmail(credentials.getEmail())) {
            throw new ValidatorException("Invalid email format");
        }

        if (credentials.getSeed() == null || credentials.getSeed().isEmpty()) {
            throw new ValidatorException("Seed cannot be null or empty");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
