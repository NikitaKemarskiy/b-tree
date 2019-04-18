import com.tree.BTree;

public class Main {
    public static void main(String[] args) {
        BTree<Integer, String> tree = new BTree(3);

        tree.insert(1, "1");
        tree.insert(2, "1");
        tree.insert(3, "1");
        tree.insert(4, "1");
        tree.insert(5, "1");
        tree.insert(6, "1");
        tree.insert(7, "1");
        tree.insert(8, "1");
        tree.insert(9, "1");
        tree.insert(10, "1");
        tree.insert(11, "1");
        tree.insert(12, "1");
        tree.insert(13, "1");
        tree.insert(14, "1");
        tree.insert(15, "1");
        tree.insert(16, "1");
        tree.insert(17, "1");
        tree.insert(18, "1");
        tree.insert(19, "1");
        tree.insert(20, "1");
        tree.insert(21, "1");
        tree.insert(22, "1");
        tree.insert(23, "1");
        tree.insert(24, "1");
        tree.insert(25, "1");
        tree.insert(26, "1");
        tree.insert(27, "1");
        tree.insert(28, "1");
        tree.insert(29, "1");
        tree.insert(30, "1");
        tree.insert(31, "1");
        tree.insert(32, "1");
        tree.insert(33, "1");
        tree.insert(34, "1");

        System.out.println(tree.traversal());
        tree.remove(34);
        System.out.println(tree.traversal());
        tree.remove(33);
        System.out.println(tree.traversal());
        tree.remove(30);
        System.out.println(tree.traversal());
        tree.remove(29);
        System.out.println(tree.traversal());
        tree.remove(31);
        System.out.println(tree.traversal());
        tree.remove(28);
        System.out.println(tree.traversal());
        tree.remove(32);
        System.out.println(tree.traversal());
        tree.remove(27);
        System.out.println(tree.traversal());
        tree.remove(26);
        System.out.println(tree.traversal());
        tree.remove(25);
        System.out.println(tree.traversal());
        tree.remove(18);
        System.out.println(tree.traversal());
        tree.remove(19);
        System.out.println(tree.traversal());
        tree.remove(20);
        System.out.println(tree.traversal());
        tree.remove(21);
        System.out.println(tree.traversal());
    }
}
