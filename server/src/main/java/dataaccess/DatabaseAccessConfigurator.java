package dataaccess;

import exception.ResponseException;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DatabaseAccessConfigurator {

    public DatabaseAccessConfigurator(String data) {
        if (data.equals("user")) {
            configureDatabase(createStatementsUser);
        } else if (data.equals("auth")) {
            configureDatabase(createStatementsAuth);
        } else if (data.equals("game")) {
            configureDatabase(createStatementsGame);
        }
    }

    public void configureDatabase(String[] createStatements) {
        try {
            DatabaseManager.createDatabase();
            try (Connection conn = DatabaseManager.getConnection()) {
                for (String statement : createStatements) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to configure database: %s", ex.getMessage()));
        }
    }

    public void executeUpdate(String statement, Object... params) {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                }
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatementsAuth = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private final String[] createStatementsUser = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private final String[] createStatementsGame = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            ) AUTO_INCREMENT=1000 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


}
