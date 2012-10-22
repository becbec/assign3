package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 17/10/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class HonestyChallenge implements Challenge {
    ArrayList<String> facts;
    int falseFact;
    ArrayList<String> nonfacts;


    public HonestyChallenge() {
        facts = new ArrayList<String>();
        nonfacts = new ArrayList<String>();

        facts.add("111,111,111 x 111,111,111 = 12,345,678,987,654,321");
        facts.add("A dragonfly has a lifespan of 24 hours.");
        facts.add("A jellyfish is 95 percent water.");
        facts.add("A pig's orgasm lasts for 30 minutes.");
        facts.add("A skunk can spray its stinky scent more than 10 feet.");
        facts.add("Bubble gum contains rubber.");
        facts.add("Cat's urine glows under a blacklight.");
        facts.add("Stewardesses is the longest word typed with only the left hand.");
        facts.add("Shakespeare invented the word ' assassination' and 'bump'.");
        facts.add("The electric chair was invented by a dentist.");
        facts.add("A newborn kangaroo is about 1 inch in length.");
        facts.add("A cow gives nearly 200,000 glasses of milk in her lifetime.");
        facts.add("There are 701 types of pure breed dogs");
        facts.add("Snakes are immune to their own poison.");
        facts.add("A shrimp's heart is in their head.");
        facts.add("A woodpecker can peck twenty times a second.");
        facts.add("Tigers have striped skin, not just striped fur.");
        facts.add("Mosquitoes have teeth.");
        facts.add("More people are killed annually by donkeys than die in air crashes.");
        facts.add("More people use blue toothbrushes, than red ones.");
        facts.add("A pregnant goldfish is called a twit");
        facts.add("The animal responsible for the most human deaths world-wide is the mosquito");
        facts.add("A bird requires more food in proportion to its size than a baby or a cat.");
        facts.add("Coca-Cola was originally green.");
        facts.add("A cat's urine glows under a blacklight");
        facts.add("Starfish don't have brains.");
        facts.add("The strongest muscle in the body is the tongue.");
        facts.add("Every person has a unique tongue print.");
        facts.add("Giraffes have no vocal cords.");
        facts.add("\"I am.\" is the shortest complete sentence in the English language.");
        facts.add("Most dust particles in your house are made from dead skin.");
        facts.add("Pinocchio is Italian for \"pine head.\"");
        facts.add("Rubber bands last longer when refrigerated.");
        facts.add("Your heart beats over 100,000 times a day.");

        nonfacts.add("A cockroach will live ten days without its head, before it starves to death.");
        nonfacts.add("Cats have 10 vocal sounds, while dogs more than one hundred");
        nonfacts.add("An iguana cannot stay under water for 28 minutes.");
        nonfacts.add("Pigs, walruses and light-colored horses cannot be sunburned.");
        nonfacts.add("A cat can see directly under its nose");
        nonfacts.add("A polecat is a cat.");
        nonfacts.add("The Canary Islands were named after a bird called a canary.");
        nonfacts.add("The mouse is the least common mammal in the US");
        nonfacts.add("Owls have eyeballs that are tubular in shape, because of this, they can move their eyes.");
        nonfacts.add("Ants do sleep.");
        nonfacts.add("The ant always falls over on its left side when intoxicated.");
        nonfacts.add("A newborn kangaroo is about half an inch in length.");
        nonfacts.add("Hawaiian alphabet has 24 letters.");
        nonfacts.add("An ostrich's eye is smaller that it's brain.");
        nonfacts.add("You are less likely to be killed by a champagne cork than by a poisonous spider.");
        nonfacts.add("A crocodile can stick its tongue out.");
        nonfacts.add("Polar bears are right handed.");
        nonfacts.add("Elephants can jump.");

    }

    @Override
    public String createChallenge() {
        String message = "";
        String challenge = "";
        String[] shuffle = new String[5];
        Random generator = new Random();
        ArrayList<Integer> seen = new ArrayList<Integer>();

        for (int i = 0; i < 4; i++) {
            int n = generator.nextInt(facts.size());
            while (seen.contains(n)) {
                n = generator.nextInt(facts.size());
            }
            seen.add(n);
            shuffle[i] = facts.get(i);
        }

        int n = generator.nextInt(nonfacts.size());
        shuffle[4] = nonfacts.get(n);

        Collections.shuffle(Arrays.asList(shuffle));

        for (int i = 0; i < shuffle.length; i++) {
            challenge+= i+". "+shuffle[i]+'\n';
            if (shuffle[i].equals(nonfacts.get(n))) {
                falseFact = i;
            }
        }

        System.out.println("false fact = "+falseFact);
        message+= "Honesty Challenge\n\nTo pick up a girl using honesty you need to figure out which is the incorrect " +
                "fact among all of the correct facts. \nTo do so type the number of the fact that you think is incorrect\n\n" +
                challenge+"\n\nType your answer here\n";

        return message;
    }

    @Override
    public boolean checkAnswer(String answer) {

        if (Integer.parseInt(answer) == falseFact) {
            return true;
        }

        return false;
    }
}
