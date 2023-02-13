package ru.DmN.llml.utils;

import java.util.*;
import java.util.stream.Collectors;

public class VariableMap<T extends Variable> implements Map<String, T> {
    public final List<T> list;

    public VariableMap() {
        this.list = new ArrayList<>();
    }

    public VariableMap(List<T> list) {
        this.list = list;
    }

    public void add(T v) {
        if (!this.containsKey(v.name))
            list.add(v);
    }

    public void addAll(List<? extends T> c) {
        for (var e : c) {
            this.add(e);
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return list.stream().anyMatch(it -> it.name.equals(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return list.contains(value);
    }

    @Override
    public T get(Object key) {
        return list.stream().filter(it -> it.name.equals(key)).findFirst().orElse(null);
    }

    @Override
    public T put(String key, T value) {
        list.add(value);
        return value;
    }

    @Override
    public T remove(Object key) {
        var v = this.get(key);
        list.remove(v);
        return v;
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> m) {
        list.addAll(m.values());
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Set<String> keySet() {
        return list.stream().map(it -> it.name).collect(Collectors.toSet());
    }

    @Override
    public Collection<T> values() {
        return list;
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return list.stream().map(it -> new Entry<String, T>() {

            @Override
            public String getKey() {
                return it.name;
            }

            @Override
            public T getValue() {
                return it;
            }

            @Override
            public T setValue(T value) {
                throw new RuntimeException("NOT IMPL");
            }
        }).collect(Collectors.toSet());
    }
}
