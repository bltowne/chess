package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

public record User(Session session, UserType userType) {

    public enum UserType {
        WHITE,
        BLACK,
        OBSERVE
    }
}
