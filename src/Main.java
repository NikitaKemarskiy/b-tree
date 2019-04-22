import com.tree.BTree;
import com.tree.Entry;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Input branching factor t: ");
        int t = scan.nextInt();
        System.out.print("Input number of items: ");
        int n = scan.nextInt();

        BTree<Integer, String> tree = new BTree(t);

        int next = 0;
        for (int i = 0; i < n; i++) {
            Entry<Integer, String> entry = random(next, next + 5, 4);
            System.out.println("Insert: " + entry.toString());
            tree.insert(entry.getKey(), entry.getValue());
            next += 6;
        }
        System.out.println("B-tree: " + tree.traversal());

        System.out.println("Input key to search the value: ");
        int num = scan.nextInt();

        System.out.println(tree.search(num));
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
