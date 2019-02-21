package koorbloh;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionManager {

    Map<String,SessionWrapper> sessionMap = new HashMap<>();
    static SessionManager instance = null;
    private SessionManager() {

    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Optional<SessionWrapper> getWrappedSessionBySession(Session session) {
        return Optional.ofNullable(sessionMap.getOrDefault(session.getId(), null));
    }

    public void onOpen(Session session) {
        sessionMap.put(session.getId(),new SessionWrapper(session));
    }

    public void onClose(SessionWrapper sessionWrapper) {
        sessionMap.remove(sessionWrapper.getSessionId());
    }
}
