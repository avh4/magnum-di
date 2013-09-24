package net.avh4.util.di.magnum.util;

import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import java.util.Iterator;

public class InheritanceIterable<T> implements Iterable<Class<? super T>> {
    private PSet<Class<? super T>> classes = HashTreePSet.empty();

    public InheritanceIterable(Class<T> objectClass) {
        add(objectClass);
    }

    private void add(Class<? super T> cl) {
        if (cl == null) return;
        classes = classes.plus(cl);
        for (Class<?> in : cl.getInterfaces()) {
            //noinspection unchecked
            add((Class<? super T>) in);
        }
        add(cl.getSuperclass());
    }

    @Override public Iterator<Class<? super T>> iterator() {
        return classes.iterator();
    }
}
