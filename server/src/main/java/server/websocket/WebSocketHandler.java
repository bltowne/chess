package server.websocket;

import chess.ChessGame;
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
import java.util.ArrayList;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
            String username = getUsername(command.getAuthToken());
            saveSession(gameID, session, username);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, gameID, username, command);
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

    private String getUsername(String authToken) {
        AuthDAO dataAccess = new MySqlAuthDAO();
        AuthData authData = dataAccess.getAuth(authToken);
        return authData.username();
    }

    private void saveSession(int gameID, Session session, String username) {
        GameData game = getGameData(gameID);
        if (game.whiteUsername().equals(username)) {
            connections.add(gameID, session, ChessGame.TeamColor.WHITE);
        } else if (game.blackUsername().equals(username)) {
            connections.add(gameID, session, ChessGame.TeamColor.BLACK);
        } else {
            connections.add(gameID, session, null);
        }
    }

    private void connect(Session session, Integer gameID, String username, UserGameCommand command) throws IOException {
        GameData game = getGameData(gameID);
        var notification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
        session.getRemote().sendString(notification.toString());
        var notificationToAll = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has joined the game", username));
        connections.broadcast(session, notificationToAll, gameID);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) {}

    private void leaveGame(Session session, String username, UserGameCommand command) {}

    private void resign(Session session, String username, UserGameCommand command) {}

    private GameData getGameData(int gameID) {
        GameDAO dataAccess = new MySqlGameDAO();
        return dataAccess.findGame(gameID);
    }
}
