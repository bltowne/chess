package dataaccess;

import exception.ResponseException;
import model.RegisterRequest;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
    UserData getUser(String username, String password) throws ResponseException;

    void createUser(RegisterRequest r) throws ResponseException, DataAccessException;

    Collection<UserData> listUsers() throws ResponseException;

    void deleteAllUsers() throws ResponseException, DataAccessException;
}
