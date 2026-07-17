package dataaccess;

import model.*;

import java.util.Collection;

public class MySqlUserDAO implements UserDAO {

    public UserData getUser(String name) {}

    public void createUser(RegisterRequest r) {}

    public Collection<UserData> listUsers() {}

    public void deleteAllUsers() {}
}
