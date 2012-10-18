package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import server.MessageServer;

import game.*;
import game.GameServer.Attributes;
import game.GameServer.HairColour;
import game.GameServer.EyeColour;
import game.GameServer.BodyType;

public class GameClient {
	public int playerID;
	public BufferedReader lineOfText;
	public Integer points;
	public int remainingPoints;
	public String name;
	public List<Attribute> attrs;
	public List<Look> looks;
	List<PlayerCharacter> girls;
	
	
	public GameClient() {
		playerID = 0;
		lineOfText = new BufferedReader(new InputStreamReader(System.in));
		points = 0;
		remainingPoints = 20;
		name = "";
		attrs = new ArrayList<Attribute>();
		looks = new ArrayList<Look>();
		girls = new ArrayList<PlayerCharacter>();
		girls = setGirls(girls);
	}
	
	public static void main(String[] args) throws JSONException {
		GameClient gc = new GameClient();

		
	    try { //TODO: error checking for server/port
	    	System.out.println("Please enter the host server");
			String server = gc.lineOfText.readLine();
			System.out.println("Please enter the host port");
			String temp = gc.lineOfText.readLine();
			int port = Integer.parseInt(temp);
	    	System.out.println("Please enter your name");
			gc.name = gc.lineOfText.readLine();
			System.out.println("You have " + gc.remainingPoints + " points to assign to the following attributes: ");
			System.out.println("INTELLIGENCE, LOOKS, CHARM, HONESTY, STRENGTH, KINDNESS, HUMOUR, ");
			System.out.println("ENTHUSIASM, ADAPTABILITY, RELIABILITY, and GENEROSITY");



			for (Attributes attr : Attributes.values()) { 
				if (gc.remainingPoints ==0 ) {
					Attribute newAttr = new Attribute(attr.toString(), 0);
					gc.attrs.add(newAttr);
				} else {

					System.out.println();
					System.out.println("You have " + gc.remainingPoints + " points remaining");
					System.out.println("How many points would you like to assign to " + attr);
					temp = gc.lineOfText.readLine();
					gc.points = tryParse(temp);
					while (gc.points == null) {
						System.out.println("Please make sure to enter numbers only");
						System.out.println("How many points would you like to assign to " + attr);
						temp = gc.lineOfText.readLine();
						gc.points = Integer.parseInt(temp);
					}
					
					if (gc.points <= gc.remainingPoints) {
						Attribute newAttr = new Attribute(attr.toString(), gc.points);
						gc.attrs.add(newAttr);
						gc.remainingPoints -= gc.points;
					} else {
						Attribute newAttr = new Attribute(attr.toString(), gc.remainingPoints);
						gc.attrs.add(newAttr);
						System.out.println("You only had " + gc.remainingPoints + " points remaining. These points were assigned to " + attr.toString());
						gc.remainingPoints= 0;
					}
				}

			}

				int selected = 0;
				Look l = new Look();
				String selection = "";
				while (selected == 0) {
					//Select Hair Colour
					System.out.println("Please select your HAIR COLOUR"); //add checks that they typed it correctly
					System.out.print("Your choices are: ");
					for (HairColour hc : HairColour.values()) {
						System.out.print(hc + " | ");
					}
					System.out.println();
					selection = gc.lineOfText.readLine().toUpperCase();
					try {
						l = new Look("HAIR", HairColour.valueOf(selection).toString());
						selected = 1;
					} catch (IllegalArgumentException e) {
						//do nothing
					}
				}	
				
				gc.looks.add(l);
				selected = 0;
				
				while (selected == 0) {
					//Select Eye Colour
					System.out.println("Please select your EYE COLOUR"); //add checks that they typed it correctly
					System.out.print("Your choices are: ");
					selection = "";
					for (EyeColour ec : EyeColour.values()) {
						System.out.print(ec + " | ");
					}
					System.out.println();
					selection = gc.lineOfText.readLine().toUpperCase();
					try {
						l = new Look("EYES", EyeColour.valueOf(selection).toString());
						selected = 1;
					} catch (IllegalArgumentException e) {
						//do nothing
					}
				}
				gc.looks.add(l);
				selected = 0;
				
				while (selected == 0) {
					//Select Clothing
					System.out.println("Please select your BODY TYPE"); //add checks that they typed it correctly
					System.out.print("Your choices are: ");
					selection = "";
					for (BodyType bt : BodyType.values()) {
						System.out.print(bt + " | ");
					}
					System.out.println();
					selection = gc.lineOfText.readLine().toUpperCase();
					
					try {
						l = new Look("BODY", BodyType.valueOf(selection).toString());
						selected = 1;
					} catch (IllegalArgumentException e) {
						//do nothing
					}
				}
				
				gc.looks.add(l);
				
			
		PlayerCharacter character = new PlayerCharacter(gc.name,gc.attrs,gc.looks);
		//System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n");
		//System.out.println("Your chosen character: ");
		//character.printCharacter();
		
		//spawns thread to connect to Server
		ClientServerConnection csConnection = new ClientServerConnection(server, port);
		Thread t = new Thread(csConnection);
		t.start();
		//spawns thread to read incoming messages + print them
		ProcessIncomingMessages pim = new ProcessIncomingMessages(csConnection);
		t = new Thread(pim);
		t.start();

		JSONObject JObj = createJSONCharacterMsg(character, gc);
		csConnection.m_outgoingMsgQueue.add(JObj.toString());
		
		//System.out.println(JObj.toString());
		
//		jt.put("ruth", "mierowsky");
//		if (jt.has("ruth"))
//			System.out.println(jt.getString("ruth").toString());
		

		//main thread blocks and waits for user input
		String input;
		while (true) {
			 input = gc.lineOfText.readLine();
			 //TODO:process input - make JSON message
			 csConnection.m_outgoingMsgQueue.add(input);
			 
		}

		} catch (IOException e) {
			System.err.println("failed in client main");
		}
	}

	private static JSONObject createJSONCharacterMsg(PlayerCharacter character, GameClient gc) throws JSONException {
		JSONObject jt = new JSONObject();
		jt.put("PlayerID", gc.playerID );
		
		JSONObject J_character = new JSONObject();
		for (Attribute a : character.getAttributes()) {
			J_character.put(a.getAttributeType(), a.getAttributeValue());
		}
		for (Look l : character.getLooks()) {
			J_character.put(l.getLookType(), l.getLookValue());	
		}
		jt.put("Character", J_character);
		return jt;
	}


	public static Integer tryParse(String text) {
		try {
			return new Integer(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	private static List<PlayerCharacter> setGirls(List<PlayerCharacter> girls) {
		Random generator = new Random();
		List<Attribute> aList = new ArrayList<Attribute>();
		List<Look> aLook = new ArrayList<Look>();
		int n;
		
		for (int i = 0; i < 2; i++) {
			String name = "girl"+i;
			
			for (int j = 0; j < 2; j++) {
				n = generator.nextInt(Attributes.values().length);
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
		for (int i = 0; i < 2; i++) {
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