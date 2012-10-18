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
	private int numberOfGirls;
	private int currentGirl;
	private double currentPoints;
	
	public PlayerCharacter(String name, List<Attribute> attributes, List<Look> looks){
		this.name = name;
		this.attributes = new ArrayList<Attribute>(attributes);
		this.looks = new ArrayList<Look>(looks);
		girlsSeen = new int[7];
		for (int i = 0; i < 7; i++) {
			girlsSeen[i] = 0;
		}
		stage = 0;
		challengeNumber = -1;
		numberOfGirls = 0;
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
	
	public boolean isGirlSeen(int i) {
		if (girlsSeen[i] == 0) { //TODO: This is causing an exception, fix this
			return false;
		}
		
		return true;
	}
	
	public void setGirlSeen() {
		girlsSeen[currentGirl] = 1;
	}
	
	public int stageNumber() {
		return stage;
	}
	
	public void updateStage(int stage) {
		this.stage = stage;
	}
	
	public String initChallenege(int n) {
		challengeNumber = n;
		
		if (n == 1) {
			currentGirl = 1;
			ch = new IntelligenceChallenge();
		} else if (n == 2) {
			currentGirl = 2;
			ch = new CharmChallenege();
		} else if (n == 3) {
			currentGirl = 3;
			ch = new HonestyChallenge();
		} else if (n == 4) {
			currentGirl = 4;
			ch = new HumourChallenege();
		} else if (n == 5) {
			currentGirl = 5;
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
	
	public void updateNumberOfGirls() {
		numberOfGirls++;
	}
	
	public String updateCurrentPoints() {
		double value = 0;
		
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
		
		if (value == 0) {
			value = 1;
		} else {
			value = value/(0.2);
		}

		currentPoints += value;
		
		return "value = " +value+" currentPoints = "+currentPoints;
	}

	public double getCurrentPoints() {
		return currentPoints;
	}
}
