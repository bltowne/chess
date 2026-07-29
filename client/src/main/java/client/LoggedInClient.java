package client;

import chess.ChessGame;
import exception.ResponseException;
import facade.ServerFacade;
import model.*;

import java.util.Collection;

public class LoggedInClient {

    private final ServerFacade server;
    private Collection<GameData> games;

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

    public String logout(String authToken) throws ResponseException {
        server.logout(authToken);
        return "Successfully logged out";
    }

    public String create(String[] params, String authToken) throws ResponseException {
        if (params.length >= 1) {
            CreateRequest request = new CreateRequest(params[0]);
            CreateResult result = server.create(request, authToken);
            return String.format("Successfully created game with ID %s", result.gameID());
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <name>");
    }

    public String list(String authToken) throws ResponseException {
        games = server.list(authToken).games();
        StringBuilder result = new StringBuilder("Order - Game Name - Game ID - White Player - Black Player\n");
        int i = 1;
        String dash = " - ";
        for (GameData game : games) {
            String statement = getStatement(game, i, dash);
            result.append(statement);
            i++;
        }
        return result.toString();
    }

    private static String getStatement(GameData game, int i, String dash) {
        String whitePlayer;
        String blackPlayer;
        if (game.whiteUsername() == null) {
            whitePlayer = "none";
        } else {
            whitePlayer = game.whiteUsername();
        }
        if (game.blackUsername() == null) {
            blackPlayer = "none";
        } else {
            blackPlayer = game.blackUsername();
        }
        return i + dash + game.gameName() + dash + game.gameID() + dash + whitePlayer + dash + blackPlayer + "\n";
    }

    public String join(String[] params, String authToken) throws ResponseException {
        if (params.length >= 2) {
            int gameID = Integer.parseInt(params[0]);
            ChessGame.TeamColor color;
            if (params[1].equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (params[1].equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new ResponseException(ResponseException.Code.ClientError, "Error: color must be white or black");
            }
            for (GameData game : games) {
                if (game.gameID() == gameID) {
                    JoinRequest request = new JoinRequest(color, gameID);
                    server.join(request, authToken);
                    return String.format("Successfully joined game %d", gameID);
                }
            }
            throw new ResponseException(ResponseException.Code.ClientError, "Error: provided game ID doesn't exist");
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <id> [white|black]");
    }

    public String observe(String[] params) throws ResponseException {
        if (params.length >= 1) {
            int gameID = Integer.parseInt(params[0]);
            for (GameData game : games) {
                if (game.gameID() == gameID) {
                    return String.format("Successfully observing game %d", gameID);
                }
            }
            throw new ResponseException(ResponseException.Code.ClientError, "Error: provided game ID doesn't exist");
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <id>");
    }
}
