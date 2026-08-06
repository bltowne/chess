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
                ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s has joined the game as %s", username, getPlayerType(session, command.getGameID())));
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

        boolean madeMove = false;
        boolean inCheck = false;
        boolean inCheckmate = false;
        boolean inStalemate = false;
        String mate = "";
        String check = "";

        try {
            game.makeMove(command.getMove());
            madeMove = true;
        } catch (InvalidMoveException ex) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
            session.getRemote().sendString(notification.toString());
            return;
        }

        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            inCheckmate = true;
            mate = String.format("Checkmate for %s. %s wins!", getWhiteUsername(command.getGameID()), getBlackUsername(command.getGameID()));
        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            inCheckmate = true;
            mate = String.format("Checkmate for %s. %s wins!", getBlackUsername(command.getGameID()), getWhiteUsername(command.getGameID()));
        }

        if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            inStalemate = true;
            mate = String.format("Stalemate for %s. %s wins!", getWhiteUsername(command.getGameID()), getBlackUsername(command.getGameID()));
        } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            inStalemate = true;
            mate = String.format("Stalemate for %s. %s wins!", getBlackUsername(command.getGameID()), getWhiteUsername(command.getGameID()));
        }

        if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            inCheck = true;
            check = String.format("%s is in check", getWhiteUsername(command.getGameID()));
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            inCheck = true;
            check = String.format("%s is in check", getBlackUsername(command.getGameID()));
        }

        if (madeMove) {
            if (userColor.equals(ChessGame.TeamColor.WHITE)) {
                game.setTeamTurn(ChessGame.TeamColor.BLACK);
            } else if (userColor.equals(ChessGame.TeamColor.BLACK)){
                game.setTeamTurn(ChessGame.TeamColor.WHITE);
            }

            gameAccess.updateGame(command.getGameID(), game);
            var notification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            connections.broadcast(null, notification, command.getGameID());
            var exclusiveNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s moved %s to %s", username, convertFromPosition(command.getMove().getStartPosition()),
                            convertFromPosition(command.getMove().getEndPosition())));
            connections.broadcast(session, exclusiveNotification, command.getGameID());
        }

        if (inCheckmate || inStalemate) {
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, mate);
            connections.broadcast(null, notification, command.getGameID());
            game.endGame();
            return;
        }

        if (inCheck) {
            var checkNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, check);
            connections.broadcast(null, checkNotification, command.getGameID());
        }
    }

    private void leaveGame(Session session, String username, UserGameCommand command) throws IOException {
        connections.remove(command.getGameID(), session);
        ChessGame.TeamColor color = getUserColor(command.getGameID(), username);
        if (color != null) {
            gameAccess.leaveGame(command.getGameID(), color);
        }
        var notification = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has left the game", username));
        connections.broadcast(session, notification, command.getGameID());
    }

    private void resign(Session session, String username, UserGameCommand command) throws IOException {
        User.UserType userType = connections.findUserType(session, command.getGameID());
        if (userType.equals(User.UserType.OBSERVE)) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: observers can't resign");
            session.getRemote().sendString(notification.toString());
            return;
        }
        GameData gameData = gameAccess.findGame(command.getGameID());
        ChessGame game = gameData.game();
        if (!game.getActive()) {
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game is over");
            session.getRemote().sendString(notification.toString());
            return;
        }
        game.endGame();
        gameAccess.updateGame(command.getGameID(), game);
        var notification = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has resigned. The game is over", username));
        connections.broadcast(null, notification, command.getGameID());
    }

    private ChessGame.TeamColor getUserColor(int gameID, String username) {
        GameData game = gameAccess.findGame(gameID);
        if (game != null && game.whiteUsername() != null && game.whiteUsername().equals(username)) {
            return ChessGame.TeamColor.WHITE;
        } else if (game != null && game.blackUsername() != null && game.blackUsername().equals(username)) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }

    private String getPlayerType(Session session, int gameID) {
        switch (connections.findUserType(session, gameID)) {
            case WHITE -> {
                return "white";
            }
            case BLACK -> {
                return "black";
            }
            case OBSERVE -> {
                return "observer";
            }
        }
        return "";
    }

    private String getWhiteUsername(int gameID) {
        GameData game = gameAccess.findGame(gameID);
        return game.whiteUsername();
    }

    private String getBlackUsername (int gameID) {
        GameData game = gameAccess.findGame(gameID);
        return game.blackUsername();
    }

    private String convertFromPosition(ChessPosition position) {
        String letter = "";
        String number = "";
        if (position.getColumn() == 1) {
            letter = "a";
        } else if (position.getColumn() == 2) {
            letter = "b";
        } else if (position.getColumn() == 3) {
            letter = "c";
        } else if (position.getColumn() == 4) {
            letter = "d";
        } else if (position.getColumn() == 5) {
            letter = "e";
        } else if (position.getColumn() == 6) {
            letter = "f";
        } else if (position.getColumn() == 7) {
            letter = "g";
        } else if (position.getColumn() == 8) {
            letter = "h";
        }
        if (position.getRow() == 1) {
            number = "1";
        } else if (position.getRow() == 2) {
            number = "2";
        } else if (position.getRow() == 3) {
            number = "3";
        } else if (position.getRow() == 4) {
            number = "4";
        } else if (position.getRow() == 5) {
            number = "5";
        } else if (position.getRow() == 6) {
            number = "6";
        } else if (position.getRow() == 7) {
            number = "7";
        } else if (position.getRow() == 8) {
            number = "8";
        }
        return String.format("%s%s", letter, number);
    }
}
