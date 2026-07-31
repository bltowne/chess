package server.websocket;

import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        int gameID;
        Session session = ctx.session;
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = command.getGameID();
            String username = getUsername(command.getAuthString());
            saveSession(gameID, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, command);
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

    private String getUsername(String authToken) {}

    private void saveSession(int gameID, Session session) {}

    private void connect(Session session, String username, UserGameCommand command) {}

    private void makeMove(Session session, String username, UserGameCommand command) {}

    private void leaveGame(Session session, String username, UserGameCommand command) {}

    private void resign(Session session, String username, UserGameCommand command) {}
}
