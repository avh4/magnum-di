package net.avh4.util.di.magnum;

import org.pcollections.PVector;
import org.pcollections.TreePVector;

public class AmbiguousProvider<T> implements Provider<T> {
    private final Class<T> aClass;
    private PVector<Provider<?>> providers;

    public AmbiguousProvider(Class<T> aClass, Provider<? extends T> oldProvider, Provider<? extends T> newProvider) {
        this.aClass = aClass;
        if (oldProvider instanceof AmbiguousProvider) {
            //noinspection unchecked
            providers = ((AmbiguousProvider) oldProvider).providers.plus(newProvider);
        } else {
            providers = TreePVector.<Provider<?>>singleton(oldProvider).plus(newProvider);
        }
    }

    @Override public T get(Container container) {
        throw new RuntimeException("Multiple matches for " + aClass + "\n        " + providers);
    }
}
