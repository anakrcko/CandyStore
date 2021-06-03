package chat;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message>{

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}

	@Override
	public Message decode(String textMessage) throws DecodeException {
		Message message = new Message();
        JsonObject jsonObject = Json.createReader(new StringReader(textMessage)).readObject();
        message.setPoruka(jsonObject.getString("poruka"));
        message.setPosiljalac(jsonObject.getString("posiljalac"));
        return message;
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}

}
