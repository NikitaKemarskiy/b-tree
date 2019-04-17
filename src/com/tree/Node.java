package com.tree;

import java.util.List;

class Node<K extends Comparable<K>, V> {
    // Private
    private int t;
    private int n;
    private Entry<K, V>[] entries;
    private Node<K, V>[] children;
    private Node<K, V> parent;

    // Public
    Node(int t) {
        this.t = t;
        entries = new Entry[2 * t];
        children = new Node[2 * t + 1];
    }

    Node(int t, Node parent) {
        this.t = t;
        this.parent = parent;
        entries = new Entry[2 * t];
        children = new Node[2 * t + 1];
    }

    Node insert(K key, V value) { // Method that inserts passed key-value entry
        int index = find(key);
        if (isLeaf()) {
            addValue(key, value, index);
        } else {
            children[index].insert(key, value);
        }

        return split();
    }

    void remove(K key) { // Method that removes passed key-value entry
        //...
    }

    V search(K key) { // Method that searches a passed value
        int index = n;
        for (int i = 0; i < n; i++) {
            if (key.compareTo(entries[i].getKey()) == 0) { // Key was found
                return entries[i].getValue();
            }
            if (key.compareTo(entries[i].getKey()) < 0) { // Right range was found
                index = i;
                break;
            }
        }

        return isLeaf() ? null : children[index].search(key);
    }

    Node split() { // Method that splits a node into two nodes
        if (n < 2 * t) { // Node isn't overloaded
            return isRoot() ? this : parent.split();
        }
        if (isRoot()) { parent = new Node<>(t); } // There's no parent

        int index = parent.find(entries[0].getKey()); // Index of the current node as a child
        int mid = n / 2;
        Node<K, V> left = new Node<>(t, parent);
        Node<K, V> right = new Node<>(t, parent);

        for (int i = 0; i < mid; i++) {
            left.insert(entries[i].getKey(), entries[i].getValue());
        }
        for (int i = mid + 1; i < n; i++) {
            right.insert(entries[i].getKey(), entries[i].getValue());
        }

        parent.addValue(entries[mid].getKey(), entries[mid].getValue(), index);
        parent.addChildren(left, right, index);
        return parent.split();
    }

    void addValue(K key, V value, int index) {
        for (int i = n; i > index; i--) {
            entries[i] = entries[i - 1];
        }
        entries[index] = new Entry<>(key, value);
        n++;
    }

    void addChildren(Node left, Node right, int index) {
        children[index] = left;
        for (int i = n; i > index; i--) {
            children[i + 1] = children[i];
        }
        children[index + 1] = right;
    }

    int find(K key) {
        int index = n;
        for (int i = 0; i < n; i++) {
            if (key.compareTo(entries[i].getKey()) <= 0) { // Key was found
                index = i;
                break;
            }
        }
        return index;
    }

    boolean isLeaf() {
        return children[0] == null ? true : false;
    }

    boolean isRoot() {
        return parent == null ? true : false;
    }

    boolean isFull() {
        return n == (2 * t - 1) ? true : false;
    }

    boolean isEmpty() {
        return n == 0 ? true : false;
    }

    String traversal() {
        String str = toString();
        if (isLeaf() || isEmpty()) { return str; } // Node is leaf or empty
        for (int i = 0; i < n + 1; i++) {
            str += children[i].traversal();
        }
        return str;
    }

    public String toString() {
        String str = "[";
        for (int i = 0; i < n; i++) {
            str += entries[i].getKey() + ", ";
        }
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 2);
        }
        str += "]";
        return str;
    }
}
