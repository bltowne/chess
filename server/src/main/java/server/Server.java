package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import service.*;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        UserDAO userAccess = new MySqlUserDAO();
        GameDAO gameAccess = new MySqlGameDAO();
        AuthDAO authAccess = new MySqlAuthDAO();

        this.userService = new UserService(userAccess, authAccess);
        this.gameService = new GameService(gameAccess, authAccess);
        this.clearService = new ClearService(userAccess, gameAccess, authAccess);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear)
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
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

    private void register(Context ctx) throws ResponseException {
        RegisterRequest request = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);
        ctx.result(new Gson().toJson(result));
        ctx.status(200);
    }

    private void login(Context ctx) throws ResponseException {
        LoginRequest request = new Gson().fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        ctx.result(new Gson().toJson(result));
        ctx.status(200);
    }

    private void logout(Context ctx) throws ResponseException {
        AuthTokenRequest request = new AuthTokenRequest(ctx.header("authorization"));
        userService.logout(request);
        ctx.status(200);
    }

    private void listGames(Context ctx) throws ResponseException {
        AuthTokenRequest request = new AuthTokenRequest(ctx.header("authorization"));
        ListResult result = gameService.list(request);
        ctx.result(new Gson().toJson(result));
        ctx.status(200);
    }

    private void createGame(Context ctx) throws ResponseException {
        checkAuth(ctx);
        CreateRequest request = new Gson().fromJson(ctx.body(), CreateRequest.class);
        CreateResult result = gameService.create(request);
        ctx.result(new Gson().toJson(result));
        ctx.status(200);
    }

    private void joinGame(Context ctx) throws ResponseException {
        AuthData auth = checkAuth(ctx);
        JoinRequest request = new Gson().fromJson(ctx.body(), JoinRequest.class);
        gameService.join(request, auth);
        ctx.status(200);
    }

    private AuthData checkAuth(Context ctx) throws ResponseException {
        String authToken = ctx.header("authorization");
        return userService.checkAuth(new AuthTokenRequest(authToken));
    }
}
