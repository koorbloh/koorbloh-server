package koorbloh.messages;

import javax.websocket.Session;

public class RebroadcastMessage extends KMessage {

    public final String data;

    @Deprecated //jackson
    public RebroadcastMessage() {
        data = "";
    }

    @Override
    public void handle(Session session) {

    }
}
