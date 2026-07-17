package dataaccess;

import exception.ResponseException;
import model.RegisterRequest;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO{

    final private Collection<UserData> users = new ArrayList<>();

    public UserData getUser(String username, String password) throws ResponseException {
        for (UserData user : users) {
            if (user.username().equals(username) && user.password().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(RegisterRequest r) {
        UserData data = new UserData(r.username(), r.password(), r.email());
        users.add(data);
    }

    public Collection<UserData> listUsers() {return users;}

    public void deleteAllUsers() {users.clear();}
}
