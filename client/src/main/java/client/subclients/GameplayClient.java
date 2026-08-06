package client.subclients;

import chess.*;
import client.GameBoardDisplay;
import client.websocket.WebSocketFacade;
import exception.ResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

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
            if (moveOptions == null) {
                throw new ResponseException(ResponseException.Code.ClientError, "Error: please select a square with a piece");
            }
            Collection<ChessPosition> highlights = new ArrayList<>();
            for (ChessMove moveOption : moveOptions) {
                boolean alreadyIncluded = false;
                for (ChessPosition position : highlights) {
                    if (position.equals(moveOption.getEndPosition())) {
                        alreadyIncluded = true;
                        break;
                    }
                }
                if (!alreadyIncluded) {
                    highlights.add(moveOption.getEndPosition());
                }
//                if (moveOption.getPromotionPiece() != null) {
//                    break;
//                }
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
        return new ChessPosition(row, col);
    }

    private ChessMove convertToMove(String input) {
        char one = input.toCharArray()[0];
        char two = input.toCharArray()[1];
        ChessPosition startPosition = convertToPosition(String.format("%c%c", one, two));
        char three = input.toCharArray()[2];
        char four = input.toCharArray()[3];
        ChessPosition endPosition = convertToPosition(String.format("%c%c", three, four));
        ChessPiece.PieceType promotion = checkForPromotion(startPosition);
        return new ChessMove(startPosition, endPosition, promotion);
    }

    private ChessPiece.PieceType checkForPromotion(ChessPosition position) {
        ChessBoard board = game.getBoard();
        ChessPiece piece = board.getPiece(position);
        if (piece != null && piece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
            if ((position.getRow() == 7 && piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) ||
                    (position.getRow() == 2 && piece.getTeamColor().equals(ChessGame.TeamColor.BLACK))) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Your pawn can promote! Please enter [queen|bishop|knight|rook]");
                String line = scanner.nextLine();
                if (line.equals("queen")) {
                    return ChessPiece.PieceType.QUEEN;
                } else if (line.equals("bishop")) {
                    return ChessPiece.PieceType.BISHOP;
                } else if (line.equals("knight")) {
                    return ChessPiece.PieceType.KNIGHT;
                } else if (line.equals("rook")) {
                    return ChessPiece.PieceType.ROOK;
                }
            }
        }
        return null;
    }
}
