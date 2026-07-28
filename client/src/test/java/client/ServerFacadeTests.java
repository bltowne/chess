package client;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import facade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void reset() {
        facade.clear();
    }

    @Test
    public void registerPositive() throws ResponseException {
        var request = new RegisterRequest("username", "password", "email");
        var response = assertDoesNotThrow(() -> facade.register(request));
        assertEquals(request.username(), response.username());
    }

    @Test
    public void registerNegative() throws ResponseException {
        var request = new RegisterRequest(null, "password", "email");
        assertThrows(ResponseException.class, () -> facade.register(request));
    }

    @Test
    public void loginPositive() throws ResponseException {
        var register = new RegisterRequest("username", "password", "email");
        facade.register(register);
        var request = new LoginRequest("username", "password");
        var result = assertDoesNotThrow(() -> facade.login(request));
        assertEquals(request.username(), result.username());
    }

    @Test
    public void loginNegative() throws ResponseException {
        var request = new LoginRequest("username", "password");
        assertThrows(ResponseException.class, () -> facade.login(request));
    }

    @Test
    public void logoutPositive() throws ResponseException {
        var register = new RegisterRequest("username", "password", "email");
        var registered = facade.register(register);
        assertDoesNotThrow(() -> facade.logout(registered.authToken()));
    }

    @Test
    public void logoutNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> facade.logout("12345"));
    }

    @Test
    public void createPositive() throws ResponseException {
        var register = new RegisterRequest("username", "password", "email");
        var registered = facade.register(register);
        var request = new CreateRequest("name");
        assertDoesNotThrow(() -> facade.create(request, registered.authToken()));
    }

    @Test
    public void createNegative() throws ResponseException {
        var request = new CreateRequest("name");
        assertThrows(ResponseException.class, () -> facade.create(request, "12345"));
    }

    @Test
    public void joinPositive() throws ResponseException {
        var register = new RegisterRequest("username", "password", "email");
        var registered = facade.register(register);
        var create = new CreateRequest("name");
        var created = facade.create(create, registered.authToken());
        var request = new JoinRequest(ChessGame.TeamColor.WHITE, created.gameID());
        assertDoesNotThrow(() -> facade.join(request, registered.authToken()));
    }

    @Test
    public void joinNegative() throws ResponseException {
        var request = new JoinRequest(ChessGame.TeamColor.WHITE, 1234);
        assertThrows(ResponseException.class, () -> facade.join(request, "12345"));
    }

    @Test
    public void listPositive() throws ResponseException {
        var register = new RegisterRequest("username", "password", "email");
        var registered = facade.register(register);
        var create = new CreateRequest("name");
        facade.create(create, registered.authToken());
        var result = assertDoesNotThrow(() -> facade.list(registered.authToken()));
        assertEquals(1, result.games().size());
    }

    @Test
    public void listNegative() throws ResponseException {
        assertThrows(ResponseException.class, () -> facade.list("12345"));
    }

    @Test
    public void clear() throws ResponseException {
        assertDoesNotThrow(() -> facade.clear());
    }
}

