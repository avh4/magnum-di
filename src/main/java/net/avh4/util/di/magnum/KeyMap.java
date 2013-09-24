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
            return addClass((Class)key);
        } else {
            return new KeyMap(map.plus(key, key));
        }
    }

    private KeyMap addClass(Class<?> bClass) {
        PMap<Object, Object> map = this.map;
        for (Class<?> key : new InheritanceIterable<>(bClass)) {
            final Object oldValue = this.map.get(key);
            if (oldValue == null) {
                map = map.plus(key, bClass);
            } else {
                if (oldValue instanceof AmbiguousKey) {
                    map = map.plus(key, new AmbiguousKey((AmbiguousKey)oldValue, bClass));
                } else {
                    map = map.plus(key, new AmbiguousKey(oldValue, bClass));
                }
            }
        }
        return new KeyMap(map);
    }

    public Object getBestMatch(Object key) {
        return map.get(key);
    }
}
