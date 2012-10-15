import java.util.List;

public class Main {
	
	public enum Attributes { //Add more as we add more to game
		LOOKS, CHARM, INTELLIGENCE, HONESTY
	}
	
	public enum LookFeatures {
		HAIR_COLOUR, EYE_COLOUR, CLOTHING 
	}
	
	public enum HairColour {
		BROWN, BLACK, BLONDE, RED
	}
	
	public enum EyeColour {
		BROWN, BLUE, GREEN, GREY
	}
	
	public enum Clothing {
		SUIT, BEACHWEAR, CLUBWEAR, CASUAL
	}
	
	public List<Character> characters;

	public static void main(String[] args) {
		System.out.println("hi");
	}

}