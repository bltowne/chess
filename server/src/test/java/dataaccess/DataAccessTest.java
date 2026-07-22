package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {

    static UserDAO userAccess = new MySqlUserDAO();
    static GameDAO gameAccess = new MySqlGameDAO();
    static AuthDAO authAccess = new MySqlAuthDAO();

    @BeforeEach
    void clear() {
        userAccess.deleteAllUsers();
        gameAccess.deleteAllGames();
        authAccess.deleteAllAuth();
    }

    @Test
    void createAuthPositive() {
        String expected = "username";
        AuthData created = authAccess.createAuth(expected);
        AuthData actual = authAccess.getAuth(created.authToken());
        Collection<AuthData> auths = authAccess.listAuth();

        assertEquals(expected, actual.username());
        assertEquals(1, auths.size());
    }

    @Test
    void createAuthNegative() {
        assertThrows(ResponseException.class, () -> authAccess.createAuth(null));
    }

    @Test
    void getAuthPositive() {
        String expected = "username";
        AuthData created = authAccess.createAuth(expected);
        AuthData actual = authAccess.getAuth(created.authToken());

        assertEquals(expected, actual.username());
    }

    @Test
    void getAuthNegative() {
        assertNull(authAccess.getAuth(""));
    }

    @Test
    void deleteAuthPositive() {
        AuthData created = authAccess.createAuth("username");
        Collection<AuthData> auths = authAccess.listAuth();
        assertEquals(1, auths.size());

        authAccess.deleteAuth(created);
        Collection<AuthData> deletedAuths = authAccess.listAuth();
        assertEquals(0, deletedAuths.size());
    }

    @Test
    void deleteAuthNegative() {
        String statement = "DROP TABLE auths";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }

        AuthData delete = new AuthData("authToken", "username");
        assertThrows(ResponseException.class, () -> authAccess.deleteAuth(delete));
        authAccess = new MySqlAuthDAO();
    }

    @Test
    void listAuthPositive() {
        authAccess.createAuth("username");
        Collection<AuthData> auths = authAccess.listAuth();

        assertEquals(1, auths.size());
    }

    @Test
    void listAuthNegative() {
        String statement = "DROP TABLE auths";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
        assertThrows(ResponseException.class, authAccess::listAuth);
        authAccess = new MySqlAuthDAO();
    }

    @Test
    void deleteAllAuth() {
        authAccess.createAuth("username");
        authAccess.createAuth("usertwo");
        Collection<AuthData> auths = authAccess.listAuth();
        assertEquals(2, auths.size());

        authAccess.deleteAllAuth();
        Collection<AuthData> deletedAuths = authAccess.listAuth();
        assertEquals(0, deletedAuths.size());
    }

    @Test
    void getUserPositive() {
        String expected = "username";
        userAccess.createUser(new RegisterRequest(expected, "password", "email"));
        UserData actual = userAccess.getUser(expected, "password");

        assertEquals(expected, actual.username());
    }

    @Test
    void getUserNegative() {
        userAccess.createUser(new RegisterRequest("username", "password", "email"));
        assertNull(userAccess.getUser("username", "wrong"));
    }

    @Test
    void createUserPositive() {
        String expected = "username";
        userAccess.createUser(new RegisterRequest(expected, "password", "email"));
        UserData actual = userAccess.getUser(expected, "password");
        Collection<UserData> users = userAccess.listUsers();

        assertEquals(expected, actual.username());
        assertEquals(1, users.size());
    }

    @Test
    void createUserNegative() {
        assertThrows(ResponseException.class, () -> userAccess.createUser(new RegisterRequest(null, null, null)));
    }

    @Test
    void listUsersPositive() {
        String expected = "username";
        userAccess.createUser(new RegisterRequest(expected, "password", "email"));
        Collection<UserData> users = userAccess.listUsers();

        assertEquals(1, users.size());
    }

    @Test
    void listUsersNegative() {
        String statement = "DROP TABLE users";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
        assertThrows(ResponseException.class, userAccess::listUsers);
        userAccess = new MySqlUserDAO();
    }

    @Test
    void deleteAllUsers() {
        userAccess.createUser(new RegisterRequest("username", "password", "email"));
        userAccess.createUser(new RegisterRequest("usertwo", "passtwo", "gmail"));
        Collection<UserData> users = userAccess.listUsers();
        assertEquals(2, users.size());

        userAccess.deleteAllUsers();
        Collection<UserData> deletedUsers = userAccess.listUsers();
        assertEquals(0, deletedUsers.size());
    }

    @Test
    void createGamePositive() {
        String expected = "game";
        int created = gameAccess.createGame(expected);
        GameData actual = gameAccess.findGame(created);
        Collection<GameData> games = gameAccess.listGames();

        assertEquals(expected, actual.gameName());
        assertEquals(1, games.size());
    }

    @Test
    void createGameNegative() {
        assertThrows(ResponseException.class, () -> gameAccess.createGame(null));
    }

    @Test
    void findGamePositive() {
        String expected = "game";
        int created = gameAccess.createGame(expected);
        GameData actual = gameAccess.findGame(created);

        assertEquals(expected, actual.gameName());
    }

    @Test
    void findGameNegative() {
        assertNull(gameAccess.findGame(0));
    }

    @Test
    void checkColorPositive() {
        String expected = "game";
        int created = gameAccess.createGame(expected);
        GameData game = gameAccess.findGame(created);

        assertTrue(gameAccess.checkColor(game, ChessGame.TeamColor.WHITE));
    }

    @Test
    void checkColorNegative() {
        String expected = "game";
        int created = gameAccess.createGame(expected);
        GameData game = gameAccess.findGame(created);
        gameAccess.joinGame(game, ChessGame.TeamColor.WHITE, "username");

        assertFalse(gameAccess.checkColor(game, ChessGame.TeamColor.WHITE));
    }

    @Test
    void joinGamePositive() {
        String expected = "game";
        int created = gameAccess.createGame(expected);
        GameData game = gameAccess.findGame(created);
        gameAccess.joinGame(game, ChessGame.TeamColor.WHITE, "username");
        GameData updatedGame = gameAccess.findGame(game.gameID());

        assertEquals("username", updatedGame.whiteUsername());
    }

    @Test
    void joinGameNegative() {
        String statement = "DROP TABLE games";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
        assertThrows(ResponseException.class, () -> gameAccess.joinGame(
                new GameData(1000, null, null, "name", new ChessGame()),
                null,
                null
        ));
        gameAccess = new MySqlGameDAO();
    }

    @Test
    void listGamesPositive() {
        String expected = "game";
        gameAccess.createGame(expected);
        Collection<GameData> games = gameAccess.listGames();

        assertEquals(1, games.size());
    }

    @Test
    void listGamesNegative() {
        String statement = "DROP TABLE games";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
        assertThrows(ResponseException.class, gameAccess::listGames);
        gameAccess = new MySqlGameDAO();
    }

    @Test
    void deleteAllGames() {
        gameAccess.createGame("game one");
        gameAccess.createGame("game two");
        Collection<GameData> games = gameAccess.listGames();
        assertEquals(2, games.size());

        gameAccess.deleteAllGames();
        Collection<GameData> deletedGames = gameAccess.listGames();
        assertEquals(0, deletedGames.size());
    }
}
