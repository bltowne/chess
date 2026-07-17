package dataaccess;

import model.*;

import java.util.Collection;

public class MySqlAuthDAO implements AuthDAO {

    public AuthData createAuth(String username) {}

    public AuthData getAuth(String authToken) {}

    public void deleteAuth(AuthData authData) {}

    public Collection<AuthData> listAuth() {}

    public void deleteAllAuth() {}
}
