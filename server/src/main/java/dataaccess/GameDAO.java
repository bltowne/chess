package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws ResponseException;

    GameData findGame(int gameID) throws ResponseException;

    boolean checkColor(GameData game, ChessGame.TeamColor color) throws ResponseException;

    void joinGame(GameData game, ChessGame.TeamColor color, String username) throws ResponseException;

    Collection<GameData> listGames() throws ResponseException;

    void deleteAllGames() throws ResponseException;

    void updateGame(int gameID, ChessGame game) throws ResponseException;

    void leaveGame(int gameID, ChessGame.TeamColor color) throws ResponseException;
}
