package client.subclients;

import chess.*;
import client.GameBoardDisplay;
import client.websocket.WebSocketFacade;
import exception.ResponseException;

import java.util.ArrayList;
import java.util.Collection;

public class GameplayClient {

    private final WebSocketFacade ws;
    private final GameBoardDisplay display;
    private ChessGame game;
    private ChessGame.TeamColor color;

    public GameplayClient(WebSocketFacade ws, String authToken, int gameID) {
        this.ws = ws;
        ws.connect(authToken, gameID);
        display = new GameBoardDisplay();
        game = null;
    }

    public String help() {
        return """
                  redraw - the board
                  leave - the game
                  move <move> - make a move
                  resign - the game
                  highlight <piece coordinates> - highlights legal moves
                  help - with possible commands
               """;
    }

    public String redraw() {
        gameboard();
        return "";
    }

    public String leave(String authToken, int gameID) {
        ws.leave(authToken, gameID);
        return "You have successfully left the game";
    }

    public String move(String authToken, int gameID, String[] params) throws ResponseException {
        if (params.length >= 1) {
            ws.makeMove(authToken, gameID, convertToMove(params[0]));
            return "";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <move>");
    }

    public String resign(String authToken, int gameID) {
        ws.resign(authToken, gameID);
        return "";
    }

    public String highlight(String[] params) {
        if (params.length >= 1) {
            ChessPosition move = convertToPosition(params[0]);
            Collection<ChessMove> moveOptions = game.validMoves(move);
            Collection<ChessPosition> highlights = new ArrayList<>();
            for (ChessMove moveOption : moveOptions) {
                highlights.add(moveOption.getEndPosition());
            }
            display.showBoard(game, color, move, highlights);
            return "";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <piece coordinates>");
    }

    public void saveGame(ChessGame game, ChessGame.TeamColor color) {
        this.game = game;
        this.color = color;
    }

    public void gameboard() {
        display.showBoard(game, color, null, null);
    }

    private ChessPosition convertToPosition(String input) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.toCharArray()[i];
            if (c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8') {
                row = Character.getNumericValue(c);
            } else if (c == 'a') {
                col = 1;
            } else if (c == 'b') {
                col = 2;
            } else if (c == 'c') {
                col = 3;
            } else if (c == 'd') {
                col = 4;
            } else if (c == 'e') {
                col = 5;
            } else if (c == 'f') {
                col = 6;
            } else if (c == 'g') {
                col = 7;
            } else if (c == 'h') {
                col = 8;
            } else {
                throw new ResponseException(ResponseException.Code.ClientError, "Error: please input correctly formatted position");
            }
        }
        ChessPosition position = new ChessPosition(row, col);
        System.out.println(position);
        return position;
    }

    private ChessMove convertToMove(String input) {
        char one = input.toCharArray()[0];
        char two = input.toCharArray()[1];
        char three = input.toCharArray()[2];
        char four = input.toCharArray()[3];
        ChessMove move = new ChessMove(convertToPosition(String.format("%c%c", one, two)), convertToPosition(String.format("%c%c", three, four)), null);
        System.out.println(move);
        return move;
    }
}
