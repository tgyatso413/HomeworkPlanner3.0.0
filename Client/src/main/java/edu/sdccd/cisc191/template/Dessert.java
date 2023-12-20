package edu.sdccd.cisc191.template;

import java.util.Random;

public class Dessert extends Snack{

    private String[][] snackArray = new String[][]{
            {"White Bread", "Brioche", "Cookie"},
            {"Nutella", "PBJ", "Honey", "Jam"}
    };
    private Random random = new Random();
    private String snackDescription = new String();

    /**
     * Randomizes a dessert based on ingredients listed in array above
     * @return a String containing description of a dessert and its toppings
     */
    @Override
    public String getSnack() {

        String dessert = snackArray[0][random.nextInt(3)];
        String topping = snackArray[1][random.nextInt(4)];
        snackDescription = "Have some " + topping + " on " + dessert;
        return snackDescription;
    }

    /**
     * For testing purposes: tests if program can maneuver between two rows of an array
     * @return a String containing the description of a dessert
     */
    @Override
    public String getFirst() {
        String dessert = snackArray[0][0];
        String topping = snackArray[1][0];
        snackDescription = "Have some " + topping + " on " + dessert;
        return snackDescription;
    }
}