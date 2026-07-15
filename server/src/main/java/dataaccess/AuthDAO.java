package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    Collection<AuthData> listAuth() throws ResponseException;

    void deleteAllAuth() throws ResponseException;
}
