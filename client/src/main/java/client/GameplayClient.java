package client;

import chess.ChessGame;
import facade.ServerFacade;

public class GameplayClient {

    private final ServerFacade server;
    private final GameBoardDisplay display;

    public GameplayClient(ServerFacade server) {
        this.server = server;
        display = new GameBoardDisplay();
    }

    public void gameboard(String[] params) {
        if (params.length < 2) {
            display.showBoard(new ChessGame(), null);
        } else if (params[1].equals("white")) {
            display.showBoard(new ChessGame(), ChessGame.TeamColor.WHITE);
        } else if (params[1].equals("black")) {
            display.showBoard(new ChessGame(), ChessGame.TeamColor.BLACK);
        }
    }
}
