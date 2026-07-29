package client;

import exception.ResponseException;
import facade.ServerFacade;
import model.*;

import java.util.Arrays;
import java.util.Scanner;

//import static ui.EscapeSequences.*;

public class Repl {

    private final LoggedOutClient loggedOut;
    private final LoggedInClient loggedIn;
    private final GameplayClient gameplay;
    private String isLoggedIn;
    private boolean isGameplay;

    public Repl(String serverUrl) throws ResponseException {
        ServerFacade server = new ServerFacade(serverUrl);
        isLoggedIn = null;
        isGameplay = false;
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

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (cmd.equals("quit")) return "quit";
            else if (isGameplay) return "Gameplay functions";
            else if (isLoggedIn != null) return evalLoggedIn(cmd, params);
            else return evalLoggedOut(cmd, params);
        } catch (ResponseException ex) {
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
                System.out.println(loggedIn.join(params, isLoggedIn) + "\n");
                isGameplay = true;
                gameplay.gameboard(params);
                return "";
            }
            case "observe" -> {
                System.out.println(loggedIn.observe(params) + "\n");
                isGameplay = true;
                gameplay.gameboard(params);
                return "";
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
        } else if (isLoggedIn != null) {
            return "[LOGGED_IN]";
        } else {
            return "[LOGGED_OUT]";
        }
    }
}
