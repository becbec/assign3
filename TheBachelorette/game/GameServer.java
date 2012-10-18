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
	
	public List<PlayerCharacter> characters;
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
				if (!msg.equals("{\"blah\" : \"blah\"}")) {
					System.out.println(msg);
				
				// check client1 games
				// start game
				
				JSONObject j;
				try {
					j = new JSONObject(msg);
					System.out.println("failed here?"+msg);
					if (j.get("PlayerID").equals("1")) {
						String m = playGame(characters.get(0), null);
						client1OutgoingMsgs.add(m);
					} else if (j.get("PlayerID").equals("2")) {
						String m = playGame(characters.get(1), null);
						client2OutgoingMsgs.add(m);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				}
				
				GameServer.m_client1GameEndTime = Calendar.getInstance();
				GameServer.m_client1GameEndTime.add(Calendar.SECOND, 30);
			}
			// check game ended?
			if (Calendar.getInstance().after(GameServer.m_client1GameEndTime))
			{
				//update games states, determine if player won/lost, how many points they got etc
				client1OutgoingMsgs.add("You won!");
				client2OutgoingMsgs.add("Player 1 won the girl!");
			}
			
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
	
	private String playGame(PlayerCharacter p, String msg) {
		int stage = p.stageNumber();
		String message = "";
		if (stage == 0) {
			message = "Which girl would you like to try and get a number from?\n" +
					"Girl1, Girl2, Girl3, Girl4, Girl5, Girl6\nType a number to select a girl\n";
			p.updateStage(1);
		} else if (stage == 1 && p.isGirlSeen(Integer.parseInt(msg))) {
			message = "You have already got that girl's number!\nChoose another girl you would like to try and get a number from.\n" +
			"Girl1, Girl2, Girl3, Girl4, Girl5, Girl6\nType a number to select a girl\n";
		} else if (stage == 1 && !p.isGirlSeen(Integer.parseInt(msg))) {
			p.setGirlSeen(Integer.parseInt(msg));
			message = "What would you like to use to impress a girl and get her number?\n" +
					"1. Show your intelligence    2. Use a cheesy pick up line    3. Reveal the truth    4. Tell a joke" +
					"    5. Buy her a drink\nType a number to select what to use\n";
			p.updateStage(2);
		} else if (stage == 2) {
			message = p.initChallenege(Integer.parseInt(msg));
			p.updateStage(3);
		} else if (stage == 3) {
			if (p.isAnswerCorrect(msg)) {
				message = "that is the correct answer";
			} else if (!p.isAnswerCorrect(msg)) {
				message = "That is not the correct answer.\n Would you like to: 1. Try again at using this challenge to impress a girl, or\n" +
						"2. Go back impress a girl using something else. Type a number to select your choice.";
				p.updateStage(5);
			}
 		} else if (stage == 4) {
 			
 		} else if (stage == 5) {
 			if (Integer.parseInt(msg) == 1) {
 				message = p.initChallenege(Integer.parseInt(p.getChallengeNumber()));
 				p.updateStage(3);
 			} else if (Integer.parseInt(msg) == 2) {
 				p.setGirlSeen(Integer.parseInt(msg));
 				message = "What would you like to use to impress a girl and get her number?\n" +
				"1. Show your intelligence    2. Use a cheesy pick up line    3. Reveal the truth    4. Tell a joke" +
				"    5. Buy her a drink\nType a number to select what to use\n";
 				p.updateStage(2);
 			}
 		}
		
		return message;
	}
	
}