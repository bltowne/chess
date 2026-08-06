package client;

import chess.ChessGame;
import client.subclients.GameplayClient;
import client.subclients.LoggedInClient;
import client.subclients.LoggedOutClient;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import facade.ServerFacade;
import model.*;
import websocket.messages.*;

import java.util.Arrays;
import java.util.Scanner;

public class Repl implements NotificationHandler {

    private final String serverUrl;
    private final LoggedOutClient loggedOut;
    private final LoggedInClient loggedIn;
    private GameplayClient gameplay;
    private String isLoggedIn;
    private Integer isGameplay;
    private ChessGame.TeamColor color;

    public Repl(String serverUrl) throws ResponseException {
        ServerFacade server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        isLoggedIn = null;
        isGameplay = null;
        loggedOut = new LoggedOutClient(server);
        loggedIn = new LoggedInClient(server);
        color = null;
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess! ♕");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result + "\n");
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg + "\n");
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage notification) {
        System.out.println();
        try {
            switch (notification.getServerMessageType()) {
                case LOAD_GAME -> {
                    LoadGameMessage message = (LoadGameMessage) notification;
                    ChessGame game = message.getGame();
                    gameplay.saveGame(game, color);
                    gameplay.gameboard();
                }
                case ERROR -> {
                    ErrorMessage message = (ErrorMessage) notification;
                    System.out.println(message.getErrorMessage());
                }
                case NOTIFICATION -> {
                    NotificationMessage message = (NotificationMessage) notification;
                    System.out.println(message.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        printPrompt();
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (cmd.equals("quit")) {
                return "quit";
            }
            else if (isGameplay != null) {
                return evalGameplay(cmd, params);
            }
            else if (isLoggedIn != null) {
                return evalLoggedIn(cmd, params);
            }
            else {
                return evalLoggedOut(cmd, params);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    private String evalLoggedOut(String cmd, String[] params) {
        switch (cmd) {
            case "register" -> {
                RegisterResult result = loggedOut.register(params);
                isLoggedIn = result.authToken();
                return String.format("Successfully registered and logged in %s", result.username());
            }
            case "login" -> {
                LoginResult result = loggedOut.login(params);
                isLoggedIn = result.authToken();
                return String.format("Successfully logged in %s", result.username());
            }
            default -> {
                return loggedOut.help();
            }
        }
    }

    private String evalLoggedIn(String cmd, String[] params) {
        WebSocketFacade ws;
        switch (cmd) {
            case "logout" -> {
                String result = loggedIn.logout(isLoggedIn);
                isLoggedIn = null;
                return result;
            }
            case "create" -> {
                return loggedIn.create(params, isLoggedIn);
            }
            case "list" -> {
                return loggedIn.list(isLoggedIn);
            }
            case "join" -> {
                GameData game = loggedIn.join(params, isLoggedIn);
                System.out.println("Successfully joined game");
                setColor(params[1]);
                isGameplay = game.gameID();
                ws = new WebSocketFacade(serverUrl, this);
                gameplay = new GameplayClient(ws, isLoggedIn, isGameplay);
                return "";
            }
            case "observe" -> {
                GameData game = loggedIn.observe(params);
                System.out.println("Successfully observing game");
                isGameplay = game.gameID();
                ws = new WebSocketFacade(serverUrl, this);
                gameplay = new GameplayClient(ws, isLoggedIn, isGameplay);
                return "";
            }
            default -> {
                return loggedIn.help();
            }
        }
    }

    private String evalGameplay(String cmd, String[] params) {
        switch (cmd) {
            case "redraw" -> {
                return gameplay.redraw();
            }
            case "leave" -> {
                String response = gameplay.leave(isLoggedIn, isGameplay);
                isGameplay = null;
                return response;
            }
            case "move" -> {
                return gameplay.move(isLoggedIn, isGameplay, params);
            }
            case "resign" -> {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Are you sure you want to resign? [yes|no]");
                String line = scanner.nextLine();
                if (line.equals("yes")) {
                    return gameplay.resign(isLoggedIn, isGameplay);
                } else {
                    return "";
                }
            }
            case "highlight" -> {
                return gameplay.highlight(params);
            }
            default -> {
                return gameplay.help();
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + clientState() + ">>> ");
    }

    private String clientState() {
        if (isGameplay != null) {
            return "[GAMEPLAY]";
        } else if (isLoggedIn != null) {
            return "[LOGGED_IN]";
        } else {
            return "[LOGGED_OUT]";
        }
    }

    private void setColor(String param) {
        if (param.equals("white")) {
            color = ChessGame.TeamColor.WHITE;
        } else if (param.equals("black")) {
            color = ChessGame.TeamColor.BLACK;
        }
    }
}
