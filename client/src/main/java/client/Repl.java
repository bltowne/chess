package client;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

//import static ui.EscapeSequences.*;

public class Repl {

    private final ServerFacade server;
    private final LoggedOutClient loggedOut;
    private final LoggedInClient loggedIn;
    private final GameplayClient gameplay;
    private boolean isLoggedIn;
    private boolean isGameplay;

    public Repl(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        isLoggedIn = false;
        isGameplay = false;
        loggedOut = new LoggedOutClient();
        loggedIn = new LoggedInClient();
        gameplay = new GameplayClient();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (isGameplay) return gameplay.gameboard();
            else if (isLoggedIn) {
                return evalLoggedIn(cmd);
            }
            else {
                return evalLoggedOut(cmd);
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String evalLoggedOut(String cmd) {
        switch (cmd) {
            case "register" -> {
                isLoggedIn = true;
                return loggedOut.register();
            }
            case "login" -> {
                isLoggedIn = true;
                return loggedOut.login();
            }
            case "quit" -> {
                return "quit";
            }
            default -> {
                return loggedOut.help();
            }
        }
    }

    private String evalLoggedIn(String cmd) {
        switch (cmd) {
            case "logout" -> {
                isLoggedIn = false;
                return loggedIn.logout();
            }
            case "create" -> {
                return loggedIn.create();
            }
            case "list" -> {
                return loggedIn.list();
            }
            case "play" -> {
                isGameplay = true;
                return loggedIn.play();
            }
            case "observe" -> {
                isGameplay = true;
                return loggedIn.observe();
            }
            default -> {
                return loggedIn.help();
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + clientState() + ">>> ");
    }

    private String clientState() {
        if (isGameplay) {
            return "[GAMEPLAY]";
        } else if (isLoggedIn) {
            return "[LOGGED_IN]";
        } else {
            return "[LOGGED_OUT]";
        }
    }
}
