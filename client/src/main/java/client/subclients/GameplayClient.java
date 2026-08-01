package client.subclients;

import chess.ChessGame;
import client.GameBoardDisplay;
import client.websocket.WebSocketFacade;

public class GameplayClient {

    private final WebSocketFacade server;
    private final GameBoardDisplay display;

    public GameplayClient(WebSocketFacade server) {
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
        display.showBoard(new ChessGame(), null);
        return "";
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

    public void gameboard(ChessGame.TeamColor color, ChessGame game) {
        display.showBoard(game, color);
    }
}
