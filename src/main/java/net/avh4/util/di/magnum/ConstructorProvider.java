package net.avh4.util.di.magnum;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorProvider<T> implements Provider<T> {
    private final Class<T> componentClass;
    private final Constructor<T> constructor;

    public static <T> ConstructorProvider<T> forClass(Class<T> componentClass) {
        if (componentClass.isInterface()) throw new RuntimeException("Can't create provider for interface: " + componentClass.getName());
        if (componentClass.getConstructors().length == 0) {
            throw new RuntimeException("No accessible constructors: " + componentClass);
        }
        return new ConstructorProvider<>(componentClass);
    }

    private ConstructorProvider(Class<T> componentClass) {
        this.componentClass = componentClass;
        this.constructor = findConstructor();
    }

    @Override public Class<T> getProvidedClass() {
        return componentClass;
    }

    @Override public Class<?>[] getDependencyTypes() {
        return constructor.getParameterTypes();
    }

    @Override public T get(Object... dependencies) {
        try {
            return constructor.newInstance(dependencies);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new RuntimeException("Can't instantiate " + componentClass, e);
        }
    }

    private Constructor<T> findConstructor() {
        Constructor<T> constructor;
        //noinspection unchecked
        constructor = (Constructor<T>) componentClass.getConstructors()[0];
        return constructor;
    }

    @Override public String toString() {
        return "ConstructorProvider<" + componentClass.getCanonicalName() + '>';
    }

    @SuppressWarnings("RedundantIfStatement") @Override
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
