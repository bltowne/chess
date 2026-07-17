package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    static final UserDAO USER_ACCESS = new MySqlUserDAO();
    static final GameDAO GAME_ACCESS = new MySqlGameDAO();
    static final AuthDAO AUTH_ACCESS = new MySqlAuthDAO();
    static final UserService USER_SERVICE = new UserService(USER_ACCESS, AUTH_ACCESS);
    static final GameService GAME_SERVICE = new GameService(GAME_ACCESS, AUTH_ACCESS);
    static final ClearService CLEAR_SERVICE = new ClearService(USER_ACCESS, GAME_ACCESS, AUTH_ACCESS);

    @BeforeEach
    void reset() throws ResponseException {
        CLEAR_SERVICE.clear();
        USER_SERVICE.register(new RegisterRequest("username", "password", "email"));
    }

    @Test
    void registerPositive() throws ResponseException {
        RegisterResult actual = USER_SERVICE.register(new RegisterRequest("second", "awesome", "gmail"));
        RegisterResult expected = new RegisterResult("second", "authToken");
        Collection<UserData> actualUsers = USER_ACCESS.listUsers();

        assertEquals(2, actualUsers.size());
        assertEquals(actual.username(), expected.username());
    }

    @Test
    void registerNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> USER_SERVICE.register(new RegisterRequest("username", null, "email")));
    }

    @Test
    void loginPositive() throws ResponseException {
        LoginResult actual = USER_SERVICE.login(new LoginRequest("username", "password"));
        LoginResult expected = new LoginResult("username", "authToken");
        Collection<AuthData> auths = AUTH_ACCESS.listAuth();

        assertEquals(2, auths.size());
        assertEquals(actual.username(), expected.username());
    }

    @Test
    void loginNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> USER_SERVICE.login(new LoginRequest("username", "wrong")));
    }

    @Test
    void logoutPositive() throws ResponseException {
        LoginResult loggedIn = USER_SERVICE.login(new LoginRequest("username", "password"));
        Collection<AuthData> authsBefore = AUTH_ACCESS.listAuth();
        assertEquals(2, authsBefore.size());

        USER_SERVICE.logout(new AuthTokenRequest(loggedIn.authToken()));
        Collection<AuthData> authsAfter = AUTH_ACCESS.listAuth();
        assertEquals(1, authsAfter.size());
    }

    @Test
    void logoutNegative() throws ResponseException{
        assertThrows(ResponseException.class, () -> USER_SERVICE.logout(new AuthTokenRequest("wrong")));
    }

    @Test
    void checkAuthPositive() throws ResponseException {
        LoginResult loggedIn = USER_SERVICE.login(new LoginRequest("username", "password"));
        String authToken = loggedIn.authToken();
        AuthData actual = USER_SERVICE.checkAuth(new AuthTokenRequest(authToken));
        AuthData expected = new AuthData(authToken, "username");

        assertEquals(expected.authToken(), actual.authToken());
        assertEquals(expected.username(), actual.username());
    }

    @Test
    void checkAuthNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> USER_SERVICE.checkAuth(new AuthTokenRequest(null)));
    }

    @Test
    void createPositive() throws ResponseException {
        GAME_SERVICE.create(new CreateRequest("new game"));
        Collection<GameData> actual = GAME_ACCESS.listGames();

        assertEquals(1, actual.size());
    }

    @Test
    void createNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> GAME_SERVICE.create(new CreateRequest(null)));
    }

    @Test
    void joinPositive() throws ResponseException {
        LoginResult loggedIn = USER_SERVICE.login(new LoginRequest("username", "password"));
        AuthData auth = USER_SERVICE.checkAuth(new AuthTokenRequest(loggedIn.authToken()));
        CreateResult created = GAME_SERVICE.create(new CreateRequest("name"));
        GAME_SERVICE.join(new JoinRequest(ChessGame.TeamColor.WHITE, created.gameID()), auth);
        GameData game = GAME_ACCESS.findGame(created.gameID());

        assertEquals("username", game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    void joinNegative() throws ResponseException {
        LoginResult loggedIn = USER_SERVICE.login(new LoginRequest("username", "password"));
        AuthData auth = USER_SERVICE.checkAuth(new AuthTokenRequest(loggedIn.authToken()));
        CreateResult created = GAME_SERVICE.create(new CreateRequest("name"));
        GAME_SERVICE.join(new JoinRequest(ChessGame.TeamColor.WHITE, created.gameID()), auth);

        RegisterResult registered = USER_SERVICE.register(new RegisterRequest("second", "password", "gmail"));
        AuthData secondAuth = USER_SERVICE.checkAuth(new AuthTokenRequest(registered.authToken()));
        assertThrows(ResponseException.class, () -> GAME_SERVICE.join(new JoinRequest(ChessGame.TeamColor.WHITE, created.gameID()), secondAuth));
    }

    @Test
    void listPositive() throws ResponseException {
        LoginResult loggedIn = USER_SERVICE.login(new LoginRequest("username", "password"));
        GAME_SERVICE.create(new CreateRequest("name"));
        ListResult actual = GAME_SERVICE.list(new AuthTokenRequest(loggedIn.authToken()));

        assertEquals(1, actual.games().size());
    }

    @Test
    void listNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> GAME_SERVICE.list(new AuthTokenRequest(null)));
    }

    @Test
    void clear() throws ResponseException {
        USER_SERVICE.register(new RegisterRequest("user", "word", "gmail"));
        GAME_SERVICE.create(new CreateRequest("game 1"));
        GAME_SERVICE.create(new CreateRequest("game 2"));
        GAME_SERVICE.create(new CreateRequest("game 3"));

        CLEAR_SERVICE.clear();
        Collection<UserData> users = USER_ACCESS.listUsers();
        Collection<GameData> games = GAME_ACCESS.listGames();
        Collection<AuthData> auths = AUTH_ACCESS.listAuth();

        assertEquals(0, users.size());
        assertEquals(0, games.size());
        assertEquals(0, auths.size());
    }
}
