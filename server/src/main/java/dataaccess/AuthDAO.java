package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    AuthData createAuth(String username) throws ResponseException, DataAccessException;

    AuthData getAuth(String authToken) throws ResponseException;

    void deleteAuth(AuthData authData) throws ResponseException, DataAccessException;

    Collection<AuthData> listAuth() throws ResponseException;

    void deleteAllAuth() throws ResponseException, DataAccessException;
}
