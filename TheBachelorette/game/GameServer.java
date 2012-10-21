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
						p.setPlayerID(Integer.toString(this.clientOutgoingMsgs.size() + 1));
						characters.add(p);
						// characters.add(Integer.parseInt(pid)-1, p);
						
						List<String> outgoingMsgs = new LinkedList<String>();
						this.clientOutgoingMsgs.add(outgoingMsgs);
						outgoingMsgs.add("{\"PlayerID\" : \"" + this.clientOutgoingMsgs.size() + "\" }");
						
						if (characters.size() == 2) {
							for (int i = 0; i < characters.size(); i++) {
								clientOutgoingMsgs.get(i).add(playGame(characters.get(i), new JSONObject("{\"Message\" : \"\" }")).toString());
							}
							/*
							client1OutgoingMsgs.add(playGame(characters.get(0), null).toString());
							client2OutgoingMsgs.add(playGame(characters.get(1), null).toString());
							*/
						}

						
					} else {
						int index = Integer.parseInt(j.getString("PlayerID"))-1;
						clientOutgoingMsgs.get(index).add(playGame(characters.get(index),j).toString());
						
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
	
	private JSONObject playGame(PlayerCharacter p, JSONObject incomingJSON) throws JSONException {
		int stage = p.stageNumber();
		JSONObject j = new JSONObject();
		String content ="";
		String message = "";
		System.out.println("Play Game: " + incomingJSON.toString());
		String msg = incomingJSON.getString("Message");
		if (stage == 0) {
			message = "\n\nWhich girl would you like to approach?\n" +
					girlInfo(p)+"\nType a number to select a girl\n";
			j.put("Message", message);
			p.updateStage(1);
		} else if (stage == 1 && p.isGirlSeen(Integer.parseInt(msg))) {
			message = "You have already got that girl's number!\nChoose another girl to approach.\n" +
			girlInfo(p)+"\n";
			j.put("Message", message);
		} else if (stage == 1 && !p.isGirlSeen(Integer.parseInt(msg))) {
			message = "What would you like to use to impress a girl and get her number?\n" +
					"1. Show your intelligence    2. Use a cheesy pick up line    3. Reveal the truth    4. Tell a joke" +
					"    5. Buy her a drink\nType a number to select what to use\n";
			
			p.setGirl(Integer.parseInt(msg));
			j.put("Message", message);
			p.updateStage(2);
			
		} else if (stage == 2) {
			message+= p.isChallengeComplete(Integer.parseInt(msg));
			if (p.isChallengeComplete(Integer.parseInt(msg))) {
				message = "You have used this to impress this girl. Choose something else to try and impress the girl further"+
				"1. Show your intelligence    2. Use a cheesy pick up line    3. Reveal the truth    4. Tell a joke" +
				"    5. Buy her a drink\nType a number to select what to use\n";
			} else {
				message += p.initChallenege(Integer.parseInt(msg));
				p.updateStage(3);
			}
			j.put("Message", message);
		} else if (stage == 3) {
			if (p.isAnswerCorrect(msg)) {
				message += "Congratulations, that is the correct answer!";// You have got that girls number.\n Press Enter to continue...";
				message += p.updateCurrentPoints(girls.get(p.getCurrentGirl()));
				if (p.getCurrentPoints() < 10) {
					message += " However, you are now at "+p.getCurrentPoints()+"you still need "+(p.getPointsNeeded()-p.getCurrentPoints())+ " points in order to get this girls number."
					+" You will need to choose something else to impress a girl\nWhat would you like to use to impress a" +
							" girl and get her number?\n 1. Show your intelligence    2. Use a cheesy pick up line   " +
							" 3. Reveal the truth    4. Tell a joke    5. Buy her a drink\nType a number to select what to use\n";
					p.updateStage(2);
				}else {
					message+=" You have been successful in acquiring this girl's phone number! \nPress Enter to continue.";
					//Send message to other player(s) that this player got a girl's number
					//loop through queues, for all queues not including this one, add message
					for (int i = 0; i < characters.size(); i++) {  //&& i!=Integer.parseInt(p.getPlayerID()) - 1
						System.out.println("PlayerID = " + p.getPlayerID());
						if (i != Integer.parseInt(p.getPlayerID()) - 1 )
						clientOutgoingMsgs.get(i).add("{\"Message\" : \"" + p.getName() + " got a girl's number! Don't get left behind!\"}");
					}
					p.updateStage(6);
					p.setGirlSeen();
				}
				j.put("Message", message);
			} else if (!p.isAnswerCorrect(msg)) {
				message = "That is not the correct answer.\n Would you like to: \n1. Try again at using this challenge to impress a girl, or\n" +
						"2. Go back impress a girl using something else. Type a number to select your choice.";
				j.put("Message", message);
				p.updateStage(5);
			}
 		} else if (stage == 4) {
 			p.updateNumberOfGirls();
 			message = "Which girl would you like to try and get another number from?\n" +
 			girlInfo(p)+"\nType a number to select a girl\n";
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
 		} else if (stage == 6) {
 			//give more points to add to attributes
 			message = "You have been awarded 2 more attribute points to add to attributes of your choice. Press Enter to continue...";
 			j.put("Message", message);
 			j.put("UpdateCharacter", "");
 			p.updateStage(7);
 		}  else if (stage == 7) {
 			if (!msg.equals("")) {
 				//Update points
				System.out.println("Stage 7: " + incomingJSON.toString());
 				PlayerCharacter c = characters.get(Integer.parseInt(incomingJSON.getString("PlayerID"))-1);
				JSONObject uc = incomingJSON.getJSONObject("UpdateCharacter");
 				List<Attribute> l = c.getAttributes();
				for (Attribute a : l) {
					a.setAttributeValue(a.getAttributeValue() + uc.getInt(a.getAttributeType()));
				}
				for (Attribute a : l) { 
					System.out.println(a.getAttributeType() + " : " + a.getAttributeValue());
				}
 				p.updateStage(4);
 			}
 			j.put("Message", "Press Enter to continue");
 		}
		
		return j;
	}
	
	private String girlInfo(PlayerCharacter p) {
		String message = "";
		for (int i = 0; i < girls.size(); i++) {
			if (!p.isGirlSeen(i)) {
				List<Look> looks = girls.get(i).getLooks();
				message+= "Girl"+(i)+" has ";
				message+= looks.get(0).getLookValue()+" hair, ";
				message+= looks.get(1).getLookValue()+" eyes, ";
				message+= looks.get(2).getLookValue()+" body type.";
			}
			message+="\n";
		}
		
		return message;
	}
	
	private static List<PlayerCharacter> setGirls(List<PlayerCharacter> girls) { //TODO: This should be on the server, not the client, 
		//otherwise the girls wont be the same for both clients! Robin says fuck off, but not before fixing stuff
		Random generator = new Random();
		List<Attribute> aList = new ArrayList<Attribute>();
		List<Look> aLook = new ArrayList<Look>();
		int n;
		List<Integer> seen = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			String name = "girl"+i;

			// Choose things girls don't like
			/*for (int j = 0; j < 2; j++) {
				n = generator.nextInt(Attributes.values().length);
				while (seen.contains(n)){
					n = generator.nextInt(Attributes.values().length);
				}
				seen.add(n);
				aList.add(new Attribute(Attributes.values()[n].toString(), 5));
			}*/
			aList.add(new Attribute(Attributes.CHARM.toString(), 5));
			aLook = new ArrayList<Look>();
			n = generator.nextInt(HairColour.values().length);
			aLook.add(new Look("HAIR", HairColour.values()[n].toString()));
			n = generator.nextInt(EyeColour.values().length);
			aLook.add(new Look("EYE", EyeColour.values()[n].toString()));
			n = generator.nextInt(BodyType.values().length);
			aLook.add(new Look("BODY", BodyType.values()[n].toString()));
			
			girls.add(new PlayerCharacter(name,aList,aLook));
		}
		
		return girls;
	}
	
}