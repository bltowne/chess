package server;

import exception.ResponseException;
import model.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest r) throws ResponseException {

    }

    public LoginResult login(LoginRequest r) throws ResponseException {

    }

    public void logout(AuthTokenRequest r) throws ResponseException {

    }

    public CreateResult create(CreateRequest r) throws ResponseException {

    }

    public void join(JoinRequest r, AuthData auth) throws ResponseException {

    }

    public ListResult list(AuthTokenRequest r) throws ResponseException {

    }

    public void clear() throws ResponseException {

    }

    private HttpRequest buildRequest(String method, String path, Object body) {

    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {

    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {

    }

    private <T> handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {

    }

    private boolean isSuccessful(int status) {

    }
}