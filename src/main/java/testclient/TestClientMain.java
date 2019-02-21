package testclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import koorbloh.KoorblohServerConfig;
import koorbloh.messages.AuthenticateMessage;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestClientMain {

    public static void main(String args[]) {
        ObjectMapper objectMapper = new ObjectMapper();



        CountDownLatch latch = new CountDownLatch(1);
        KoorblohServerConfig serverConfig = KoorblohServerConfig.getServerConfig();
        ClientManager client = ClientManager.createClient();
        try {

            String userId = "steve";
            String passcode = "12345";

            client.asyncConnectToServer(TestClientEndpoint.class, new URI("ws://localhost:" + serverConfig.port + "/"))
                .get(1500, TimeUnit.MILLISECONDS)
                .getBasicRemote()
                .sendText(AuthenticateMessage.class.getSimpleName() + "!" + objectMapper.writeValueAsString((new AuthenticateMessage(userId, passcode))));


            latch.await();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}