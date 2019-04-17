import com.tree.BTree;

public class Main {
    public static void main(String[] args) {
        BTree<Integer, String> tree = new BTree(3);
        tree.insert(1, "str1");
        tree.insert(21, "str2");
        tree.insert(13, "str3");
        tree.insert(4, "str4");
        tree.insert(5, "str5");
        tree.insert(68, "str6");
        tree.insert(-2, "str7");
        tree.insert(5, "str8");
        tree.insert(6, "str9");
        System.out.println(tree.traversal());
    }
}
