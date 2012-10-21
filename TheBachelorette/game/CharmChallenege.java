package game;

import java.util.ArrayList;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 15/10/12
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class CharmChallenege implements Challenge {
    ArrayList<String> pickUps;
    int pickUpIndex;
    int pickUpLength;
    String order = "";

    public CharmChallenege () {
        pickUps = new ArrayList<String>();
        pickUps.add("I just wanted to show this rose how incredibly beautiful you are!");
        pickUps.add("Is there an airport nearby or is that just my heart taking off?");
        pickUps.add("There must be something wrong with my eyes, I can't take them off you.");
        pickUps.add("Is it hot in here or is it just you?");
        pickUps.add("Are you from Tennessee? Because you're the only ten I see!");
        pickUps.add("I've seem to have lost my number, can I have yours?");
        pickUps.add("Kiss me if I'm wrong, but haven't we met before?");
        pickUps.add("Is there a Rainbow, because you're the treasure I've been searching for.");
        pickUps.add("If I could rearrange the alphabet I'd put U and I together");
        pickUps.add("Do you have a map? I just keep on getting lost in your eyes.");
        pickUps.add("I hope you know CPR, because you take my breath away!");
        pickUps.add("I seemed to have lost my way, would you mind taking me with you.");
        pickUps.add("You're so hot you would make the devil sweat.");
        pickUps.add("Was your father an alien? Because there's nothing else like you on earth!");
        pickUps.add("You must be a magnet, because it looks like you are attracted to my buns of steel.");
        pickUps.add("My lips are skittles, wanna taste the rainbow?");
        pickUps.add("Excuse me, do you have any raisins? How about a date?");
        pickUps.add("Well, here I am. What were your other two wishes?");
        pickUps.add("So, do you have a new years resolution, I'm looking at mine right now.");
        pickUps.add("Somebody call the cops, because it's got to be illegal to look that good!");
        pickUps.add("Do you have any raisins? No? How about a date?");
        pickUps.add("Do you have an eraser? Because I can't get you out of my mind.");
        pickUps.add("Your name must be Mickey because you're so fine.");
    }

    @Override
    public String createChallenge() {
        Random generator = new Random();
        pickUpIndex = generator.nextInt(pickUps.size());
        String[] pickUpLine = pickUps.get(pickUpIndex).split(" ");
        String[] shuffled = pickUps.get(pickUpIndex).split(" ");
        Collections.shuffle(Arrays.asList(shuffled));
        String challenge = "";
        String message = "";

        pickUpLength = pickUpLine.length;

        for (int i = 0; i < pickUpLength; i++) {
            //String word = pickUpLine[i];
            challenge+=i+"."+shuffled[i]+"   ";
            /*for (int j = 0; j < pickUpLength; j++) {
                if (word.equals(shuffled[j])) {
                    order+=j+" ";
                }
            }*/
        }
        System.out.println("Order "+order);
        message+="Charm Challenge!\n\nTo pick up a girl using your charm you will need to rearrange the pickup line " +
                "so that it is in the correct order\nTo do this, type the line exactly how it should appear." +
                " Once you think you have the write answer press enter\n\n"+challenge+"\n\nType your answer here: \n";

        return message;
    }

    @Override
    public boolean checkAnswer(String answer) {
       // String[] orderNumber = order.split(" ");
       // String[] answerNumber = answer.split(" ");

        /*if (pickUpLength != answerNumber.length) {
            return false;
        }

        for (int i = 0; i < pickUpLength; i++) {
            if (!orderNumber[i].equals(answerNumber[i])) {
                return false;
            }
        }*/
        if (answer.equals(pickUps.get(pickUpIndex))) {
        	return true;
        }

        return false;
    }
}
