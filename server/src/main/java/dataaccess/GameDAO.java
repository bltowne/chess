package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws ResponseException;

    Collection<GameData> listGames() throws ResponseException;

    void deleteAllGames() throws ResponseException;
}
