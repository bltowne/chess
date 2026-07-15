package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
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

    public GameData findGame(int gameID) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Error: bad request");
    }

    public boolean checkColor(GameData game, ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) ||
                (color == ChessGame.TeamColor.BLACK && game.blackUsername() == null);
    }

    public void joinGame(GameData game, ChessGame.TeamColor color, String username) {
        GameData newGame = game.addPlayer(color, username);
        games.remove(game);
        games.add(newGame);
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
