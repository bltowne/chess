package dataaccess;

import exception.ResponseException;
import model.RegisterRequest;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
    UserData getUser(String name) throws ResponseException;

    void createUser(RegisterRequest r) throws ResponseException;

    Collection<UserData> listUsers() throws ResponseException;

    void deleteAllUsers() throws ResponseException, DataAccessException;
}
