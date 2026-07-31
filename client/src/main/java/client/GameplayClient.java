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

    public String help() {
        return """
                  redraw - the board
                  leave - the game
                  move <move> - make a move
                  resign - the game
                  highlight <move> - highlights legal moves
                  help - with possible commands
               """;
    }

    public String redraw() {
        return "Redraw function";
    }

    public String leave() {
        return "Leave function";
    }

    public String move(String[] params) {
        return "Move function";
    }

    public String resign() {
        return "Resign function";
    }

    public String highlight(String[] params) {
        return "Highlight function";
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
