package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class ClearService {

    private final UserDAO userAccess;
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;

    public ClearService(UserDAO userAccess, GameDAO gameAccess, AuthDAO authAccess) {
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
        this.authAccess = authAccess;
    }

    public void clear() throws ResponseException, DataAccessException {
        deleteAllUsers();
        deleteAllGames();
        deleteAllAuth();
    }

    private void deleteAllUsers() throws ResponseException, DataAccessException {
        Collection<UserData> users = userAccess.listUsers();
        if (!users.isEmpty()) {
            userAccess.deleteAllUsers();
        }
    }

    private void deleteAllGames() throws ResponseException, DataAccessException {
        Collection<GameData> games = gameAccess.listGames();
        if (!games.isEmpty()) {
            gameAccess.deleteAllGames();
        }
    }

    private void deleteAllAuth() throws ResponseException, DataAccessException {
        Collection<AuthData> auths = authAccess.listAuth();
        if (!auths.isEmpty()) {
            authAccess.deleteAllAuth();
        }
    }

}
