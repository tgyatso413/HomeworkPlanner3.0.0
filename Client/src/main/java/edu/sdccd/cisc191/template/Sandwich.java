package edu.sdccd.cisc191.template;

import java.util.Random;

public class Sandwich extends Snack{

    private String[][] snackArray = new String[][]{
            {"White Bread", "Sourdough", "Bagel"},
            {"Salami", "Ham", "Smoked Salmon", "Turkey"},
            {"Cream Cheese", "Mustard", "Mayo"},
    };

    private Random random = new Random();
    private String snackDescription = new String();
    /**
     * Randomizes a sandwich made of bread, a topping, and a sauce
     * @return a String with a randomly generated sandwich
     */
    @Override
    public String getSnack() {

        String bread = snackArray[0][random.nextInt(3)];
        String topping = snackArray[1][random.nextInt(4)];
        String sauce = snackArray[2][random.nextInt(3)];
        snackDescription = "Make a sandwich with: " + bread + ", " + topping + "\nand " + sauce;
        return snackDescription;
    }
    /**
     * For testing purposes: tests to see if overridden method is recognized
     * @return a String describing a sandwich
     */
    @Override
    public String getFirst() {
        String bread = snackArray[0][0];
        String topping = snackArray[1][0];
        String sauce = snackArray[2][0];
        snackDescription = "Make a sandwich with: " + bread + ", " + topping + "\nand " + sauce;
        return snackDescription;
    }
}
