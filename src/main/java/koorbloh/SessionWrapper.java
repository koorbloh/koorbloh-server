package koorbloh;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import koorbloh.messages.AuthenticateMessage;
import koorbloh.messages.KMessage;

import javax.websocket.Session;

public class SessionWrapper {

    private final Session session;
    private final String sessionId;
    private final ObjectMapper objectMapper;
    AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

    public SessionWrapper(Session session) {
        this.session = session;
        this.sessionId = session.getId();
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void onClose() {
        authenticationManager.logout(session);
    }

    public void handleMessage(Class<? extends KMessage> messageType, String message) throws Exception {
        checkAuthentication(messageType, message);
        KMessage parsedMessage = (KMessage)objectMapper.readValue(message, messageType);
        parsedMessage.handle(session);
    }

    private void checkAuthentication(Class<? extends KMessage> messageType, String message) throws Exception {
        if (!messageType.equals(AuthenticateMessage.class) && !authenticationManager.isAuthenticated(session)) {
            throw new Exception("Session not authenticated");
        }
    }

    public String getSessionId() {
        return sessionId;
    }
}
