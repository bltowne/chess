package dataaccess;

import exception.ResponseException;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MySqlAuthDAO implements AuthDAO {

    private final DatabaseAccessConfigurator dataAccess;

    public MySqlAuthDAO() {
        dataAccess = new DatabaseAccessConfigurator("auth");
    }

    public AuthData createAuth(String username) throws ResponseException {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        String authToken = UUID.randomUUID().toString();
        dataAccess.executeUpdate(statement, authToken, username);
        return new AuthData(authToken, username);
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auths WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(AuthData authData) throws ResponseException {
        var statement = "DELETE FROM auths WHERE authToken=?";
        dataAccess.executeUpdate(statement, authData.authToken());
    }

    public Collection<AuthData> listAuth() throws ResponseException {
        var result = new ArrayList<AuthData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auths";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readAuth(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteAllAuth() {
        var statement = "TRUNCATE auths";
        dataAccess.executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        return new AuthData(rs.getString("authToken"), rs.getString("username"));
    }
}
