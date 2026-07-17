package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.*;

public class GameService {

    private final GameDAO gameAccess;
    private final AuthDAO authAccess;

    public GameService(GameDAO gameAccess, AuthDAO authAccess) {
        this.gameAccess = gameAccess;
        this.authAccess = authAccess;
    }

    public CreateResult create(CreateRequest r) throws ResponseException {
        if (r.gameName() == null) {
            throw new ResponseException(ResponseException.Code.ClientError, "Error: bad request");
        }
        return new CreateResult(gameAccess.createGame(r.gameName()));
    }

    public void join(JoinRequest r, AuthData auth) throws ResponseException {
        GameData game = gameAccess.findGame(r.gameID());
        if (game == null || (r.playerColor() != ChessGame.TeamColor.WHITE && r.playerColor() != ChessGame.TeamColor.BLACK)) {
            throw new ResponseException(ResponseException.Code.ClientError, "Error: bad request");
        }
        if (!gameAccess.checkColor(game, r.playerColor())) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenException, "Error: already taken");
        }
        gameAccess.joinGame(game, r.playerColor(), auth.username());
    }

    public ListResult list(AuthTokenRequest r) throws ResponseException {
        AuthData auth = authAccess.getAuth(r.authToken());
        if (auth == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Error: unauthorized");
        }
        return new ListResult(gameAccess.listGames());
    }

}
