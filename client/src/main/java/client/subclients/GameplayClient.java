package client.subclients;

import chess.*;
import client.GameBoardDisplay;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import exception.ResponseException;

import java.util.ArrayList;
import java.util.Collection;

public class GameplayClient {

    private final WebSocketFacade ws;
    private final GameBoardDisplay display;
    private ChessGame game;
    private ChessGame.TeamColor color;

    public GameplayClient(WebSocketFacade ws) {
        this.ws = ws;
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
            ws.makeMove(authToken, gameID, new Gson().fromJson(params[0], ChessMove.class));
            return "Move function";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <move>");
    }

    public String resign(String authToken, int gameID) {
        ws.resign(authToken, gameID);
        return "";
    }

    public String highlight(String[] params) {
        if (params.length >= 1) {
            ChessPosition move = new Gson().fromJson(params[0], ChessPosition.class);
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
}
