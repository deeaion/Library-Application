package server.service;

import server.model.Credentials;
import server.model.CredentialsDTO;
import server.service.exceptions.InvalidCredentialsException;
import server.service.exceptions.UserAlreadyLoggedInException;
import server.service.exceptions.UserNotLoggedInException;

public interface IServiceCommon {
    CredentialsDTO login(String username, String password) throws UserAlreadyLoggedInException, InvalidCredentialsException;
    void logout(Credentials credentials) throws UserNotLoggedInException;

}
