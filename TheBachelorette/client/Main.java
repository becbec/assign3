package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.MessageServer;

import game.*;
import game.Main.Attributes;
import game.Main.HairColour;
import game.Main.EyeColour;
import game.Main.BodyType;


public class Main {

	public static void main(String[] args) {
		BufferedReader lineOfText = new BufferedReader(new InputStreamReader(System.in));
		Integer points = 0;
		int remainingPoints = 20;
		String name = "";
		List<Attribute> attrs = new ArrayList<Attribute>();
		List<Look> looks = new ArrayList<Look>();
		// set the girls
		List<PlayerCharacter> girls = new ArrayList<PlayerCharacter>();
		girls = setGirls(girls);

		try { //TODO: error checking for server/port
			System.out.println("Please enter the host server");
			String server = lineOfText.readLine();
			System.out.println("Please enter the host port");
			String temp = lineOfText.readLine();
			int port = Integer.parseInt(temp);

			System.out.println("Please enter your name");
			name = lineOfText.readLine();
			System.out.println("You have " + remainingPoints + " points to assign to the following attributes: ");
			System.out.println("INTELLIGENCE, LOOKS, CHARM, HONESTY, STRENGTH, KINDNESS, HUMOUR, ");
			System.out.println("ENTHUSIASM, ADAPTABILITY, RELIABILITY, and GENEROSITY");



			for (Attributes attr : Attributes.values()) { 
				if (remainingPoints ==0 ) {
					Attribute newAttr = new Attribute(attr.toString(), 0);
					attrs.add(newAttr);
				} else {

					System.out.println();
					System.out.println("You have " + remainingPoints + " points remaining");
					System.out.println("How many points would you like to assign to " + attr);
					temp = lineOfText.readLine();
					points = tryParse(temp);
					while (points == null) {
						System.out.println("Please make sure to enter numbers only");
						System.out.println("How many points would you like to assign to " + attr);
						temp = lineOfText.readLine();
						points = Integer.parseInt(temp);
					}

					if (points <= remainingPoints) {
						Attribute newAttr = new Attribute(attr.toString(), points);
						attrs.add(newAttr);
						remainingPoints -= points;
					} else {
						Attribute newAttr = new Attribute(attr.toString(), remainingPoints);
						attrs.add(newAttr);
						System.out.println("You only had " + remainingPoints + " points remaining. These points were assigned to " + attr.toString());
						remainingPoints= 0;
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
				selection = lineOfText.readLine().toUpperCase();
				try {
					l = new Look("HAIR", HairColour.valueOf(selection).toString());
					selected = 1;
				} catch (IllegalArgumentException e) {
					//do nothing
				}
			}	

			looks.add(l);
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
				selection = lineOfText.readLine().toUpperCase();
				try {
					l = new Look("EYES", EyeColour.valueOf(selection).toString());
					selected = 1;
				} catch (IllegalArgumentException e) {
					//do nothing
				}
			}
			looks.add(l);
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
				selection = lineOfText.readLine().toUpperCase();

				try {
					l = new Look("BODY", BodyType.valueOf(selection).toString());
					selected = 1;
				} catch (IllegalArgumentException e) {
					//do nothing
				}
			}

			looks.add(l);


			PlayerCharacter character = new PlayerCharacter(name,attrs,looks);
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("Your chosen character: ");
			character.printCharacter();

			//spawns thread to connect to Server
			ClientServerConnection csConnection = new ClientServerConnection(server, port);
			Thread t = new Thread(csConnection);
			t.start();
			//spawns thread to read incoming messages + print them
			ProcessIncomingMessages pim = new ProcessIncomingMessages(csConnection);
			t = new Thread(pim);
			t.start();
			//main thread blocks and waits for user input
			String input;
			while (true) {
				input = lineOfText.readLine();
				//TODO:process input - make JSON message
				csConnection.m_outgoingMsgQueue.add(input);

			}

		} catch (IOException e) {
			System.err.println("failed in client main");
		}
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
