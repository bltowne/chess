package server.websocket;

import chess.*;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameAccess = new MySqlGameDAO();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws IOException {
        int gameID;
        Session session = ctx.session;
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = command.getGameID();
            String username = getUsername(command.getAuthToken(), session);
            if (username == null) {
                return;
            }
            saveSession(gameID, session, username);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                    makeMove(session, username, moveCommand);
                }
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (ResponseException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private String getUsername(String authToken, Session session) throws IOException {
        try {
            AuthDAO dataAccess = new MySqlAuthDAO();
            AuthData authData = dataAccess.getAuth(authToken);
            return authData.username();
        } catch (Exception ex) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
            session.getRemote().sendString(notification.toString());
            return null;
        }
    }

    private void saveSession(int gameID, Session session, String username) {
        ChessGame.TeamColor color = getUserColor(gameID, username);
        connections.add(gameID, session, color);
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException {
        GameData game = gameAccess.findGame(command.getGameID());
        if (game == null) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid game ID");
            session.getRemote().sendString(notification.toString());
            return;
        }
        var notification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
        session.getRemote().sendString(notification.toString());
        var notificationToAll = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has joined the game", username));
        connections.broadcast(session, notificationToAll, command.getGameID());
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws IOException {
        GameData gameData = gameAccess.findGame(command.getGameID());
        ChessGame game = gameData.game();
        if (!game.getActive()) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game has ended");
            session.getRemote().sendString(notification.toString());
            return;
        }

        ChessGame.TeamColor userColor = getUserColor(command.getGameID(), username);
        if (userColor == null || !userColor.equals(game.getTeamTurn())) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: not your turn");
            session.getRemote().sendString(notification.toString());
            return;
        }

        try {
            game.makeMove(command.getMove());
        } catch (InvalidMoveException ex) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
            session.getRemote().sendString(notification.toString());
            return;
        }

        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            var notification = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION, "Checkmate for team white. Black wins!"
            );
            connections.broadcast(null, notification, command.getGameID());
            game.endGame();
            return;
        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            var notification = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION, "Checkmate for team black. White wins!"
            );
            connections.broadcast(null, notification, command.getGameID());
            game.endGame();
            return;
        }

        if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            var notification = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION, "Stalemate for team white. Black wins!"
            );
            connections.broadcast(null, notification, command.getGameID());
            game.endGame();
            return;
        } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            var notification = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION, "Stalemate for team black. White wins!"
            );
            connections.broadcast(null, notification, command.getGameID());
            game.endGame();
            return;
        }

        if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            var notification = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION, "White is in check"
            );
            connections.broadcast(null, notification, command.getGameID());
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            var notification = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION, "Black is in check"
            );
            connections.broadcast(null, notification, command.getGameID());
        }

        if (userColor.equals(ChessGame.TeamColor.WHITE)) {
            game.setTeamTurn(ChessGame.TeamColor.BLACK);
        } else if (userColor.equals(ChessGame.TeamColor.BLACK)){
            game.setTeamTurn(ChessGame.TeamColor.WHITE);
        }
    }

    private void leaveGame(Session session, String username, UserGameCommand command) throws IOException {
        connections.remove(command.getGameID(), session);
        var notification = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has left the game", username));
        connections.broadcast(session, notification, command.getGameID());
    }

    private void resign(Session session, String username, UserGameCommand command) throws IOException {
        GameData gameData = gameAccess.findGame(command.getGameID());
        ChessGame game = gameData.game();
        game.endGame();
        var notification = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has resigned. The game is over", username));
        connections.broadcast(null, notification, command.getGameID());
    }

    private ChessGame.TeamColor getUserColor(int gameID, String username) {
        GameData game = gameAccess.findGame(gameID);
        if (game != null && game.whiteUsername().equals(username)) {
            return ChessGame.TeamColor.WHITE;
        } else if (game != null && game.blackUsername().equals(username)) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }
}
