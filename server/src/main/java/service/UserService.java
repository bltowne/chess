package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.*;

public class UserService {

    private final UserDAO userAccess;
    private final AuthDAO authAccess;

    public UserService(UserDAO userAccess, AuthDAO authAccess) {
        this.userAccess = userAccess;
        this.authAccess = authAccess;
    }

    public RegisterResult register(RegisterRequest r) {
        if (r.username() == null || r.password() == null || r.email() == null) {
            throw new ResponseException(ResponseException.Code.ClientError, "Error: bad request");
        }
        UserData user = userAccess.getUser(r.username());
        if (user != null) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenException, "Error: already taken");
        }
        userAccess.createUser(r);
        AuthData auth = authAccess.createAuth(r.username());
        return new RegisterResult(r.username(), auth.authToken());
    }

    public LoginResult login(LoginRequest r) {
        if (r.username() == null || r.password() == null) {
            throw new ResponseException(ResponseException.Code.ClientError, "Error: bad request");
        }
        UserData user = userAccess.getUser(r.username());
        if (user == null || !comparePasswords(user.password(), r.password())) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Error: unauthorized");
        }
        AuthData auth = authAccess.createAuth(r.username());
        return new LoginResult(r.username(), auth.authToken());
    }

    public void logout(LogoutRequest r) {
        AuthData auth = authAccess.getAuth(r.authToken());
        if (auth == null) {
            throw new ResponseException(ResponseException.Code.Unauthorized, "Error: unauthorized");
        }
        authAccess.deleteAuth(auth);
    }


    private boolean comparePasswords(String savedPassword, String submittedPassword) {
        return savedPassword.equals(submittedPassword);
    }
}
