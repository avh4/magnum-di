package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class Cache {
    private final Cache parent;
    private final int generation;
    private PMap<Object, Object> map;

    public Cache() {
        this.parent = null;
        this.generation = 0;
        map = HashTreePMap.empty();
    }

    public Cache(Cache parent) {
        this.parent = parent;
        this.generation = parent.generation + 1;
        map = HashTreePMap.empty();
    }

    public GenerationTag<Object> get(Object key) {
        //noinspection unchecked
        final Object instance = map.get(key);
        if (instance != null)
            return new GenerationTag<>(generation, instance);
        else if (parent == null) return null;
        else return parent.get(key);
    }

    public void add(Object key, Object instance, int generation) {
        if (generation > this.generation) throw new IllegalArgumentException("Requested generation (" + generation + ") is not known to this Cache: " + this);
        if (generation < 0) throw new IllegalArgumentException("generation must be >= 0");
        if (generation == this.generation) map = map.plus(key, instance);
        else parent.add(key, instance, generation);
    }

    @Override public String toString() {
        return "Cache{" +
                "generation=" + generation +
                '}';
    }
}
