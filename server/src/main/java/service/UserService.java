package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

public class UserService {

    private final UserDAO userAccess;
    private final AuthDAO authAccess;

    public UserService(UserDAO userAccess, AuthDAO authAccess) {
        this.userAccess = userAccess;
        this.authAccess = authAccess;
    }

    public RegisterResult register(RegisterRequest r) {
        UserData user = userAccess.getUser(r.username());
        if (user != null) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenException, "Error: username already taken");
        }
        userAccess.createUser(r);
        AuthData auth = authAccess.createAuth(r.username());
        return new RegisterResult(r.username(), auth.authToken());
    }
//
//    public LoginResult login(LoginRequest r) {}
//
//    public LogoutResult logout(LogoutRequest r) {}

}
