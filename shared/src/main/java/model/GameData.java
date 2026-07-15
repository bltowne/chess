package model;

import chess.ChessGame;
import exception.ResponseException;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData addPlayer(ChessGame.TeamColor color, String username) {
        if (color == ChessGame.TeamColor.WHITE) {
            return new GameData(gameID, username, blackUsername, gameName, game);
        } else if (color == ChessGame.TeamColor.BLACK) {
            return new GameData(gameID, whiteUsername, username, gameName, game);
        }
        throw new ResponseException(ResponseException.Code.AlreadyTakenException, "Error: already taken");
    }
}
