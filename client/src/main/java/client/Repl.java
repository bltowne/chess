package client;

import chess.ChessGame;
import client.subclients.GameplayClient;
import client.subclients.LoggedInClient;
import client.subclients.LoggedOutClient;
import client.websocket.NotificationHandler;
import exception.ResponseException;
import facade.ServerFacade;
import model.*;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;

public class Repl implements NotificationHandler {

    private final LoggedOutClient loggedOut;
    private final LoggedInClient loggedIn;
    private final GameplayClient gameplay;
    private String isLoggedIn;
    private ChessGame isGameplay;

    public Repl(String serverUrl) throws ResponseException {
        ServerFacade server = new ServerFacade(serverUrl);
        isLoggedIn = null;
        isGameplay = null;
        loggedOut = new LoggedOutClient(server);
        loggedIn = new LoggedInClient(server);
        gameplay = new GameplayClient(server);
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
//        System.out.println(notification.message());
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
                ChessGame game = loggedIn.join(params, isLoggedIn);
                System.out.println("Successfully joined game");
                isGameplay = game;
                gameplay.gameboard(params, game);
                return "";
            }
            case "observe" -> {
                ChessGame game = loggedIn.observe(params);
                System.out.println("Successfully observing game");
                gameplay.gameboard(params, game);
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
                String response = gameplay.leave();
                isGameplay = null;
                return response;
            }
            case "move" -> {
                return gameplay.move(params);
            }
            case "resign" -> {
                return gameplay.resign();
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
}
