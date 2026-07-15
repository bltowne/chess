package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO{

    final private Collection<UserData> users = new ArrayList<>();

    public Collection<UserData> listUsers() {return users;}

    public void deleteAllUsers() {users.clear();}
}
