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
        if (has(key)) { return split(); } // Tree already contains such value
        if (isLeaf()) {
            addEntry(key, value, index);
            return split();
        } else {
            return children[index].insert(key, value);
        }
    }

    void remove(K key) { // Method that removes passed key-value entry
        if (isEmpty()) { return; } // Node is empty
        int index = find(key);
        if (index < n && key.compareTo(entries[index].getKey()) == 0) { // Item was found
            removeEntry(index);
        } else if (!isLeaf()) { // Range was found
            children[index].remove(key);
        }
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

        // Fill left and right nodes with entries
        for (int i = 0; i < mid; i++) {
            left.insert(entries[i].getKey(), entries[i].getValue());
        }
        for (int i = mid + 1; i < n; i++) {
            right.insert(entries[i].getKey(), entries[i].getValue());
        }

        // Fill left and right nodes with children
        for (int i = 0; i <= mid; i++) {
            left.children[i] = children[i];
            if (children[i] != null) {
                children[i].setParent(left);
            }
        }
        for (int i = mid + 1; i <= n; i++) {
            right.children[i - mid - 1] = children[i];
            if (children[i] != null) {
                children[i].setParent(right);
            }
        }

        parent.addChildren(left, right, index);
        parent.addEntry(entries[mid].getKey(), entries[mid].getValue(), index);
        return parent.split();
    }

    void unite() {
        if (n >= t - 1) { return; } // Node shouldn't be rebuilt
        if (isRoot()) { return; } // Node is a root

        int index = parent.find(entries[0].getKey()); // Index of the current node as a child
        if (index > 0) { // Take the left neighbour
            Node<K, V> neighbour = parent.children[index - 1]; // Left neighbour
            if (neighbour.n > t - 1) { // Left neighbour has enough entries
                Entry<K, V> delim = neighbour.entries[neighbour.n - 1]; // Delimiter entry
                neighbour.removeEntry(neighbour.n - 1);
                insert(parent.entries[index - 1].getKey(), parent.entries[index - 1].getValue());
                parent.entries[index - 1] = delim;
                return;
            }
        }
        if (index < parent.n) { // Take the right neighbour
            Node<K, V> neighbour = parent.children[index + 1]; // Right neighbour
            if (neighbour.n > t - 1) { // Right neighbour has enough entries
                Entry<K, V> delim = neighbour.entries[0]; // Delimiter entry
                neighbour.removeEntry(0);
                insert(parent.entries[index].getKey(), parent.entries[index].getValue());
                parent.entries[index] = delim;
                return;
            }
        }
        if (index > 0) { // Take the left neighbour
            Node<K, V> node = new Node<>(t, parent);
            Node<K, V> neighbour = parent.children[index - 1]; // Left neighbour
            for (int i = 0; i < neighbour.n; i++) {
                node.insert(neighbour.entries[i].getKey(), neighbour.entries[i].getValue());
            }
            for (int i = 0; i < n; i++) {
                node.insert(entries[i].getKey(), entries[i].getValue());
            }
            node.insert(parent.entries[index - 1].getKey(), parent.entries[index - 1].getValue());
            parent.replaceChildren(index - 1, node);
            parent.removeEntry(index - 1);
            return;
        }
        if (index < parent.n) { // Take the right neighbour
            Node<K, V> node = new Node<>(t, parent);
            Node<K, V> neighbour = parent.children[index + 1]; // Right neighbour
            for (int i = 0; i < neighbour.n; i++) {
                node.insert(neighbour.entries[i].getKey(), neighbour.entries[i].getValue());
            }
            for (int i = 0; i < n; i++) {
                node.insert(entries[i].getKey(), entries[i].getValue());
            }
            node.insert(parent.entries[index].getKey(), parent.entries[index].getValue());
            parent.replaceChildren(index, node);
            parent.removeEntry(index);
            return;
        }
    }

    void addEntry(K key, V value, int index) {
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

    void removeEntry(int index) {
        if (isLeaf()) { // Node is a leaf
            for (int i = index; i < n - 1; i++) {
                entries[i] = entries[i + 1];
            }
            n--;
            unite();
        } else { // Node isn't a leaf
            // TEST CODE
            for (int i = index; i < n - 1; i++) {
                entries[i] = entries[i + 1];
            }
            n--;
            // TEST CODE
        }
    }

    void replaceChildren(int index, Node node) {
        children[index] = node;
        for (int i = index + 1; i < n; i++) {
            children[i] = children[i + 1];
        }
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

    boolean has(K key) {
        for (int i = 0; i < n; i++) {
            if (key.compareTo(entries[i].getKey()) == 0) { // Key was found
                return true;
            }
        }
        return false;
    }

    boolean isLeaf() {
        return children[0] == null ? true : false;
    }

    boolean isRoot() {
        return parent == null ? true : false;
    }

    boolean isEmpty() {
        return n == 0 ? true : false;
    }

    void setParent(Node parent) {
        this.parent = parent;
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
