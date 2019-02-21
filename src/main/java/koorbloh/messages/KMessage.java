package koorbloh.messages;

import javax.websocket.Session;

public abstract class KMessage {
    public abstract void handle(Session session);
}
