package koorbloh.messages;

import koorbloh.AuthenticationManager;

import javax.websocket.Session;

public class AuthenticateMessage extends KMessage {
    public final String userId;
    public final String passKey;

    public AuthenticateMessage(String userId, String passKey) {
        this.userId = userId;
        this.passKey = passKey;
    }

    @Override
    public void handle(Session session) {
        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
        authenticationManager.authenticate(session, userId, passKey);
    }
}
