package edu.sdccd.cisc191.template;

import java.util.ArrayList;
import java.util.Random;

public class Quote {
    private ArrayList<String> quoteList = new ArrayList<>();
    private Random randomGenerator = new Random();

    public void setQuote(String s) {
        quoteList.add(s);
    }
    /**

     Randomize a quote out of four options in an array
     @return a random String from an array*/
    public String getQuote() {
        int randomNum = randomGenerator.nextInt(quoteList.size());
        return quoteList.get(randomNum);}
}