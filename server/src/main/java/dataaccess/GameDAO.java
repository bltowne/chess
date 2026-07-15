package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames() throws ResponseException;

    void deleteAllGames() throws ResponseException;
}
