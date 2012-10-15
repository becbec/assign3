import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import org.json.*;

public class Main {
	private static Calendar m_client1GameEndTime, m_client2GameTime;
	
	public enum Attributes { //Add more as we add more to game
		LOOKS, CHARM, INTELLIGENCE, HONESTY
	}
	
	public enum LookFeatures {
		HAIR_COLOUR, EYE_COLOUR, CLOTHING 
	}
	
	public enum HairColour {
		BROWN, BLACK, BLONDE, RED
	}
	
	public enum EyeColour {
		BROWN, BLUE, GREEN, GREY
	}
	
	public enum Clothing {
		SUIT, BEACHWEAR, CLUBWEAR, CASUAL
	}
	
	public List<Character> characters;

	// TODO: PUT THIS INTO A NON-STAIC CLASS
	public static void main(String[] args) throws JSONException {
		System.out.println("hi");
		JSONObject jt = new JSONObject("{\"blah\" : \"blah\"}");
	
		jt.put("ruth", "mierowsky");
		if (jt.has("ruth"))
			System.out.println(jt.getString("ruth").toString());
		
		MessageServer listenServer = new MessageServer();
		Thread t = new Thread(listenServer);
		t.start();
		
		List<String> client1OutgoingMsgs;
		List<String> client2OutgoingMsgs;
		
		while (true)
		{
			client1OutgoingMsgs = new LinkedList<String>();
			client2OutgoingMsgs = new LinkedList<String>();
			
			if (!listenServer.m_incomingMsgQueue.isEmpty()) {
				// parse incoming message queue
				String msg = listenServer.m_incomingMsgQueue.remove();
			
				// check client1 games
				// start game
				Main.m_client1GameEndTime = Calendar.getInstance();
				Main.m_client1GameEndTime.add(Calendar.SECOND, 30);
			}
			// check game ended?
			if (Calendar.getInstance().after(Main.m_client1GameEndTime))
			{
				//update games states, determine if player won/lost etc
				client1OutgoingMsgs.add("You won!");
				client2OutgoingMsgs.add("Player 1 won the girl!");
			}
			
			// check client2 game
			
			// update client1
			listenServer.m_client1Queue.addAll(client1OutgoingMsgs);
			
			// update client2
			listenServer.m_client2Queue.addAll(client2OutgoingMsgs);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}