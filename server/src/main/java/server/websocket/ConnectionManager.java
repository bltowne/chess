package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<User>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session, ChessGame.TeamColor color) {
        ArrayList<User> users;
        if (connections.containsKey(gameID)) {
            if (!connections.get(gameID).isEmpty()) {
                users = connections.get(gameID);
            } else {
                users = new ArrayList<>();
            }
        } else {
            users = new ArrayList<>();
        }
        if (!userInArray(users, session) && color != null && color.equals(ChessGame.TeamColor.WHITE)) {
            users.add(new User(session, User.UserType.WHITE));
        } else if (!userInArray(users, session) && color != null && color.equals(ChessGame.TeamColor.BLACK)) {
            users.add(new User(session, User.UserType.BLACK));
        } else if (!userInArray(users, session)){
            users.add(new User(session, User.UserType.OBSERVE));
        }
        connections.put(gameID, users);
    }

    public void remove(int gameID, Session session) {
        if (connections.containsKey(gameID)) {
            ArrayList<User> users = connections.get(gameID);
            users.removeIf(user -> user.session() == session);
            if (users.isEmpty()) {
                connections.remove(gameID);
            } else {
                connections.put(gameID, users);
            }
        }
    }

    public void broadcast(Session excludeSession, ServerMessage message, int gameID) throws IOException {
        String msg = message.toString();
        ArrayList<User> users = connections.get(gameID);
        for (User user : users) {
            Session c = user.session();
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public User.UserType findUserType(Session session, int gameID) {
        ArrayList<User> users = connections.get(gameID);
        for (User user : users) {
            if (user.session().equals(session)) {
                return user.userType();
            }
        }
        return null;
    }

    private boolean userInArray(ArrayList<User> users, Session session) {
        for (User user : users) {
            if (user.session().equals(session)) {
                return true;
            }
        }
        return false;
    }
}
