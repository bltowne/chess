package exception;

import com.google.gson.Gson;

import java.util.Map;

public class ResponseException extends RuntimeException {

    public enum Code {
        ServerError,
        ClientError,
        AlreadyTakenException,
        Unauthorized,
    }

    final private Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case ServerError -> 500;
            case ClientError -> 400;
            case AlreadyTakenException -> 403;
            case Unauthorized -> 401;
        };
    }
}
