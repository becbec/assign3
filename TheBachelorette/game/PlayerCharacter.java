package game;

import java.util.ArrayList;
import java.util.List;

public class PlayerCharacter {
	private String name;
	private List<Attribute> attributes;
	private List<Look> looks;
	private int[] girlsSeen;
	private int stage;
	private Challenge ch;
	private int challengeNumber;
	
	public PlayerCharacter(String name, List<Attribute> attributes, List<Look> looks){
		this.name = name;
		this.attributes = new ArrayList<Attribute>(attributes);
		this.looks = new ArrayList<Look>(looks);
		girlsSeen = new int[2];
		for (int i = 0; i < 2; i++) {
			girlsSeen[i] = 0;
		}
		stage = 0;
		challengeNumber = -1;
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
		if (girlsSeen[i] == 0) {
			return false;
		}
		
		return true;
	}
	
	public void setGirlSeen(int i) {
		girlsSeen[i] = 1;
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
			ch = new IntelligenceChallenge();
			return ch.createChallenge();
		} else if (n == 2) {
			ch = new CharmChallenege();
			return ch.createChallenge();
		} else if (n == 3) {
			ch = new HonestyChallenge();
			return ch.createChallenge();
		} else if (n == 4) {
			ch = new HumourChallenege();
			return ch.createChallenge();
		} else if (n == 5) {
			ch = new GenerosityChallenge();
			return ch.createChallenge();
		}
		
		return null;
	}
	
	public int getChallengeNumber() {
		return challengeNumber;
	}
	
	public boolean isAnswerCorrect(String answer) {
		return ch.checkAnswer(answer);
	}
}
