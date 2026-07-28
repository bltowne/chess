package client;

import server.ServerFacade;

public class LoggedOutClient {

    private final ServerFacade server;

    public LoggedOutClient(ServerFacade server) {
        this.server = server;
    }

    public String help() {
        return """
                  register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                  login <USERNAME> <PASSWORD> - to play chess
                  quit - playing chess
                  help - with possible commands
               """;
    }

    public String login() {
        return "Login function";
    }

    public String register() {
        return "Register function";
    }
}
