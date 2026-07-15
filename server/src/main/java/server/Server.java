package server;

import dataaccess.*;
import exception.ResponseException;
import io.javalin.*;
import io.javalin.http.Context;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        UserDAO userAccess = new MemoryUserDAO();
        GameDAO gameAccess = new MemoryGameDAO();
        AuthDAO authAccess = new MemoryAuthDAO();

        this.userService = new UserService();
        this.gameService = new GameService();
        this.clearService = new ClearService(userAccess, gameAccess, authAccess);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear)
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/sesson", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .exception(ResponseException.class, this::exceptionHandler);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.result(ex.toJson());
    }

    private void clear(Context ctx) throws ResponseException {
        clearService.clear();
        ctx.status(200);
    }

    private void register(Context ctx) {}

    private void login(Context ctx) {}

    private void logout(Context ctx) {}

    private void listGames(Context ctx) {}

    private void createGame(Context ctx) {}

    private void joinGame(Context ctx) {}
}
