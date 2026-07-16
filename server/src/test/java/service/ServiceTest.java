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

    static final UserDAO userAccess = new MemoryUserDAO();
    static final GameDAO gameAccess = new MemoryGameDAO();
    static final AuthDAO authAccess = new MemoryAuthDAO();
    static final UserService userService = new UserService(userAccess, authAccess);
    static final GameService gameService = new GameService(gameAccess, authAccess);
    static final ClearService clearService = new ClearService(userAccess, gameAccess, authAccess);

    @BeforeEach
    void reset() throws ResponseException {
        clearService.clear();
        userService.register(new RegisterRequest("username", "password", "email"));
    }

    @Test
    void registerPositive() throws ResponseException {
        RegisterResult actual = userService.register(new RegisterRequest("second", "awesome", "gmail"));
        RegisterResult expected = new RegisterResult("second", "authToken");
        Collection<UserData> actualUsers = userAccess.listUsers();

        assertEquals(2, actualUsers.size());
        assertEquals(actual.username(), expected.username());
    }

    @Test
    void registerNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> userService.register(new RegisterRequest("username", null, "email")));
    }

    @Test
    void loginPositive() throws ResponseException {
        LoginResult actual = userService.login(new LoginRequest("username", "password"));
        LoginResult expected = new LoginResult("username", "authToken");
        Collection<AuthData> auths = authAccess.listAuth();

        assertEquals(2, auths.size());
        assertEquals(actual.username(), expected.username());
    }

    @Test
    void loginNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> userService.login(new LoginRequest("username", "wrong")));
    }

    @Test
    void logoutPositive() throws ResponseException {
        LoginResult loggedIn = userService.login(new LoginRequest("username", "password"));
        Collection<AuthData> authsBefore = authAccess.listAuth();
        assertEquals(2, authsBefore.size());

        userService.logout(new AuthTokenRequest(loggedIn.authToken()));
        Collection<AuthData> authsAfter = authAccess.listAuth();
        assertEquals(1, authsAfter.size());
    }

    @Test
    void logoutNegative() throws ResponseException{
        assertThrows(ResponseException.class, () -> userService.logout(new AuthTokenRequest("wrong")));
    }

    @Test
    void checkAuthPositive() throws ResponseException {
        LoginResult loggedIn = userService.login(new LoginRequest("username", "password"));
        String authToken = loggedIn.authToken();
        AuthData actual = userService.checkAuth(new AuthTokenRequest(authToken));
        AuthData expected = new AuthData(authToken, "username");

        assertEquals(expected.authToken(), actual.authToken());
        assertEquals(expected.username(), actual.username());
    }

    @Test
    void checkAuthNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> userService.checkAuth(new AuthTokenRequest(null)));
    }

    @Test
    void createPositive() throws ResponseException {
        gameService.create(new CreateRequest("new game"));
        Collection<GameData> actual = gameAccess.listGames();

        assertEquals(1, actual.size());
    }

    @Test
    void createNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> gameService.create(new CreateRequest(null)));
    }

    @Test
    void joinPositive() throws ResponseException {
        LoginResult loggedIn = userService.login(new LoginRequest("username", "password"));
        AuthData auth = userService.checkAuth(new AuthTokenRequest(loggedIn.authToken()));
        CreateResult created = gameService.create(new CreateRequest("name"));
        gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, created.gameID()), auth);
        GameData game = gameAccess.findGame(created.gameID());

        assertEquals("username", game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    void joinNegative() throws ResponseException {
        LoginResult loggedIn = userService.login(new LoginRequest("username", "password"));
        AuthData auth = userService.checkAuth(new AuthTokenRequest(loggedIn.authToken()));
        CreateResult created = gameService.create(new CreateRequest("name"));
        gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, created.gameID()), auth);

        RegisterResult registered = userService.register(new RegisterRequest("second", "password", "gmail"));
        AuthData secondAuth = userService.checkAuth(new AuthTokenRequest(registered.authToken()));
        assertThrows(ResponseException.class, () -> gameService.join(new JoinRequest(ChessGame.TeamColor.WHITE, created.gameID()), secondAuth));
    }

    @Test
    void listPositive() throws ResponseException {
        LoginResult loggedIn = userService.login(new LoginRequest("username", "password"));
        gameService.create(new CreateRequest("name"));
        ListResult actual = gameService.list(new AuthTokenRequest(loggedIn.authToken()));

        assertEquals(1, actual.games().size());
    }

    @Test
    void listNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> gameService.list(new AuthTokenRequest(null)));
    }

    @Test
    void clear() throws ResponseException {
        userService.register(new RegisterRequest("user", "word", "gmail"));
        gameService.create(new CreateRequest("game 1"));
        gameService.create(new CreateRequest("game 2"));
        gameService.create(new CreateRequest("game 3"));

        clearService.clear();
        Collection<UserData> users = userAccess.listUsers();
        Collection<GameData> games = gameAccess.listGames();
        Collection<AuthData> auths = authAccess.listAuth();

        assertEquals(0, users.size());
        assertEquals(0, games.size());
        assertEquals(0, auths.size());
    }
}
