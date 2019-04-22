package com.tree;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BTree<K extends Comparable<K>, V> {
    // Private
    private Node<K, V> root;
    private int t;
    private String treeData;

    private static int defaultT = 50;
    private static File treeFile;

    // Static initialization block
    static {
        treeFile = new File("data", "tree");
    }

    private void parseTreeData(String data) {
        root = null;
        treeData = "";
        String buff = "";
        for (int i = 0; i < data.length(); i++) {
            if (i < data.length() - 1 && data.charAt(i + 1) == '(') {
                switch (data.charAt(i)) {
                    case 't': {
                        buff = "t";
                        continue;
                    }
                    case 'i': {
                        buff = "i";
                        continue;
                    }
                    case 'r': {
                        buff = "r";
                        continue;
                    }
                }
            }
            switch (data.charAt(i)) {
                case '(': {
                    buff += "(";
                    break;
                }
                case ')': {
                    buff += ")";
                    switch (buff.charAt(0)) {
                        case 't': {
                            this.t = Integer.parseInt(buff.substring(2, buff.length() - 1));
                            treeData += buff;
                            buff = "";
                            break;
                        }
                        case 'i': {
                            K key = (K) Integer.valueOf(Integer.parseInt(buff.substring(2, buff.indexOf(','))));
                            V value = (V) buff.substring(buff.indexOf(',') + 1, buff.length() - 1);
                            insert(key, value);
                            treeData += buff;
                            buff = "";
                            break;
                        }
                        case 'r': {
                            K key = (K) Integer.valueOf(Integer.parseInt(buff.substring(2, buff.length() - 1)));
                            remove(key);
                            treeData += buff;
                            buff = "";
                            break;
                        }
                        default: {
                            buff = "";
                            break;
                        }
                    }
                    break;
                }
                default: {
                    buff += data.charAt(i);
                    break;
                }
            }
        }
    }

    // Public
    public BTree() {
        this(defaultT);
    }

    public BTree(int t) {
        read();
        if (!isEmpty()) { return; } // Tree isn't empty
        this.t = t;
        treeData = String.format("t(%s)", t);
    }

    // Methods
    public V search(K key) {
        return root == null ? null : root.search(key);
    }

    public void insert(K key, V value) { // Method that inserts passed key-value entry
        if (root == null) { // Tree is empty
            root = new Node<>(t);
        }
        if (root.search(key) == null) { treeData += "i(" + key + "," + value + ")"; } // Tree has no such key yet
        root = root.insert(key, value);
    }

    public void remove(K key) { // Method that removes passed key-value entry
        if (root == null) { // Tree is empty
            return;
        }
        if (root.search(key) != null) { treeData += "r(" + key + ")"; } // Tree has such key
        root = root.remove(key);
    }

    public String traversal() {
        return root == null ? null : root.traversal();
    }

    public void read() {
        if (!existsFile()) { return; } // Serialized tree file doesn't exist
        try (FileReader reader = new FileReader(treeFile)) {
            String data = "";
            int ch = reader.read();
            while (ch != -1) {
                data += (char) ch;
                ch = reader.read();
            }
            if (data.length() > 0) {
                parseTreeData(data);
            }
        } catch (IOException err) {
            System.out.println("Error: " + err.getMessage());
            System.exit(1);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(treeFile)) {
            writer.write(treeData);
        } catch (IOException err) {
            System.out.println("Error: " + err.getMessage());
            System.exit(1);
        }
    }

    public boolean isEmpty() {
        return root == null ? true : root.isEmpty();
    }

    // Getters
    public int getT() {
        return t;
    }

    // Static
    public static boolean existsFile() {
        return treeFile.exists();
    }
}
