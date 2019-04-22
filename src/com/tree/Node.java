package com.tree;

import java.util.List;

class Node<K extends Comparable<K>, V> {
    // Private
    private int t;
    private int n;
    private Entry<K, V>[] entries;
    private Node<K, V>[] children;
    private Node<K, V> parent;

    private Node insertRepeated(K key, V value) { // Method that inserts passed key-value entry
        int index = find(key);
        if (isLeaf()) {
            addEntry(key, value, index);
            return split();
        } else {
            return children[index].insertRepeated(key, value);
        }
    }

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

    Node remove(K key) { // Method that removes passed key-value entry
        if (isEmpty()) { return getRoot(); } // Node is empty
        int entryIndex = find(key);
        if (has(key)) { // Item was found
            return removeEntry(entryIndex);
        } else if (!isLeaf()) { // Range was found
            if (n < t && !isRoot()) { // Node has not enough items
                int index = parent.find(entries[0].getKey()); // Index of the current node as a child
                if (index > 0) { // Take the left neighbour
                    Node<K, V> neighbour = parent.children[index - 1]; // Left neighbour
                    if (neighbour.n > t - 1) { // Left neighbour has enough entries
                        Entry<K, V> delim = neighbour.entries[neighbour.n - 1]; // Delimiter entry

                        addChild(neighbour.children[neighbour.n], 0); // Insert new child node
                        addEntry(parent.entries[index - 1].getKey(), parent.entries[index - 1].getValue(), 0); // Insert new entry

                        parent.entries[index - 1] = delim; // Change parent entry

                        neighbour.n--; // Decrease neighbour size

                        return children[entryIndex + 1].remove(key);
                    }
                }
                if (index < parent.n) { // Take the right neighbour
                    Node<K, V> neighbour = parent.children[index + 1]; // Right neighbour
                    if (neighbour.n > t - 1) { // Right neighbour has enough entries
                        Entry<K, V> delim = neighbour.entries[0]; // Delimiter entry

                        children[n + 1] = neighbour.children[0]; // Insert new child node
                        addEntry(parent.entries[index].getKey(), parent.entries[index].getValue(), n); // Insert new entry

                        parent.entries[index] = delim; // Change parent entry

                        for (int i = 0; i < neighbour.n - 1; i++) { // Delete entry from neighbour
                            neighbour.entries[i] = neighbour.entries[i + 1];
                        }
                        for (int i = 0; i < neighbour.n; i++) { // Delete child from neighbour
                            neighbour.children[i] = neighbour.children[i + 1];
                        }
                        neighbour.n--; // Decrease neighbour size

                        return children[entryIndex].remove(key);
                    }
                }
                // Both left and right neighbours contain t - 1 items
                if (index > 0) { // Take the left neighbour
                    Node<K, V> node = new Node<>(t);
                    Node<K, V> neighbour = parent.children[index - 1]; // Left neighbour

                    // Fill node with entries
                    for (int i = 0; i < n; i++) {
                        node.insert(entries[i].getKey(), entries[i].getValue());
                    }
                    for (int i = 0; i < neighbour.n; i++) {
                        node.insert(neighbour.entries[i].getKey(), neighbour.entries[i].getValue());
                    }
                    node.insert(parent.entries[index - 1].getKey(), parent.entries[index - 1].getValue());

                    // Fill node with children
                    for (int i = 0; i <= neighbour.n; i++) {
                        node.children[i] = neighbour.children[i];
                        neighbour.children[i].setParent(node);
                    }
                    for (int i = 0; i <= n; i++) {
                        node.children[i + neighbour.n + 1] = children[i];
                        children[i].setParent(node);
                    }

                    // Remove entry in parent
                    for (int i = index - 1; i < parent.n - 1; i++) {
                        parent.entries[i] = parent.entries[i + 1];
                    }
                    parent.replaceTwoChildren(node, index - 1);
                    parent.n--;

                    if (!parent.isEmpty()) { // Parent is still not empty
                        node.setParent(parent); // Set parent as new node's parent
                    }

                    // Remove node
                    return node.remove(key);
                }
                if (index < parent.n) { // Take the right neighbour
                    Node<K, V> node = new Node<>(t);
                    Node<K, V> neighbour = parent.children[index + 1]; // Right neighbour

                    // Fill node with entries
                    for (int i = 0; i < n; i++) {
                        node.insert(entries[i].getKey(), entries[i].getValue());
                    }
                    for (int i = 0; i < neighbour.n; i++) {
                        node.insert(neighbour.entries[i].getKey(), neighbour.entries[i].getValue());
                    }
                    node.insert(parent.entries[index].getKey(), parent.entries[index].getValue());

                    // Fill node with children
                    for (int i = 0; i <= n; i++) {
                        node.children[i] = children[i];
                        children[i].setParent(node);
                    }
                    for (int i = 0; i <= neighbour.n; i++) {
                        node.children[i + n + 1] = neighbour.children[i];
                        neighbour.children[i].setParent(node);
                    }

                    // Remove entry in parent
                    for (int i = index; i < parent.n - 1; i++) {
                        parent.entries[i] = parent.entries[i + 1];
                    }
                    parent.replaceTwoChildren(node, index);
                    parent.n--;

                    if (!parent.isEmpty()) { // Parent is still not empty
                        node.setParent(parent); // Set parent as new node's parent
                    }

                    // Remove node
                    return node.remove(key);
                }
            }
            return children[entryIndex].remove(key);
        }
        return getRoot();
    }

    Node removeEntry(int index) {
        return isLeaf() ? removeLeafEntry(index) : removeInnerEntry(index);
    }

    Node removeLeafEntry(int entryIndex) {
        for (int i = entryIndex; i < n - 1; i++) {
            entries[i] = entries[i + 1];
        }
        n--;

        if (n >= t - 1 || isRoot()) { return getRoot(); } // Node shouldn't be rebuilt

        int index = parent.find(entries[0].getKey()); // Index of the current node as a child
        if (index > 0) { // Take the left neighbour
            Node<K, V> neighbour = parent.children[index - 1]; // Left neighbour
            if (neighbour.n > t - 1) { // Left neighbour has enough entries
                Entry<K, V> delim = neighbour.entries[neighbour.n - 1]; // Delimiter entry
                neighbour.removeEntry(neighbour.n - 1);
                insert(parent.entries[index - 1].getKey(), parent.entries[index - 1].getValue());
                parent.entries[index - 1] = delim;
                return getRoot();
            }
        }
        if (index < parent.n) { // Take the right neighbour
            Node<K, V> neighbour = parent.children[index + 1]; // Right neighbour
            if (neighbour.n > t - 1) { // Right neighbour has enough entries
                Entry<K, V> delim = neighbour.entries[0]; // Delimiter entry
                neighbour.removeEntry(0);
                insert(parent.entries[index].getKey(), parent.entries[index].getValue());
                parent.entries[index] = delim;
                return getRoot();
            }
        }
        if (index > 0) { // Take the left neighbour
            insert(parent.entries[index - 1].getKey(), parent.entries[index - 1].getValue());
            return parent.removeEntry(index - 1);
        }
        if (index < parent.n) { // Take the right neighbour
            insert(parent.entries[index].getKey(), parent.entries[index].getValue());
            return parent.removeEntry(index);
        }
        return getRoot();
    }

    Node removeInnerEntry(int index) {
        if (children[index].n > t - 1) { // Left child has enough items
            Entry<K, V> delim = children[index].entries[children[index].n - 1];
            entries[index] = delim;
            return children[index].removeEntry(children[index].n - 1);
        }
        if (children[index + 1].n > t - 1) { // Right child has enough items
            Entry<K, V> delim = children[index + 1].entries[0];
            entries[index] = delim;
            return children[index + 1].removeEntry(0);
        }

        Node<K, V> node = new Node<>(t);
        K key = entries[index].getKey();

        // Fill node with entries
        for (int i = 0; i < children[index].n; i++) {
            node.insert(children[index].entries[i].getKey(), children[index].entries[i].getValue());
        }
        for (int i = 0; i < children[index + 1].n; i++) {
            node.insert(children[index + 1].entries[i].getKey(), children[index + 1].entries[i].getValue());
        }
        node.insertRepeated(entries[index].getKey(), entries[index].getValue());

        // Children aren't leaf nodes
        if (!children[index].isLeaf()) {
            // Fill node with children
            for (int i = 0; i <= children[index].n; i++) {
                node.children[i] = children[index].children[i];
                children[index].children[i].setParent(node);
            }
            for (int i = 0; i <= children[index + 1].n; i++) {
                node.children[i + children[index].n + 1] = children[index + 1].children[i];
                children[index + 1].children[i].setParent(node);
            }
        }

        // Replace two children with a new node
        replaceTwoChildren(node, index);

        // Remove entry in current node
        for (int i = index; i < n - 1; i++) {
            entries[i] = entries[i + 1];
        }
        n--;

        if (!isEmpty()) { // Current node is still not empty
            node.setParent(this);
        }

        // Remove node
        return node.remove(key);
    }

    V search(K key) { // Method that searches a passed value
        int high = n - 1;
        int low = 0;
        int comparisons = 0;
        while (high >= low) {
            int guess = (low + high) / 2;
            comparisons++;
            if (entries[guess].getKey().compareTo(key) > 0) {
                if (guess > 0 && entries[guess - 1].getKey().compareTo(key) < 0) {
                    //System.out.println("Comparisons: " + comparisons);
                    return children[guess].search(key);
                }
                high = guess - 1;
            } else if (entries[guess].getKey().compareTo(key) < 0) {
                low = guess + 1;
            } else {
                //System.out.println("Comparisons: " + comparisons);
                return entries[guess].getValue();
            }
        }
        //System.out.println("Comparisons: " + comparisons);
        return isLeaf() ? null : high < 0 ? children[0].search(key) : children[n].search(key);
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

        parent.replaceOneChild(left, right, index);
        parent.addEntry(entries[mid].getKey(), entries[mid].getValue(), index);
        return parent.split();
    }

    void addEntry(K key, V value, int index) { // Method that adds an entry into the passed index position
        for (int i = n; i > index; i--) {
            entries[i] = entries[i - 1];
        }
        entries[index] = new Entry<>(key, value);
        n++;
    }

    void addChild(Node node, int index) { // Method that adds a child into the passed index position
        for (int i = n + 1; i > index; i--) {
            children[i] = children[i - 1];
        }
        children[index] = node;
    }

    void replaceOneChild(Node left, Node right, int index) { // Method that replaces one child node with two children nodes
        children[index] = left;
        for (int i = n; i > index; i--) {
            children[i + 1] = children[i];
        }
        children[index + 1] = right;
    }

    void replaceTwoChildren(Node node, int index) { // Method that replaces two children nodes with one child
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

    Node getRoot() {
        return isRoot() || parent.isEmpty() ? this : parent.getRoot();
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
