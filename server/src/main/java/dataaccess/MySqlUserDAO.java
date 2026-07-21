package dataaccess;

import exception.ResponseException;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import java.util.ArrayList;
import java.util.Collection;

public class MySqlUserDAO implements UserDAO {

    private final DatabaseAccessConfigurator dataAccess;

    public MySqlUserDAO() {
        dataAccess = new DatabaseAccessConfigurator("user");
    }

    public UserData getUser(String username, String password) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && verifyUser(rs, password)) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void createUser(RegisterRequest r) throws ResponseException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(r.password(), BCrypt.gensalt());
        dataAccess.executeUpdate(statement, r.username(), hashedPassword, r.email());
    }

    public Collection<UserData> listUsers() throws ResponseException {
        var result = new ArrayList<UserData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readUser(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteAllUsers() throws ResponseException {
        var statement = "TRUNCATE users";
        dataAccess.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
    }

    private boolean verifyUser(ResultSet rs, String providedClearTextPassword) throws SQLException {
        return BCrypt.checkpw(providedClearTextPassword, rs.getString("password"));
    }
}
