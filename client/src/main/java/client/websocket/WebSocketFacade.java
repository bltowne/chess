package client.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.messages.*;

import java.net.URI;

import static websocket.messages.ServerMessage.ServerMessageType.ERROR;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                        notificationHandler.notify(notification);
                    } catch (Exception ex) {
                        notificationHandler.notify(new ErrorMessage(ERROR, ex.getMessage()));
                    }
                }
            });
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect() {

    }

    public void makeMove() {}

    public void resign() {}

    public void leave() {}
}
