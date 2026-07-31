package client;

import chess.ChessGame;
import exception.ResponseException;
import facade.ServerFacade;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class LoggedInClient {

    private final ServerFacade server;
    private List<GameData> games;

    public LoggedInClient(ServerFacade server) {
        this.server = server;
        games = new ArrayList<>();
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
            server.create(request, authToken);
            return "Successfully created game";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <name>");
    }

    public String list(String authToken) throws ResponseException {
        games = new ArrayList<>(server.list(authToken).games());
        StringBuilder result = new StringBuilder("Game ID - Game Name - White Player - Black Player\n");
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
        return i + dash + game.gameName() + dash + whitePlayer + dash + blackPlayer + "\n";
    }

    public ChessGame join(String[] params, String authToken) throws ResponseException {
        if (params.length >= 2) {
            int externalGameID = getExternalGameID(params);
            ChessGame.TeamColor color;
            if (params[1].equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (params[1].equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new ResponseException(ResponseException.Code.ClientError, "Error: color must be white or black");
            }
            if (externalGameID <= games.size() && externalGameID > 0) {
                JoinRequest request = new JoinRequest(color, games.get(externalGameID - 1).gameID());
                server.join(request, authToken);
                return games.get(externalGameID - 1).game();
            }
            throw new ResponseException(ResponseException.Code.ClientError, "Error: provided game ID doesn't exist");
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <id> [white|black]");
    }

    private static int getExternalGameID(String[] params) throws ResponseException {
        int externalGameID;
        try {
            externalGameID = Integer.parseInt(params[0]);
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, "Error: please enter valid game ID");
        }
        return externalGameID;
    }

    public ChessGame observe(String[] params) throws ResponseException {
        if (params.length >= 1) {
            int externalGameID = getExternalGameID(params);
            if (externalGameID <= games.size() && externalGameID > 0) {
                return games.get(externalGameID - 1).game();
            }
            throw new ResponseException(ResponseException.Code.ClientError, "Error: provided game ID doesn't exist");
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <id>");
    }
}
