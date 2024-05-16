package server.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.model.Credentials;
import server.model.CredentialsDTO;
import server.persistance.implementations.CredentialsRepository;
import server.persistance.implementations.LibrarianRepository;
import server.persistance.implementations.SubscriberRepository;
import server.service.IServiceCommon;
import server.service.util.PasswordEncryption;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ServiceCommon implements IServiceCommon {
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private LibrarianRepository librarianRepository;



    private final ConcurrentHashMap<String, Boolean> loggedInUsers = new ConcurrentHashMap<>();

    @Override
    public CredentialsDTO login(String username, String password) {
        // Check if the user is already logged in
        if (loggedInUsers.containsKey(username) && loggedInUsers.get(username)) {
            throw new IllegalStateException("User is already logged in");
        }

        // Authenticate the user (this is a placeholder, replace with actual authentication logic)
        CredentialsDTO credentials = authenticate(username, password);
        if (credentials != null) {
            loggedInUsers.put(username, true);
            return credentials;
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    @Override
    public void logout(Credentials credentials) {
        String username = credentials.getUsername();
        // Check if the user is not logged in
        if (!loggedInUsers.containsKey(username) || !loggedInUsers.get(username)) {
            throw new IllegalStateException("User is not logged in");
        }

        // Log out the user
        loggedInUsers.put(username, false);
    }

    private CredentialsDTO authenticate(String username, String password) {
        boolean found=false;
        CredentialsDTO credentialsDTO = new CredentialsDTO();
        if(Objects.equals(username, "admin") && Objects.equals(password, "admin"))
        {
            credentialsDTO=new CredentialsDTO("admin","admin","admin","admin","admin");
            return credentialsDTO;
        }

            credentialsDTO=new CredentialsDTO(credentialsRepository.findByEmail(username),"none");
            if(credentialsDTO!=null)
            {
                try {
                    if(PasswordEncryption.verifyPassword(password,credentialsDTO.getPassword(),credentialsDTO.getSeed()))
                    {
                        found=true;
                        return credentialsDTO;
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Error verifying password");
                }
            }
            credentialsDTO=new CredentialsDTO(credentialsRepository.findByUsername(username),"none");
            if(credentialsDTO!=null)
            {
                try {
                    if(PasswordEncryption.verifyPassword(password,credentialsDTO.getPassword(),credentialsDTO.getSeed()))
                    {
                        found=true;
                        return credentialsDTO;
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Error verifying password");
                }
            }
        if(!found)
        {
            return null;
        }
        credentialsDTO=findTypeOfUser(credentialsDTO);
        return credentialsDTO;


    }

    private CredentialsDTO findTypeOfUser(CredentialsDTO credentialsDTO) {
        if(subscriberRepository.findByUsername(credentialsDTO.getUsername())!=null)
        {
            credentialsDTO.setType("subscriber");
            return credentialsDTO;
        }
        if(librarianRepository.findByUserId(credentialsDTO.getId())!=null)
        {
            credentialsDTO.setType("librarian");
            return credentialsDTO;
        }
        return credentialsDTO;

    }
}
