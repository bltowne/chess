package java.client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() {
        facade.clear();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() throws ResponseException {}

    @Test
    public void registerNegative() throws ResponseException {}

    @Test
    public void loginPositive() throws ResponseException {}

    @Test
    public void loginNegative() throws ResponseException {}

    @Test
    public void logoutPositive() throws ResponseException {}

    @Test
    public void logoutNegative() throws ResponseException {}

    @Test
    public void createPositive() throws ResponseException {}

    @Test
    public void createNegative() throws ResponseException {}

    @Test
    public void joinPositive() throws ResponseException {}

    @Test
    public void joinNegative() throws ResponseException {}

    @Test
    public void listPositive() throws ResponseException {}

    @Test
    public void listNegative() throws ResponseException {}

    @Test
    public void clearPositive() throws ResponseException {}

    @Test
    public void clearNegative() throws ResponseException {}
}
