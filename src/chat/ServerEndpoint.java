package chat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

@javax.websocket.server.ServerEndpoint(value = "/chat/{korisnik}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ServerEndpoint 
{
	 static Map<String,Set<Session>> peers = new ConcurrentHashMap<>();;

	    @OnOpen
	    public void onOpen(Session session,  @PathParam("korisnik") String osoba) 
	    {
	        Set<Session> soba = peers.get(osoba);
	        
	        if(soba == null) 
	        {
	        	soba = new HashSet<>();
	        	soba.add(session);
	        	peers.put(osoba, soba);
	        }
	        else 
	        {
	        	soba.add(session);
	        	peers.put(osoba, soba);
	        }
	    }

	    @OnMessage
	    public void onMessage(Message message, Session session,  @PathParam("korisnik") String osoba) throws IOException, EncodeException 
	    {
	        String user = (String) session.getUserProperties().get("user");
	        
	        if (user == null) 
	        {
	            session.getUserProperties().put("user", message.getPosiljalac());
	        }
	  
	        for (Session peer : peers.get(osoba)) 
	        {
	                peer.getBasicRemote().sendObject(message);
	        }
	    }

	    @OnClose
	    public void onClose(Session session,  @PathParam("korisnik") String osoba) throws IOException, EncodeException 
	    {
	        peers.get(osoba).remove(session);
	    }

}
