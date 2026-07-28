package client;

import exception.ResponseException;
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

    public String login(String[] params) throws ResponseException {
        if (params.length >= 2) {
            String username = params[0];
            String password = params[1];
            return "LOGIN" + "\n" +
                    "username: " + username + "\n" +
                    "password: " + password;
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password");
    }

    public String register(String[] params) throws ResponseException {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            return "REGISTER" + "\n" +
                    "username: " + username + "\n" +
                    "password: " + password + "\n" +
                    "email: " + email;
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password> <email>");
    }
}
