package server.service;

import server.model.Credentials;

public interface IServiceCommon {
    Credentials login(String username, String password);
    void logout(Credentials credentials);

}
