package com.lib.utils;

import java.util.HashMap;
import java.util.Map;

public class ReverseMap<K, V> extends HashMap<K, V> {

    Map<V, K> reverseMap = new HashMap<>();

    @Override
    public V put(K key, V value) {
        reverseMap.put(value, key);
        return super.put(key, value);
    }

    public K getKey(V value) {
        return reverseMap.get(value);
    }

}