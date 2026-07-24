package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.net.URI;
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
        var request = buildRequest("POST", "/user", r);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest r) throws ResponseException {
        var request = buildRequest("POST", "/session", r);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void logout(AuthTokenRequest r) throws ResponseException {
        var request = buildRequest("DELETE", "/session", r);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public CreateResult create(CreateRequest r) throws ResponseException {
        var request = buildRequest("POST", "/game", r);
        var response = sendRequest(request);
        return handleResponse(response, CreateResult.class);
    }

    public void join(JoinRequest r, AuthData auth) throws ResponseException {
        var request = buildRequest("PUT", "/game", r);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public ListResult list(AuthTokenRequest r) throws ResponseException {
        var request = buildRequest("GET", "/game", r);
        var response = sendRequest(request);
        return handleResponse(response, ListResult.class);
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}