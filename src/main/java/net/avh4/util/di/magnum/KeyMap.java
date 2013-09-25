package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.util.InheritanceIterable;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class KeyMap {
    private final PMap<Object, Object> map;

    public KeyMap() {
        map = HashTreePMap.empty();
    }

    private KeyMap(PMap<Object, Object> map) {
        this.map = map;
    }

    public KeyMap add(Object key) {
        if (map.containsKey(key)) throw new RuntimeException("Container already has a provider for key: " + key);
        if (key instanceof Class) {
            return addClass((Class) key);
        } else {
            return new KeyMap(map.plus(key, key));
        }
    }

    private KeyMap addClass(Class<?> bClass) {
        PMap<Object, Object> map = this.map;
        for (Class<?> key : new InheritanceIterable<>(bClass)) {
            map = map.plus(key, bClass);
        }
        map = addPrimitive(bClass, map);
        return new KeyMap(map);
    }

    private PMap<Object, Object> addPrimitive(Class<?> bClass, PMap<Object, Object> map) {
        if (bClass == Integer.class) {
            map = map.plus(Integer.TYPE, bClass);
        }
        if (bClass == Long.class) {
            map = map.plus(Long.TYPE, bClass);
        }
        if (bClass == Byte.class) {
            map = map.plus(Byte.TYPE, bClass);
        }
        if (bClass == Character.class) {
            map = map.plus(Character.TYPE, bClass);
        }
        if (bClass == Short.class) {
            map = map.plus(Short.TYPE, bClass);
        }
        if (bClass == Float.class) {
            map = map.plus(Float.TYPE, bClass);
        }
        if (bClass == Double.class) {
            map = map.plus(Double.TYPE, bClass);
        }
        if (bClass == Boolean.class) {
            map = map.plus(Boolean.TYPE, bClass);
        }
        return map;
    }

    public Object getBestMatch(Object key) {
        return map.get(key);
    }
}
