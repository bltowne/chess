package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.Collection;

public class MySqlGameDAO implements GameDAO {

    public int createGame(String gameName) {}

    public GameData findGame(int gameID) {}

    public boolean checkColor(GameData game, ChessGame.TeamColor color) {}

    public void joinGame(GameData game, ChessGame.TeamColor color, String username) {}

    public Collection<GameData> listGames() {}

    public void deleteAllGames() {}
}
