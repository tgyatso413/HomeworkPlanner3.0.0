package edu.sdccd.cisc191.template;
public class DrinkTree {
    Node root;
    Node recent; // user's most recent drink
    static class Node {
        String drink;
        Node left, right;
        Node (String drink) {
            this.drink = drink;
            this.left = null;
            this.right = null;
        }
    }

    /**
     * Recursively adds drinks to the drink tree. If the tree is empty, adds
     * drink to root. If root has something in it, call the method again with
     * left node as the new root. If left node isn't empty, call the method
     * with right node instead.
     * @param root the Node that will be checked for
     * @param drink
     * @return a Node that was previously empty, now containing a drink
     */
    private Node insertDrinkRecursive(Node root, String drink) {
        if (root == null) {
            return new Node(drink);
        }
        else if (root.left == null) {
            root.left = insertDrinkRecursive(root.left, drink);
        }
        else {
            root.right = insertDrinkRecursive(root.right, drink);
        }
        return root;
    }

    /**
     * A method called by Client. Taking the String given by Client, this method
     * gives it to insertDrinkRecursive so insertDrinkRecursive can sort and place
     * the drink in the drink tree.
     * @param drink a String, provided by Client, that will be added to the drink tree
     */
    public void insertDrink(String drink) {
        root = insertDrinkRecursive(root,drink);
    }

    /**
     * Checks to see if user has had the drink in root recently. If not, set the most
     * recent drink to root and return it. Otherwise, check left node, then left node.
     * @return a String of a drink not tried by the user recently
     */
    public String getDrink() {
        //return drink, avoiding the drink the user already had
        if (recent != root) {
            recent = root; //sets the user's most recent drink to the one they're about to have
            return root.drink;
        }
        else if (recent != root.left) {
            recent = root.left;
            return root.left.drink;
        }
        else {
            recent = root.right;
            return root.right.drink;
        }
    }

}
