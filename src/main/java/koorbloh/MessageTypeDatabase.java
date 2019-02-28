package koorbloh;

import com.fasterxml.jackson.databind.ObjectMapper;
import koorbloh.messages.KMessage;
import koorbloh.messages.RebroadcastMessage;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageTypeDatabase {

    static MessageTypeDatabase instance = null;

    Map<String, Class<? extends KMessage>> messageTypeMap = new HashMap<>();

    private MessageTypeDatabase() {

    }

    public static MessageTypeDatabase getInstance() {
        if (instance == null) {
            instance = new MessageTypeDatabase();

            Reflections reflections = new Reflections("koorbloh.messages");

            final Set<Class<? extends KMessage>> messageTypes = reflections.getSubTypesOf(KMessage.class);

            ObjectMapper enforcer = new ObjectMapper();

            for (Class<? extends KMessage> type : messageTypes) {
                String messageType = type.getSimpleName();
                instance.messageTypeMap.put(messageType, type);


                //Because Jackson requires default constructors AND THEY ARE SO DANG HARD TO REMEMBER TO DO,
                //I'm ENFORCING IT.
                try {
                    Constructor<?> ctor = type.getDeclaredConstructor();
                    Object object = ctor.newInstance();
                    String enforcedString = enforcer.writeValueAsString(object);
                    enforcer.readValue(enforcedString, type);
                } catch (Throwable throwable) {
                    throw new RuntimeException("Unable to handle messages for message type: " + messageType, throwable);
                }
            }

        }
        return instance;
    }

    Class<? extends KMessage> getMessageTypeFromString(String messageTypeString) {
        return messageTypeMap.getOrDefault(messageTypeString, RebroadcastMessage.class);
    }
}
