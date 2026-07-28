package client;

import chess.ChessGame;
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

    public String create(String[] params) {
        String gameName = params[0];
        return "Create function";
    }

    public String list() {
        return "List function";
    }

    public String join(String[] params) {
        int gameID = Integer.parseInt(params[0]);
        ChessGame.TeamColor color;
        if (params[1].equals("white")) {
            color = ChessGame.TeamColor.WHITE;
        } else if (params[1].equals("black")) {
            color = ChessGame.TeamColor.BLACK;
        }
        return "Join function";
    }

    public String observe(String[] params) {
        int gameID = Integer.parseInt(params[0]);
        return "Observe function";
    }
}
