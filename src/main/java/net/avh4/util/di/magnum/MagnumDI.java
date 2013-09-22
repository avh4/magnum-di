package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class MagnumDI implements Container {
    PMap<Class<?>, Provider<?>> providers = HashTreePMap.empty();
    PMap<Provider<?>, Object> cache = HashTreePMap.empty();

    public <T> void add(Class<T> componentClass) {
        add(componentClass, new ConstructorProvider<>(componentClass));
    }

    public <T> void add(Class<T> componentClass, Provider<T> provider) {
        for (Class<? super T> aClass : new InheritanceIterable<>(componentClass)) {
            if (providers.containsKey(aClass)) {
                providers = providers.plus(aClass, new AmbiguousProvider(aClass, providers.get(aClass), provider));
            } else {
                providers = providers.plus(aClass, provider);
            }
        }
    }

    @Override public <T> T get(Class<T> componentClass) {
        //noinspection unchecked
        final Provider<T> provider = (Provider<T>) providers.get(componentClass);
        if (provider == null)
            throw new RuntimeException("No provider for component: " + componentClass);

        //noinspection unchecked
        T instance = (T) cache.get(provider);
        if (instance != null) return instance;

        instance = provider.get(this);

        cache = cache.plus(provider, instance);
        return instance;
    }
}
