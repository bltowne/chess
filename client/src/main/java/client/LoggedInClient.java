package client;

import server.ServerFacade;

public class LoggedInClient {

    private final ServerFacade server;

    public LoggedInClient(ServerFacade server) {
        this.server = server;
    }

    public String help() {
        return """
                  create <NAME> - a game
                  list - games
                  join <ID> [WHITE|BLACK] - a game
                  observe <ID> - a game
                  logout - when you are done
                  quit - playing chess
                  help - with possible commands
               """;
    }

    public String logout() {
        return "Logout function";
    }

    public String create() {
        return "Create function";
    }

    public String list() {
        return "List function";
    }

    public String join() {
        return "Join function";
    }

    public String observe() {
        return "Observe function";
    }
}
