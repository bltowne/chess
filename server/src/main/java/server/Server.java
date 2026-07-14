package server;

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
        this.userService = new UserService();
        this.gameService = new GameService();
        this.clearService = new ClearService();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear)
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/sesson", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void clear(Context ctx) {}

    private void register(Context ctx) {}

    private void login(Context ctx) {}

    private void logout(Context ctx) {}

    private void listGames(Context ctx) {}

    private void createGame(Context ctx) {}

    private void joinGame(Context ctx) {}
}
