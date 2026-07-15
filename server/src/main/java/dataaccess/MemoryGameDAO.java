package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {

    final private Collection<GameData> games = new ArrayList<>();

    public Collection<GameData> listGames() {return games;}

    public void deleteAllGames() {games.clear();}
}
