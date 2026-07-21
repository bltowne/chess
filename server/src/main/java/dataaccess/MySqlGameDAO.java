package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlGameDAO implements GameDAO {

    public MySqlGameDAO() {
        new DatabaseAccessConfigurator("game");
    }

    public int createGame(String gameName) throws ResponseException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String game = new Gson().toJson(new ChessGame());
        return executeUpdateGame(statement, null, null, gameName, game);
    }

    public GameData findGame(int gameID) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public boolean checkColor(GameData game, ChessGame.TeamColor color) throws ResponseException {
        GameData gameFromDatabase = findGame(game.gameID());
        return (color == ChessGame.TeamColor.WHITE && gameFromDatabase.whiteUsername() == null) ||
                (color == ChessGame.TeamColor.BLACK && gameFromDatabase.blackUsername() == null);
    }

    public void joinGame(GameData game, ChessGame.TeamColor color, String username) throws ResponseException {
        GameData newGame = game.addPlayer(color, username);
        var statement = "UPDATE games SET whiteUsername=?, blackUsername=? WHERE gameID=?";
        executeUpdateGame(statement, newGame.whiteUsername(), newGame.blackUsername(), newGame.gameID());
    }

    public Collection<GameData> listGames() throws ResponseException {
        var result = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteAllGames() throws ResponseException {
        var statement = "TRUNCATE games";
        executeUpdateGame(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        return new GameData(rs.getInt("gameID"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"),
                            new Gson().fromJson(rs.getString("game"), ChessGame.class));
    }

    private int executeUpdateGame(String statement, Object... params) {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case Integer p -> ps.setInt(i + 1, p);
                        case String p -> ps.setString(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
