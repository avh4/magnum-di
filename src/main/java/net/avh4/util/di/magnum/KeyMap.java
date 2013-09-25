package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.util.InheritanceIterable;
import org.pcollections.ConsPStack;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;
import org.pcollections.PStack;

public class KeyMap {
    public static class AmbiguousKey {
        private final PStack<Object> keys;

        public AmbiguousKey(AmbiguousKey keys, Object key) {
            this.keys = keys.keys.plus(key);
        }

        public AmbiguousKey(Object key1, Object key2) {
            this.keys = ConsPStack.singleton(key1).plus(key2);
        }

        public PStack<Object> keys() {
            return keys;
        }
    }

    private final PMap<Object, Object> map;

    public KeyMap() {
        map = HashTreePMap.empty();
    }

    private KeyMap(PMap<Object, Object> map) {
        this.map = map;
    }

    public KeyMap add(Object key) {
        if (key instanceof Class) {
            return addClass((Class) key);
        } else {
            return new KeyMap(map.plus(key, key));
        }
    }

    private KeyMap addClass(Class<?> bClass) {
        PMap<Object, Object> map = this.map;
        for (Class<?> key : new InheritanceIterable<>(bClass)) {
            map = add(map, bClass, key);
        }
        map = addPrimitive(bClass, map);
        return new KeyMap(map);
    }

    private PMap<Object, Object> addPrimitive(Class<?> bClass, PMap<Object, Object> map) {
        if (bClass == Integer.class) {
            map = add(map, bClass, Integer.TYPE);
        }
        if (bClass == Long.class) {
            map = add(map, bClass, Long.TYPE);
        }
        if (bClass == Byte.class) {
            map = add(map, bClass, Byte.TYPE);
        }
        if (bClass == Character.class) {
            map = add(map, bClass, Character.TYPE);
        }
        if (bClass == Short.class) {
            map = add(map, bClass, Short.TYPE);
        }
        if (bClass == Float.class) {
            map = add(map, bClass, Float.TYPE);
        }
        if (bClass == Double.class) {
            map = add(map, bClass, Double.TYPE);
        }
        if (bClass == Boolean.class) {
            map = add(map, bClass, Boolean.TYPE);
        }
        return map;
    }

    private PMap<Object, Object> add(PMap<Object, Object> map, Class<?> bClass, Class<?> key) {
        final Object oldValue = this.map.get(key);
        if (oldValue == null) {
            map = map.plus(key, bClass);
        } else {
            if (oldValue instanceof AmbiguousKey) {
                map = map.plus(key, new AmbiguousKey((AmbiguousKey) oldValue, bClass));
            } else {
                map = map.plus(key, new AmbiguousKey(oldValue, bClass));
            }
        }
        return map;
    }

    public Object getBestMatch(Object key) {
        return map.get(key);
    }
}
