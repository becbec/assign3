package game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 16/10/12
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenerosityChallenge implements Challenge {
    int result;

    public GenerosityChallenge() {
        result = 0;
    }


    @Override
    public String createChallenge() {
        String numbers = "";
        Random generator = new Random();
        String mathSum = "";
        String message = "";

        for (int i = 0; i < 10; i++) {
            int next = generator.nextInt(50);
            numbers+=next+"  ";
            result = result + next;
        }

        System.out.println("amount is "+result);
        message +=  "Generosity Challenge\n\nTo pick up a girl using generosity you need to buy her a drink.\n" +
                "But in order to buy her a drink you will need to calculate how much it is going to cost you.\n" +
                "To complete this challenge quickly add up these 10 numbers\n\n"+numbers+"\n\nType your answer here: \n";

        return message;
    }

    @Override
    public boolean checkAnswer(String answer) {

        if (Integer.parseInt(answer) == result) {
            return true;
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
