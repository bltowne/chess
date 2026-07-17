package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws ResponseException, DataAccessException;

    GameData findGame(int gameID) throws ResponseException;

    boolean checkColor(GameData game, ChessGame.TeamColor color) throws ResponseException;

    void joinGame(GameData game, ChessGame.TeamColor color, String username) throws ResponseException, DataAccessException;

    Collection<GameData> listGames() throws ResponseException;

    void deleteAllGames() throws ResponseException, DataAccessException;
}
