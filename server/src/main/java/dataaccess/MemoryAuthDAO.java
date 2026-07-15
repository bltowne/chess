package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO {

    final private Collection<AuthData> auths = new ArrayList<>();

    public Collection<AuthData> listAuth() {return auths;}

    public void deleteAllAuth() {auths.clear();}
}
