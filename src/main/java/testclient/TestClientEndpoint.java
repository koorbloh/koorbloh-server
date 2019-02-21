package testclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;

@ClientEndpoint(subprotocols = {"koorbloh-protocol"})
public class TestClientEndpoint {
    final static Logger LOG = LoggerFactory.getLogger(TestClientEndpoint.class);

    private void annotatedLogDebug(Session session, String string, Object... args) {
        String sessionId = session != null ? session.getId() : "UNSET";
        LOG.debug("[" + sessionId + "]: " + string, args);
    }

    @OnOpen
    public void onOpen(Session session) {
        annotatedLogDebug(session, "Connected");


    }

    @OnMessage
    public void onMessage(String message, Session session) {
        annotatedLogDebug(session, "Message RX: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        annotatedLogDebug(session, "Disconnected" + closeReason.toString());
    }
}
