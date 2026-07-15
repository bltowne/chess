package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    final private Collection<AuthData> auths = new ArrayList<>();

    public AuthData createAuth(String username) throws ResponseException {
        AuthData auth = new AuthData(generateToken(), username);
        auths.add(auth);
        return auth;
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public void deleteAuth(AuthData authData) throws ResponseException {
        auths.removeIf(auth -> auth.authToken().equals(authData.authToken()));
    }

    public Collection<AuthData> listAuth() throws ResponseException {return auths;}

    public void deleteAllAuth() throws ResponseException {auths.clear();}

    private static String generateToken() throws ResponseException {
        return UUID.randomUUID().toString();
    }
}
