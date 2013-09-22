package net.avh4.util.di.magnum;

import org.pcollections.PVector;
import org.pcollections.TreePVector;

public class AmbiguousProvider<T> implements Provider<T> {
    private final Class<T> aClass;
    private final PVector<Provider<?>> providers;

    public AmbiguousProvider(Class<T> aClass, Provider<? extends T> oldProvider, Provider<? extends T> newProvider) {
        this.aClass = aClass;
        if (oldProvider instanceof AmbiguousProvider) {
            //noinspection unchecked
            providers = ((AmbiguousProvider) oldProvider).providers.plus(newProvider);
        } else {
            providers = TreePVector.<Provider<?>>singleton(oldProvider).plus(newProvider);
        }
    }

    @Override public Class<T> getProvidedClass() {
        return aClass;
    }

    @Override public Class<?>[] getDependencyKeys() {
        return new Class<?>[0];
    }

    @Override public T get(Object[] dependencies) {
        throw new RuntimeException("Multiple matches for " + aClass + "\n        " + providers);
    }
}
