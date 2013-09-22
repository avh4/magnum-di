package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.PMap;
import org.pcollections.PSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MagnumDI {
    PSet<Class<?>> components = HashTreePSet.empty();
    PMap<Class<?>, Object> cache = HashTreePMap.empty();

    public void add(Class<?> componentClass) {
        components = components.plus(componentClass);
    }

    public <T> T get(Class<T> componentClass) {
        if (!components.contains(componentClass))
            throw new RuntimeException("Component is not available: " + componentClass);
        if (cache.containsKey(componentClass)) return (T) cache.get(componentClass);
        final Constructor<T> constructor;
        try {
            constructor = componentClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Can't get constructor for " + componentClass, e);
        }
        final T instance;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't instantiate " + componentClass, e);
        }
        cache = cache.plus(componentClass, instance);
        return instance;
    }
}
