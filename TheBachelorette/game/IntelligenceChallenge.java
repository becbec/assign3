package game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 17/10/12
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntelligenceChallenge implements Challenge{
    ArrayList<String> words;
    String chosenWords;

    public IntelligenceChallenge () {
        words = new ArrayList<String>();
        words.add("abduct");
        words.add("abused");
        words.add("active");
        words.add("across");
        words.add("advent");
        words.add("agency");
        words.add("albino");
        words.add("anchor");
        words.add("altars");
        words.add("alters");
        words.add("anneal");
        words.add("author");
        words.add("ballot");
        words.add("ballon");
        words.add("badger");
        words.add("beauty");
        words.add("beacon");
        words.add("bitten");
        words.add("bitchy");
        words.add("blouse");
        words.add("cherry");
        words.add("cheese");
        words.add("cheats");
        words.add("chrome");
        words.add("cheeky");
        words.add("corpse");
        words.add("danger");
        words.add("dagger");
        words.add("decent");
        words.add("defend");
        words.add("donuts");
        words.add("doubts");
        words.add("douche");
        words.add("earned");
        words.add("embody");
        words.add("easily");
        words.add("embark");
        words.add("enjoys");
        words.add("ethics");
        words.add("expire");
        words.add("fatten");
    }

    @Override
    public String createChallenge() {
        Random generator = new Random();
        chosenWords = "";
        String message = "";
        String challenge = "";
        String[] scramble;
        ArrayList<String> seen = new ArrayList<String>();

        for (int j = 0; j < 3; j++) {
            String tmp = words.get(generator.nextInt(words.size()));
            while (seen.contains(tmp)) {
                tmp = words.get(generator.nextInt(words.size()));
            }
            chosenWords += tmp+" ";
            seen.add(tmp);
            scramble = tmp.split("");
            Collections.shuffle(Arrays.asList(scramble));
            for (int i = 0; i < scramble.length; i++) {
                challenge+= scramble[i];
            }
            challenge+= "  ";
        }

        System.out.println("chosenword = "+chosenWords);
        message += chosenWords+"Intelligence Challenge\n\nTo pick up a girl using your intelligence you will need to unscramble a word" +
                "To do so, type what you think the unscrambled word is\n\n"+challenge+"\n\nType the answer here: \n";


        return message;
    }

    @Override
    public boolean checkAnswer(String answer) {
        String[] words = chosenWords.split(" ");
        String[] checkAns = answer.split(" ");

        if (words.length != checkAns.length) {
            return false;
        }

        for (int i = 0; i < words.length; i++) {
            if (!words[i].equals(checkAns[i])) {
                return false;
            }
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
