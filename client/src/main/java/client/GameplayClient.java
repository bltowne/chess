package client;

import server.ServerFacade;

public class GameplayClient {

    private final ServerFacade server;

    public GameplayClient(ServerFacade server) {
        this.server = server;
    }

    public String gameboard() {
        return "Gameboard";
    }
}
