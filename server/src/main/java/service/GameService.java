package service;

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

//    public CreateResult create(CreateRequest r) {}
//
//    public JoinResult join(JoinRequest r) {}
//
    public ListResult list(AuthTokenRequest r) {
        AuthData auth = authAccess.getAuth(r.authToken());
        if (auth == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Error: unauthorized");
        }
        return new ListResult(gameAccess.listGames());
    }

}
