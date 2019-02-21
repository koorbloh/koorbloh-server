package koorbloh;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class KoorblohServerConfig {
    private static String SERVER_CONFIG_FILE = "/koorbloh_server_config.json";
    private static KoorblohServerConfig instance = null;
    public int port;
    public int supportPort;
    public long pingTimeoutMillis;

    public static KoorblohServerConfig getServerConfig() {
        if (instance != null) {
            return instance;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String fileContents = "";
        try {
            InputStream asStream = KoorblohServerMain.class.getResourceAsStream("/koorbloh_server_config.json");
            InputStreamReader streamReader = new InputStreamReader(asStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null;) {
                fileContents += line;
            }
            instance = mapper.readValue(fileContents, KoorblohServerConfig.class);
        }catch (IOException e) {
            System.out.print("unable to read server config");
            instance = new KoorblohServerConfig();
        }
        return getServerConfig();
    }
}
