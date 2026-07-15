package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
    Collection<UserData> listUsers() throws ResponseException;

    void deleteAllUsers() throws ResponseException;
}
