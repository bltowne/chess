package dataaccess;

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
    void getUserPositive() {}

    @Test
    void getUserNegative() {}

    @Test
    void createUserPositive() {}

    @Test
    void createUserNegative() {}

    @Test
    void listUsersPositive() {}

    @Test
    void listUsersNegative() {}

    @Test
    void deleteAllUsers() {}

    @Test
    void createGamePositive() {}

    @Test
    void createGameNegative() {}

    @Test
    void findGamePositive() {}

    @Test
    void findGameNegative() {}

    @Test
    void checkColorPositive() {}

    @Test
    void checkColorNegative() {}

    @Test
    void joinGamePositive() {}

    @Test
    void joinGameNegative() {}

    @Test
    void listGamesPositive() {}

    @Test
    void listGamesNegative() {}

    @Test
    void deleteAllGames() {}
}
