package net.avh4.util.di.magnum;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorProvider<T> implements Provider<T> {
    private final Class<T> componentClass;

    public ConstructorProvider(Class<T> componentClass) {
        this.componentClass = componentClass;
    }

    @Override public T get(Container container) {
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
        return instance;
    }

    @Override public String toString() {
        return "ConstructorProvider<" + componentClass.getCanonicalName() + '>';
    }
}
