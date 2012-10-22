package game;

import java.util.ArrayList;
import java.util.List;

public class PlayerCharacter {
	private String playerID; 
	private String name;
	private List<Attribute> attributes;
	private List<Look> looks;
	private int[] girlsSeen;
	private int stage;
	private Challenge ch;
	private int challengeNumber;
	private String challenge;
	private int numberOfGirls;
	private int currentGirl;
	private double currentPoints;
	private double pointsNeeded;
	private int[] challenges;
	private int clothing;
	
	public int getClothing() {
		return clothing;
	}

	public void setClothing(int clothing) {
		this.clothing = clothing;
	}

	public PlayerCharacter(String name, List<Attribute> attributes, List<Look> looks){
		this.name = name;
		this.attributes = new ArrayList<Attribute>(attributes);
		this.looks = new ArrayList<Look>(looks);
		girlsSeen = new int[10];
		challenges = new int[10];
		for (int i = 0; i < 10; i++) {
			girlsSeen[i] = 0;
		}
		
		for (int i = 0; i < 10; i++) {
			challenges[i] = 0;
		}
		
		stage = 0;
		challengeNumber = -1;
		numberOfGirls = 0;
		pointsNeeded = 5;
		currentPoints = 0;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public List<Look> getLooks() {
		return looks;
	}
	
	public void setLooks(List<Look> looks) {
		this.looks = looks;
	}
	
	public void printCharacter() {
		System.out.println("NAME: " + this.name);
		System.out.println("-----------------------------");
		System.out.println("Attributes: ");
		
		for (Attribute a: this.attributes) {
			System.out.println(a.getAttributeType() + ": " + a.getAttributeValue());
		}
		
		System.out.println("-----------------------------");
		System.out.println("Looks: ");
		
		for (Look l: this.looks) {
		System.out.println(l.getLookType() + ": " + l.getLookValue());
		}
	}
	
	public boolean isGirlSeen(int n) {
		if (girlsSeen[n] == 0) {
			return false;
		}
		
		return true;
	}
	
	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public void setGirlSeen() {
		girlsSeen[currentGirl] = 1;
		currentPoints = 0;
		pointsNeeded+=5;
		numberOfGirls+=1;
		for (int i = 0; i < 10; i++) {
			challenges[i] = 0;
		}
	}
	
	public int stageNumber() {
		return stage;
	}
	
	public void updateStage(int stage) {
		this.stage = stage;
	}
	
	public void setGirl(int n) {
		currentGirl = n;
	}
	
	public String initChallenege(int n) {
		challengeNumber = n;
		
		if (n == 1) {
			challenge = "INTELLIGENCE";
			ch = new IntelligenceChallenge();
		} else if (n == 2) {
			challenge = "CHARM";
			ch = new CharmChallenege();
		} else if (n == 3) {
			challenge = "HONESTY";
			ch = new HonestyChallenge();
		} else if (n == 4) {
			challenge = "HUMOUR";
			ch = new HumourChallenege();
		} else if (n == 5) {
			challenge = "GENEROSITY";
			ch = new GenerosityChallenge();
		}
		
		return ch.createChallenge();
	}
	
	public int getChallengeNumber() {
		return challengeNumber;
	}
	
	public boolean isAnswerCorrect(String answer) {
		return ch.checkAnswer(answer);
	}
	
	public int getNumberOfGirls() {
		return numberOfGirls;
	}

	public int getCurrentGirl() {
		return currentGirl;
	}
	
	public boolean isChallengeComplete(int i) {
		if (challenges[i] == 0) {
			return false;
		} else if (challenges[i] == 1) {
			return true;
		}
		return false;
	}
	
	public void updateCurrentPoints(PlayerCharacter girl) {
		double value = 0;
		String message = "";
		
		if (challengeNumber == 1) {
			value = attributes.get(0).getAttributeValue();
		} else if (challengeNumber == 2) {
			value = attributes.get(1).getAttributeValue();
		} else if (challengeNumber == 3) {
			value = attributes.get(2).getAttributeValue();
		} else if (challengeNumber == 4) {
			value = attributes.get(3).getAttributeValue();
		} else if (challengeNumber == 5) {
			value = attributes.get(4).getAttributeValue();
		}
		
		double tmpV = value;
		
		if (value == 0) {
			value = 1;
		} else {
			value = value/(0.2);
		}

		if (girl.attributes.get(0).getAttributeType().equals(challenge)) {
			tmpV*=0.5;
		}
		
		currentPoints += value-tmpV;
		challenges[challengeNumber] = 1;
		
		if (currentPoints < 0) currentPoints = 0;

	}

	public double getCurrentPoints() {
		return currentPoints;
	}
	
	public double getPointsNeeded() {
		return pointsNeeded;
	}

	public String getName() {
		return name;
	}


}
