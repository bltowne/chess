package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {

    final private Collection<GameData> games = new ArrayList<>();

    public int createGame(String gameName) {
        int gameID = generateGameID();
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        games.add(game);
        return gameID;
    }

    public Collection<GameData> listGames() {return games;}

    public void deleteAllGames() {games.clear();}

    private int generateGameID() {
        Random random = new Random();
        int gameID = random.nextInt(9999);
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                gameID = generateGameID();
            }
        }
        return gameID;
    }
}
