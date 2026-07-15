package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    final private Collection<AuthData> auths = new ArrayList<>();

    public AuthData createAuth(String username) throws ResponseException {
        return new AuthData(generateToken(), username);
    }

    public Collection<AuthData> listAuth() {return auths;}

    public void deleteAllAuth() {auths.clear();}

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
