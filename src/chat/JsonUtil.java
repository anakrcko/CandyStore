package chat;

import javax.json.Json;

public class JsonUtil {
	public static String formatMessage(String message, String user) {
        return Json.createObjectBuilder()
                .add("poruka", message)
                .add("posiljalac", user)
                .build().toString();
    }
}
