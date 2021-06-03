package chat;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
public class MessageEncoder implements Encoder.Text<Message> {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String encode(Message message) throws EncodeException {
		 return JsonUtil.formatMessage(message.getPoruka(), message.getPosiljalac());
	}
	
}
