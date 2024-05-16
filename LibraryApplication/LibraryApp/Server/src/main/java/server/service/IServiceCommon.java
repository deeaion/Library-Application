package server.service;

import server.model.Credentials;
import server.model.CredentialsDTO;

public interface IServiceCommon {
    CredentialsDTO login(String username, String password);
    void logout(Credentials credentials);

}
