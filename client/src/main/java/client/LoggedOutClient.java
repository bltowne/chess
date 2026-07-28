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

    public String login(String[] params) {
        String username = params[0];
        String password = params[1];
        return "LOGIN" + "\n" +
                "username: " + username + "\n" +
                "password: " + password;
    }

    public String register(String[] params) {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        return "REGISTER" + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n" +
                "email: " + email;
    }
}
