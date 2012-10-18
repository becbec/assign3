package game;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import org.json.*;

import server.MessageServer;

public class GameServer implements Runnable {
	private Thread m_msgServerThread;
	private static Calendar m_client1GameEndTime, m_client2GameTime;
	
	public enum Attributes { //Add more as we add more to game
		INTELLIGENCE, LOOKS, CHARM, HONESTY, STRENGTH, KINDNESS, HUMOUR, ENTHUSIASM, ADAPTABILITY, RELIABILITY, GENEROSITY
	}
	
	public enum HairColour {
		BROWN, BLACK, BLONDE, RED
	}
	
	public enum EyeColour {
		BROWN, BLUE, GREEN, GREY
	}
	
	public enum BodyType {
		ATHLETIC, SKINNY, MUSCULAR, AVERAGE, OVERWEIGHT
	}
	
	//TODO:Remove this and let this change as player levels up???
	public enum Clothing {
		SUIT, BEACHWEAR, CLUBWEAR, CASUAL
	}
	
	public List<Character> characters;
	MessageServer listenServer;
	List<String> client1OutgoingMsgs;
	List<String> client2OutgoingMsgs;
	
	public GameServer() {
		listenServer = new MessageServer();
		this.m_msgServerThread = new Thread(listenServer);
		m_msgServerThread.start();	
	}

	 public static void main(String[] args) {
         GameServer gs = new GameServer();
         Thread t = new Thread(gs);
         t.start();

         Scanner input = new Scanner(System.in);

         while (true) {
                 String inputStr = input.next();
                 System.out.println(inputStr);
                 if (inputStr.equals("quit")) {
                         gs.stopListening();
                         input.close();
                         t.interrupt();
                         break;
                 }
         }
 }

	
	public void run() {
		while (true)
		{
			client1OutgoingMsgs = new LinkedList<String>();
			client2OutgoingMsgs = new LinkedList<String>();
			
			if (!listenServer.m_incomingMsgQueue.isEmpty()) {
				// parse incoming message queue
				String msg = listenServer.m_incomingMsgQueue.remove();				
//				try {
//					JSONObject j = new JSONObject(msg);
//					if (j.has("Character")) {
//						
//					}
//				} catch (JSONException e) {
//				}
//				
				if (!msg.equals("{\"Blah\" : \"Blah\"}")) {
					System.out.println(msg);
				}
				
				
				// check client1 games
				// start game
				GameServer.m_client1GameEndTime = Calendar.getInstance();
				GameServer.m_client1GameEndTime.add(Calendar.SECOND, 30);
			}
			// check game ended?
			/*
			if (Calendar.getInstance().after(GameServer.m_client1GameEndTime))
			{
				//update games states, determine if player won/lost, how many points they got etc
				client1OutgoingMsgs.add("You won!");
				client2OutgoingMsgs.add("Player 1 won the girl!");
			}
			*/
			
			// check client2 game
			
			// update client1
			listenServer.m_client1OutgoingQueue.addAll(client1OutgoingMsgs);
			
			// update client2
			listenServer.m_client2OutgoingQueue.addAll(client2OutgoingMsgs);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public void stopListening() { //need to figure out when to stop listening
		m_msgServerThread.interrupt();
	}
}