package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
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

    public void clear() {
        deleteAllUsers();
        deleteAllGames();
        deleteAllAuth();
    }

    private void deleteAllUsers() {
        Collection<UserData> users = userAccess.listUsers();
        if (!users.isEmpty()) {
            userAccess.deleteAllUsers();
        }
    }

    private void deleteAllGames() {
        Collection<GameData> games = gameAccess.listGames();
        if (!games.isEmpty()) {
            gameAccess.deleteAllGames();
        }
    }

    private void deleteAllAuth() {
        Collection<AuthData> auths = authAccess.listAuth();
        if (!auths.isEmpty()) {
            authAccess.deleteAllAuth();
        }
    }

}
