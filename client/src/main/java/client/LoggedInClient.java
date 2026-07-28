package client;

import chess.ChessGame;
import exception.ResponseException;
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

    public String logout() throws ResponseException {
        return "Logout function";
    }

    public String create(String[] params) throws ResponseException {
        if (params.length >= 1) {
            String gameName = params[0];
            return "Create function";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <name>");
    }

    public String list() throws ResponseException{
        return "List function";
    }

    public String join(String[] params) throws ResponseException {
        if (params.length >= 2) {
            int gameID = Integer.parseInt(params[0]);
            ChessGame.TeamColor color;
            if (params[1].equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (params[1].equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            }
            return "Join function";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <id> [white|black]");
    }

    public String observe(String[] params) throws ResponseException {
        if (params.length >= 1) {
            int gameID = Integer.parseInt(params[0]);
            return "Observe function";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <id>");
    }
}
