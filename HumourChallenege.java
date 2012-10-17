package COMP4431.src.assign3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 16/10/12
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class HumourChallenege implements Challenge{
    ArrayList<String> riddles;
    ArrayList<String> rAnswers;
    String order;

    public HumourChallenege() {
        riddles = new ArrayList<String>();
        rAnswers = new ArrayList<String>();
        order = "";

        riddles.add("When is a door not a door?");
        riddles.add("Why did the students eat their homework?");
        riddles.add("Why are there gates around graveyards?");
        riddles.add("How did the telephone propose to the lady?");
        riddles.add("Why couldn't the pirates play cards?");
        riddles.add("What did the mayonnaise say to the fridge?");
        riddles.add("Why can't you tell jokes to an egg?");
        riddles.add("What did the man say when the church burnt down?");
        riddles.add("Why did the child bring his dad to school?");
        riddles.add("Why did the kid cross the park?");
        riddles.add("Why did the scarecrow win the Nobel Prize?");
        riddles.add("What happened to the dog that swallowed a watch?");
        riddles.add("Who won when the two waves raced?");
        riddles.add("What is a cannibal's favourite game?");
        riddles.add("Why did the man with one hand cross the road?");
        riddles.add("What did the teacher say when it rained cats and dogs?");
        riddles.add("Why did the bubblegum cross the road?");
        riddles.add("Why was the ketchup last in the race?");
        riddles.add("Why didn't the skeleton cross the road?");
        riddles.add("What did the football coach say to the banker?");
        riddles.add("Why can't a nose be twelve inches long?");
        riddles.add("What did the ocean say to the beach?");
        riddles.add("What has a ring but no finger?");
        riddles.add("What street does a vampire live on?");
        riddles.add("It goes all around the world but stays in a corner. What is it?");
        riddles.add("Why was the police man in bed?");
        riddles.add("Why did the boy lock himself in the fridge?");
        riddles.add("Why did the man throw a bucket of water out the window?");
        riddles.add("Why did the man throw the butter out the window?");
        riddles.add("Why did the man put the clock in the safe?");
        riddles.add("Why did the tomato blush?");
        riddles.add("How far can a dog run into the forest?");
        riddles.add("Why are baseball stadiums so cool?");
        riddles.add("Why can't a bicycle stand on its own?");

        rAnswers.add("When it's ajar!");
        rAnswers.add("Because the teacher said it was a piece of cake!");
        rAnswers.add("Because everybody is dying to get in!");
        rAnswers.add("It gave her a ring!");
        rAnswers.add("Because the captain was sitting on the deck!");
        rAnswers.add("Close the door, I'm dressing!");
        rAnswers.add("Because it will crack up!");
        rAnswers.add("Holy smoke!");
        rAnswers.add("'Cause he had a pop quiz!");
        rAnswers.add("To get to the other slide!");
        rAnswers.add("Because he was out standing in his field!");
        rAnswers.add("It got ticks!");
        rAnswers.add("They tide!");
        rAnswers.add("Swallow the Leader!");
        rAnswers.add("To get to the second-hand shop!");
        rAnswers.add("Be careful not to step on a poodle!");
        rAnswers.add("Because it was stuck to the chicken's foot!");
        rAnswers.add("It couldn't ketch-up!");
        rAnswers.add("'Cause he didn't have the guts!");
        rAnswers.add("I want my quarter back!");
        rAnswers.add("Why can't a nose be twelve inches long?");
        rAnswers.add("Nothing. It just waved!");
        rAnswers.add("A telephone!");
        rAnswers.add("A dead end!");
        rAnswers.add("A stamp!");
        rAnswers.add("He was an undercover agent!");
        rAnswers.add("To make himself look cooler!");
        rAnswers.add("He wanted to see the waterfall!");
        rAnswers.add("He wanted to see the butterfly!");
        rAnswers.add("He wanted to save time!");
        rAnswers.add("Beacuse it saw the salad dressing!");
        rAnswers.add("Halfway, after that he is running out of the forest!");
        rAnswers.add("There is a fan in every seat!");
        rAnswers.add("Because it's two-tired");
    }

    @Override
    public String createChallenge() {
        String[] question = new String[5];
        String[] answers = new String[5];
        String[] shuffled = new String[5];
        ArrayList<Integer> seen = new ArrayList<Integer>();
        String message = "";
        String challenge = "";
        int n;
        Random generator = new Random();

        for (int i = 0; i < 5; i++) {
            n = generator.nextInt(riddles.size());
            while (seen.contains(n)) {
                n = generator.nextInt(riddles.size());
            }
            question[i] = riddles.get(n);
            answers[i] = rAnswers.get(n);
            shuffled[i] = rAnswers.get(n);
            seen.add(n);
        }
        Collections.shuffle(Arrays.asList(shuffled));

         for (int i = 0; i < 5; i++) {
             challenge+= "Q. "+question[i]+'\n';
             for (int j = 0; j < 5; j++) {
                 if (answers[i] == shuffled[j]) {
                     order+= j+" ";
                 }
             }
         }
        challenge+= '\n';
        for (int i = 0; i < 5; i++) {
            challenge+= i+". "+shuffled[i]+'\n';
        }
        System.out.println("order = "+order);
        message += "Humour Challenge\nIn this challenge you will use your homour to try and get a number off a girl" +
                "To do this you will need to match the riddle question with the riddle Answer. Do this by" +
                "typing the order the answer should be in to match up with the correct question. Type each number followed by" +
                "a space. Eg 1 4 2 5 3\n\n" + challenge+"\n\nType your answer here: \n";

        return message;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean checkAnswer(String answer) {
        String[] result = order.split(" ");
        String[] checkAnswer = answer.split(" ");

        if (result.length != checkAnswer.length) {
            return false;
        }

        for (int i = 0; i < result.length; i++) {
            if (!result[i].equals(checkAnswer[i])) {
                return false;
            }
        }

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
