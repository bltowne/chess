package client.subclients;

import exception.ResponseException;
import facade.ServerFacade;
import model.*;

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

    public LoginResult login(String[] params) throws ResponseException {
        if (params.length >= 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            return server.login(request);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password");
    }

    public RegisterResult register(String[] params) throws ResponseException {
        if (params.length >= 3) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            return server.register(request);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password> <email>");
    }
}
