package net.avh4.util.di.magnum;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorProvider<T> implements Provider<T> {
    private final Class<T> componentClass;
    private T instance;

    public ConstructorProvider(Class<T> componentClass) {
        this.componentClass = componentClass;
    }

    @Override public T get(Container container) {
        if (instance != null) return instance;
        final Constructor<T> constructor;
        constructor = findConstructor();
        final T instance;
        try {
            Object[] args = createArgs(container, constructor.getParameterTypes());
            instance = constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't instantiate " + componentClass, e);
        }
        this.instance = instance;
        return instance;
    }

    private Object[] createArgs(Container container, Class<?>[] parameterTypes) {
        final Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            args[i] = container.get(parameterType);
        }
        return args;
    }

    private Constructor<T> findConstructor() {
        Constructor<T> constructor;
        constructor = (Constructor<T>) componentClass.getConstructors()[0];
        return constructor;
    }

    @Override public String toString() {
        return "ConstructorProvider<" + componentClass.getCanonicalName() + '>';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstructorProvider that = (ConstructorProvider) o;

        if (componentClass != null ? !componentClass.equals(that.componentClass) : that.componentClass != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return componentClass != null ? componentClass.hashCode() : 0;
    }
}
