package game;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
	
	MessageServer listenServer;
	public List<PlayerCharacter> characters;
	public List<PlayerCharacter> girls;
	public List<List<String>> clientOutgoingMsgs;
	
	/*
	List<String> client1OutgoingMsgs;
	List<String> client2OutgoingMsgs;
	*/
	
	public GameServer() {
		listenServer = new MessageServer();
		this.m_msgServerThread = new Thread(listenServer);
		m_msgServerThread.start();	
		characters = new ArrayList<PlayerCharacter>();
		girls = new ArrayList<PlayerCharacter>();
		girls = setGirls(girls);
		clientOutgoingMsgs = new LinkedList<List<String>>();
		/*
		client1OutgoingMsgs = new LinkedList<String>();
		client2OutgoingMsgs = new LinkedList<String>();
		*/
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
			for (List<String> clientMsgs : clientOutgoingMsgs)
				clientMsgs.clear();
			
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
				
				// check client1 games
				// start game
				
				JSONObject j;
				try {
					j = new JSONObject(msg);
					if (j.has("Character")) { //Storing the characters players have created
						// String pid = j.getString("PlayerID");
						JSONObject jo = j.getJSONObject("Character");
						String name = jo.getString("Name");
						List<Attribute> la = new ArrayList<Attribute>();
						List<Look> ll = new ArrayList<Look>();
						for (Attributes a : Attributes.values()) {
							la.add( new Attribute(a.toString(), jo.getInt(a.toString())));
						}
						for (HairColour hc : HairColour.values()) {
							ll.add(new Look("HAIR", jo.getString("HAIR")));
						}
						for (EyeColour ec : EyeColour.values()) {
							ll.add(new Look("EYES", jo.getString("EYES")));
						}
						for (HairColour hc : HairColour.values()) {
							ll.add(new Look("HAIR", jo.getString("HAIR")));
						}
						for (BodyType bt : BodyType.values()) {
							ll.add(new Look("BODY", jo.getString("BODY")));
						}
						
						PlayerCharacter p = new PlayerCharacter(name, la, ll);
						characters.add(p);
						// characters.add(Integer.parseInt(pid)-1, p);
						
						List<String> outgoingMsgs = new LinkedList<String>();
						this.clientOutgoingMsgs.add(outgoingMsgs);
						outgoingMsgs.add("{\"PlayerID\" : \"" + this.clientOutgoingMsgs.size() + "\" }");
						
						if (characters.size() == 2) {
							for (int i = 0; i < characters.size(); i++) {
								clientOutgoingMsgs.get(i).add(playGame(characters.get(i), null).toString());
							}
							/*
							client1OutgoingMsgs.add(playGame(characters.get(0), null).toString());
							client2OutgoingMsgs.add(playGame(characters.get(1), null).toString());
							*/
						}
						
					} else {
						int index = Integer.parseInt(j.getString("PlayerID"))-1;
						clientOutgoingMsgs.get(index).add(playGame(characters.get(index),j.getString("Message")).toString());
						
					}
				
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				// TODO: make more generic
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
			for (int i = 0; i < characters.size(); i++)
				listenServer.m_clientOutgoingQueues.get(listenServer.connections.get(i)).addAll(this.clientOutgoingMsgs.get(i));
			/*
			// update client1
			
			// listenServer.m_client1OutgoingQueue.addAll(client1OutgoingMsgs);
			
			// update client2
			listenServer.m_client2OutgoingQueue.addAll(client2OutgoingMsgs);
			*/
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
	
	private JSONObject playGame(PlayerCharacter p, String msg) throws JSONException {
		int stage = p.stageNumber();
		JSONObject j = new JSONObject();
		String message = "";
		if (stage == 0) {
			message = "\n\nWhich girl would you like to try and get a number from?\n" +
					"Girl1, Girl2, Girl3, Girl4, Girl5, Girl6\nType a number to select a girl\n";
			j.put("Message", message);
			p.updateStage(1);
		} else if (stage == 1 && p.isGirlSeen(Integer.parseInt(msg))) {
			String content ="";
			for (int i = 0; i < girls.size(); i++) {
				if (!p.isGirlSeen(i)) {
					content+="Girl"+i+"  ";
				}
			}
			message = "You have already got that girl's number!\nChoose another girl you would like to try and get a number from.\n" +
			content+"\n";
			j.put("Message", message);
		} else if (stage == 1 && !p.isGirlSeen(Integer.parseInt(msg))) {
			message = "What would you like to use to impress a girl and get her number?\n" +
					"1. Show your intelligence    2. Use a cheesy pick up line    3. Reveal the truth    4. Tell a joke" +
					"    5. Buy her a drink\nType a number to select what to use\n";
			j.put("Message", message);
			p.updateStage(2);
		} else if (stage == 2) {
			message = p.initChallenege(Integer.parseInt(msg));
			j.put("Message", message);
			p.updateStage(3);
		} else if (stage == 3) {
			if (p.isAnswerCorrect(msg)) {
				message = "Congratulations, that is the correct answer!";// You have got that girls number.\n Press Enter to continue...";
				p.updateCurrentPoints();
				if (p.getCurrentPoints() < 10) {
					message += "However, you are now at "+p.getCurrentPoints()+"you still need "+(10-p.getCurrentPoints())+ " points in order to get this girls number."
					+" You will need to choose something else to impress a girl\nWhat would you like to use to impress a" +
							" girl and get her number?\n 1. Show your intelligence    2. Use a cheesy pick up line   " +
							" 3. Reveal the truth    4. Tell a joke    5. Buy her a drink\nType a number to select what to use\n";
					p.updateStage(2);
				}else {
					message+=" You have also go enough points and been lucky enough to get this girls number! Press Enter to continue.";
					p.updateStage(4);
				}
				
				j.put("Message", message);
				
				p.setGirlSeen();
			} else if (!p.isAnswerCorrect(msg)) {
				message = "That is not the correct answer.\n Would you like to: 1. Try again at using this challenge to impress a girl, or\n" +
						"2. Go back impress a girl using something else. Type a number to select your choice.";
				j.put("Message", message);
				p.updateStage(5);
			}
 		} else if (stage == 4) {
 			p.updateNumberOfGirls();
 			String content ="";
			for (int i = 0; i < girls.size(); i++) {
				if (!p.isGirlSeen(i)) {
					content+="Girl"+i+"  ";
				}
			}
 			message = "Which girl would you like to try and get another number from?\n" +
			content+"\nType a number to select a girl\n";
 			j.put("Message", message);
 			p.updateStage(1);
 		} else if (stage == 5) {
 			if (Integer.parseInt(msg) == 1) {
 				message = p.initChallenege(p.getChallengeNumber());
 				j.put("Message", message);
 				p.updateStage(3);
 			} else if (Integer.parseInt(msg) == 2) {
 				message = "What would you like to use to impress a girl and get her number?\n" +
				"1. Show your intelligence    2. Use a cheesy pick up line    3. Reveal the truth    4. Tell a joke" +
				"    5. Buy her a drink\nType a number to select what to use\n";
 				j.put("Message", message);
 				p.updateStage(2);
 			}
 		}
		
		return j;
	}
	
	private static List<PlayerCharacter> setGirls(List<PlayerCharacter> girls) { //TODO: This should be on the server, not the client, 
		//otherwise the girls wont be the same for both clients! Robin says fuck off, but not before fixing stuff
		Random generator = new Random();
		List<Attribute> aList = new ArrayList<Attribute>();
		List<Look> aLook = new ArrayList<Look>();
		int n;
		List<Integer> seen = new ArrayList<Integer>();
		
		for (int i = 0; i < 2; i++) {
			String name = "girl"+i;
			
			// Girls choose n things they don't like. and if a player completes a challenge then you take 1/5 their score
			// ie less positive points
			
			for (int j = 0; j < 2; j++) {
				n = generator.nextInt(Attributes.values().length);
				while (seen.contains(n)){
					n = generator.nextInt(Attributes.values().length);
				}
				seen.add(n);
				aList.add(new Attribute(Attributes.values()[n].toString(), 5));
			}
			
			n = generator.nextInt(HairColour.values().length);
			aLook.add(new Look(HairColour.values()[n].toString(), "1"));
			n = generator.nextInt(EyeColour.values().length);
			aLook.add(new Look(EyeColour.values()[n].toString(), "1"));
			n = generator.nextInt(BodyType.values().length);
			aLook.add(new Look(BodyType.values()[n].toString(), "1"));
			
			girls.add(new PlayerCharacter(name,aList,aLook));
		}
		
		// overwrite different looks
		for (int i = 0; i < 3; i++) {
			String name = "girl"+i;
			aList.add(new Attribute(Attributes.CHARM.toString(), 5));
			aLook.add(new Look(HairColour.BLACK.toString(), "1"));
			aLook.add(new Look(EyeColour.BLUE.toString(), "1"));
			aLook.add(new Look(BodyType.ATHLETIC.toString(), "1"));
			
			girls.add(new PlayerCharacter(name, aList, aLook));
		}
		
		return girls;
	}
	
}