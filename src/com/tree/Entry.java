package com.tree;

public class Entry<K, V> {
    // Private
    private K key;
    private V value;

    // Public
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    // Getters
    public K getKey() {
        return key;
    }

    public V getValue () {
        return value;
    }

    public String toString() {
        return String.format("(%s; %s)", key, value);
    }
}
