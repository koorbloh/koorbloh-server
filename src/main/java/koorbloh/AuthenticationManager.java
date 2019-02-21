package koorbloh;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthenticationManager {
    static AuthenticationManager instance = null;

    final Set<String> sessionIdSet = new HashSet<>();

    private AuthenticationManager() {

    }

    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    public void authenticate(Session session, String userId, String passKey) {
        sessionIdSet.add(session.getId());
    }

    public boolean isAuthenticated(Session session) {
        return sessionIdSet.contains(session.getId());
    }

    public void logout(Session session) {
        sessionIdSet.remove(session.getId());
    }
}
