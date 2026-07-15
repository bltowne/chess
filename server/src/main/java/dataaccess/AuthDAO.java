package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    AuthData createAuth(String username) throws ResponseException;

    Collection<AuthData> listAuth() throws ResponseException;

    void deleteAllAuth() throws ResponseException;
}
