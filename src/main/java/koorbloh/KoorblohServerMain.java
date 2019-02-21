package koorbloh;

import koorbloh.KoorblohServerConfig;
import koorbloh.KoorblohWebsocketServer;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.tyrus.server.Server;

import java.util.concurrent.Executors;

public class KoorblohServerMain {

    public static void main(String args[]) {

        Server server = new Server("0.0.0.0", KoorblohServerConfig.getServerConfig().port, "/", null, KoorblohWebsocketServer.class);
        HttpServer httpServer = HttpServer.createSimpleServer(null, "0.0.0.0", KoorblohServerConfig.getServerConfig().supportPort);

        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            public void run() {
                try {
                    server.start();
                    httpServer.start();
                    while (true) {
                        Thread.yield();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    server.stop();
                }
            }
        });
    }
}
