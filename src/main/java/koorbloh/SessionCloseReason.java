package koorbloh;

import javax.websocket.CloseReason;

public class SessionCloseReason {
    public static CloseReason UNEXPECTED_ERROR_HANDLING_MESSAGE = new CloseReason(CloseReason.CloseCodes.getCloseCode(4001), "Unexpected Error on Server");
}
