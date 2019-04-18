package com.tree;

public class BTree<K extends Comparable<K>, V> {
    // Private
    private Node<K, V> root;
    private int t;
    private int size;

    private static int defaultT = 50;

    // Public
    public BTree() {
        t = defaultT;
    }

    public BTree(int t) {
        this.t = t;
    }

    // Methods
    public V search(K key) {
        return root == null ? null : root.search(key);
    }

    public void insert(K key, V value) { // Method that inserts passed key-value entry
        if (root == null) { // Tree is empty
            root = new Node<>(t);
        }
        root = root.insert(key, value);
    }

    public void remove(K key) { // Method that removes passed key-value entry
        if (root == null) { // Tree is empty
            return;
        }
        root = root.remove(key);
    }

    public String traversal() {
        return root == null ? null : root.traversal();
    }

    // Getters
    public int getSize() {
        return size;
    }

    public int getT() {
        return t;
    }
}
