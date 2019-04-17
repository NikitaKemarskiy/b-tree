package com.tree;

class Entry<K, V> {
    // Private
    private K key;
    private V value;

    // Public
    Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    // Getters
    K getKey() {
        return key;
    }

    V getValue () {
        return value;
    }
}
