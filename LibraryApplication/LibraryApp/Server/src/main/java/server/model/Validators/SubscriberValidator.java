package server.model.Validators;

import org.springframework.stereotype.Component;
import server.model.Subscriber;
import server.model.Validators.Validator;
import server.model.Validators.ValidatorException;

import java.time.LocalDateTime;
@Component

public class SubscriberValidator implements Validator<Subscriber> {

    private final PersonValidator personValidator = new PersonValidator();
    private final CredentialsValidator credentialsValidator = new CredentialsValidator();

    @Override
    public void validate(Subscriber subscriber) throws ValidatorException {
        if (subscriber == null) {
            throw new ValidatorException("Subscriber cannot be null");
        }

        // Validate inherited Person fields
        personValidator.validate(subscriber);

        // Validate Credentials
        credentialsValidator.validate(subscriber.getCredentials());

        if (subscriber.getDateOfSubscription() == null || subscriber.getDateOfSubscription().isAfter(LocalDateTime.now())) {
            throw new ValidatorException("Invalid date of subscription");
        }

        if (subscriber.getUniqueCode() == null || subscriber.getUniqueCode().isEmpty()) {
            throw new ValidatorException("Unique code cannot be null or empty");
        }
    }
}
