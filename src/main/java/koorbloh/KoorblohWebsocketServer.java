package koorbloh;

import koorbloh.messages.KMessage;
import koorbloh.messages.RebroadcastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/", subprotocols = {"koorbloh-protocol"})
public class KoorblohWebsocketServer {
    static final Logger LOG = LoggerFactory.getLogger(KoorblohWebsocketServer.class);

    SessionManager sessionManager = SessionManager.getInstance();
    MessageTypeDatabase messageTypeDatabase = MessageTypeDatabase.getInstance();


    @OnOpen
    public void open(Session session) {
        LOG.info("Opening session with id: " + session.getId());
        session.setMaxIdleTimeout(KoorblohServerConfig.getServerConfig().pingTimeoutMillis);
        sessionManager.onOpen(session);
    }

    @OnClose
    public void close(Session session) {
        LOG.info("Closing session with id: " + session.getId());
        sessionManager.getWrappedSessionBySession(session)
                .map(sessionWrapper -> {
                    sessionManager.onClose(sessionWrapper);
                    sessionWrapper.onClose();
                    return null;
                });
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        LOG.info("Message from session with id: " + session.getId() + ": " + message);
        try {
            final String[] messageParts = message.split("!");

            if (messageParts.length != 2) {
                throw new Exception("unable to parse message, improper format");
            }

            final Class<? extends KMessage> messageType = messageTypeDatabase.getMessageTypeFromString(messageParts[0]);
            final String fullMessage = messageParts[1];

            sessionManager
                    .getWrappedSessionBySession(session)
                    .get()
                    .handleMessage(messageType, fullMessage);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                String exceptionMessage = e.getMessage();
                exceptionMessage = exceptionMessage.substring(0, Math.min(exceptionMessage.length(), 100));
                CloseReason serverErrorCloseReason = new CloseReason(SessionCloseReason.UNEXPECTED_ERROR_HANDLING_MESSAGE.getCloseCode(), exceptionMessage);
                session.close(serverErrorCloseReason);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
