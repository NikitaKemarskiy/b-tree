import com.tree.BTree;
import com.tree.Entry;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        BTree<Integer, String> tree;

        if (BTree.existsFile()) { // Serialized tree file exists
            tree = new BTree();
        } else { // Serialized tree file doesn't exist
            System.out.print("Input branching factor t: ");
            int t = scan.nextInt();
            tree = new BTree(t);
        }

        if (tree.isEmpty()) { // Tree is empty (there was no serialized tree file)
            System.out.print("Input number of items: ");
            int n = scan.nextInt();
            int next = 0;
            for (int i = 0; i < n; i++) {
                Entry<Integer, String> entry = random(next, next + 5, 4);
                System.out.println("Insert: " + entry.toString());
                tree.insert(entry.getKey(), entry.getValue());
                next += 6;
            }
        }
        System.out.println(tree.traversal());

        printMenu(); // Print actions menu
        while (true) {
            System.out.print("Input a number of action (Q to exit): ");
            char ch = scan.next().trim().charAt(0);
            if (Character.toLowerCase(ch) == 'q') { break; } // User wants to exit a program
            switch (ch) {
                case '1': { // Search
                    System.out.print("Input key to search the value: ");
                    int key = scan.nextInt();
                    System.out.println("Value: " + tree.search(key));
                    break;
                }
                case '2': { // Insert
                    System.out.print("Input key you want to insert: ");
                    int key = scan.nextInt();
                    System.out.print("Input value you want to insert: ");
                    String value = scan.next();
                    tree.insert(key, value);
                    break;
                }
                case '3': { // Remove
                    System.out.print("Input key to remove the value: ");
                    int key = scan.nextInt();
                    tree.remove(key);
                    break;
                }
                case '4': { // Change
                    break;
                }
                case '5': { // Output tree
                    System.out.println(tree.traversal());
                    break;
                }
                case '6': { // Print actions menu
                    printMenu();
                    break;
                }
                default: { // Wrong input
                    System.out.println("Wrong input, try again");
                }
            }
        }

        tree.save(); // Save the tree to a file
    }

    private static void printMenu() { // Method that prints actions menu
        System.out.printf("Action menu:%2s%s%n", "", "1 - search");
        System.out.printf("%14s%s%n", "", "2 - insert");
        System.out.printf("%14s%s%n", "", "3 - remove");
        System.out.printf("%14s%s%n", "", "4 - change");
        System.out.printf("%14s%s%n", "", "5 - output");
        System.out.printf("%14s%s%n", "", "6 - action menu");
    }

    private static Entry<Integer, String> random(int from, int to, int symb) {
        int num = from + (int)(Math.random() * (to - from));
        String str = "";
        for (int i = 0; i < symb; i++) {
            str += symbols.charAt((int)(Math.random() * symbols.length()));
        }
        return new Entry<>(num, str);
    }

    private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lower = upper.toLowerCase();
    private static final String digits = "0123456789";
    private static final String symbols = upper + lower + digits;
}
