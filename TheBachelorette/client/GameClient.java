package client;

import game.Attribute;
import game.GameServer.Attributes;
import game.GameServer.BodyType;
import game.GameServer.EyeColour;
import game.GameServer.HairColour;
import game.Look;
import game.PlayerCharacter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONException;

public class GameClient {
	public boolean updateCharacterFlag;
	public String playerID;
	public BufferedReader lineOfText;
	public Integer points;
	public String name;
	public List<Attribute> attrs;
	public List<Look> looks;
	
	
	public GameClient() {
		updateCharacterFlag = false;
		playerID = "";
		lineOfText = new BufferedReader(new InputStreamReader(System.in));
		points = 0;
		name = "";
		attrs = new ArrayList<Attribute>();
		looks = new ArrayList<Look>();
	}
	
	
	public static void main(String[] args) throws JSONException, InterruptedException {
		GameClient gc = new GameClient();

		
	    try { //TODO: error checking for server/port
	    	System.out.println("Please enter the host server");
			String server = gc.lineOfText.readLine();
	    	//String server = "localhost"; //TODO: Remove
			//System.out.println("Please enter the host port");
			String temp = "";
			//String temp = gc.lineOfText.readLine();
			//int port = Integer.parseInt(temp);
			int port = 1338;
	    	System.out.println("Please enter your name");
			gc.name = gc.lineOfText.readLine();
	    	//gc.name = "Ruth";
			



		//assign attributes
			assignAttributes(gc, 10);

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
					//selection = "BROWN";
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
					//selection = "BROWN";
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
					//select body type
					System.out.println("Please select your BODY TYPE"); //add checks that they typed it correctly
					System.out.print("Your choices are: ");
					selection = "";
					for (BodyType bt : BodyType.values()) {
						System.out.print(bt + " | ");
					}
					System.out.println();
					selection = gc.lineOfText.readLine().toUpperCase();
					//selection = "AVERAGE";
					try {
						l = new Look("BODY", BodyType.valueOf(selection).toString());
						selected = 1;
					} catch (IllegalArgumentException e) {
						//do nothing
					}
				}
				
				gc.looks.add(l);
				
				System.out.println("You start off wearing BEACHWEAR");
				
			
		PlayerCharacter character = new PlayerCharacter(gc.name,gc.attrs,gc.looks);
		//System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n");
		//System.out.println("Your chosen character: ");
		//character.printCharacter();
		
		//spawns thread to connect to Server
		ClientServerConnection csConnection = new ClientServerConnection(server, port);
		Thread t = new Thread(csConnection);
		t.start();
		//spawns thread to read incoming messages + print them
		ProcessIncomingMessages pim = new ProcessIncomingMessages(csConnection, gc, character);
		t = new Thread(pim);
		t.start();
		
		Thread.sleep(2000);
		//System.out.println("player id" + gc.playerID);
		JSONObject JObj = createJSONCharacterMsg(character, gc);
		csConnection.m_outgoingMsgQueue.add(JObj.toString());
		
		//System.out.println(JObj.toString());
		
//		jt.put("ruth", "mierowsky");
//		if (jt.has("ruth"))
//			System.out.println(jt.getString("ruth").toString());
		
		

		//main thread blocks and waits for user input
		String input;
		while (true) {
			if (gc.updateCharacterFlag) {
				assignAttributes(gc, 2);
				JSONObject j = new JSONObject();
				JSONObject jo = new JSONObject();
				for (Attribute a : gc.attrs) {
					jo.put(a.getAttributeType(), a.getAttributeValue());
				}
				j.put("UpdateCharacter", jo);
				j.put("Message", "0");
				j.put("PlayerID",  gc.playerID);
				csConnection.m_outgoingMsgQueue.add(j.toString());
				gc.updateCharacterFlag = false;
			}
			 input = gc.lineOfText.readLine();
			 JSONObject j = new JSONObject();
			 j.put("PlayerID", gc.playerID);
			 j.put("Message", input);
			 csConnection.m_outgoingMsgQueue.add(j.toString());
			 
		}

		} catch (IOException e) {
			System.err.println("failed in client main");
		}
	}

	private static void assignAttributes(GameClient gc, int remainingPoints) throws IOException {
		System.out.println("You have " + remainingPoints + " attribute points to assign to the following attributes: ");
		System.out.println("INTELLIGENCE, CHARM, HONESTY, HUMOUR, and GENEROSITY");
		gc.attrs = new ArrayList<Attribute>();
				
		String temp;
		for (Attributes attr : Attributes.values()) { 
			if (remainingPoints ==0 ) {
				Attribute newAttr = new Attribute(attr.toString(), 0);
				gc.attrs.add(newAttr);
			} else {

				System.out.println();
				System.out.println("You have " + remainingPoints + " points remaining");
				System.out.println("How many points would you like to assign to " + attr);
				temp = gc.lineOfText.readLine();
				gc.points = tryParse(temp);
				while (gc.points == null) {
					System.out.println("Please make sure to enter numbers only");
					System.out.println("How many points would you like to assign to " + attr);
					temp = gc.lineOfText.readLine();
					gc.points = Integer.parseInt(temp);
				}
				
				if (gc.points <= remainingPoints) {
					Attribute newAttr = new Attribute(attr.toString(), gc.points);
					gc.attrs.add(newAttr);
					remainingPoints -= gc.points;
				} else {
					Attribute newAttr = new Attribute(attr.toString(), remainingPoints);
					gc.attrs.add(newAttr);
					System.out.println("You only had " + remainingPoints + " points remaining. These points were assigned to " + attr.toString());
					remainingPoints= 0;
				}
			}

		}
		
	}

	private static JSONObject createJSONCharacterMsg(PlayerCharacter character, GameClient gc) throws JSONException {
		JSONObject jt = new JSONObject();
		//jt.put("PlayerID", gc.playerID );
		
		JSONObject J_character = new JSONObject();
		J_character.put("Name", gc.name);
		for (Attribute a : character.getAttributes()) {
			J_character.put(a.getAttributeType(), a.getAttributeValue());
		}
		for (Look l : character.getLooks()) {
			J_character.put(l.getLookType(), l.getLookValue());	
		}
		jt.put("Character", J_character);
		jt.put("Message", "0");
		return jt;
	}


	public static Integer tryParse(String text) {
		try {
			return new Integer(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
/*
	public void updateCharacter() throws IOException, JSONException {
		assignAttributes(this, 2);
		JSONObject j = new JSONObject();
		JSONObject jo = new JSONObject();
		for (Attribute a : attrs) {
			jo.put(a.getAttributeType(), a.getAttributeValue());
		}
		j.put("UpdateCharacter", jo);
		this.updateCharacterFlag = false;
	}
	*/
}
