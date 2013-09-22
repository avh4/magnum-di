package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class StandardModule implements Module {
    private final PMap<Class<?>, Provider<?>> providers;

    public StandardModule() {
        providers = HashTreePMap.empty();
    }

    protected StandardModule(PMap<Class<?>, Provider<?>> providers) {
        this.providers = providers;
    }

    @Override public <T> Provider<? extends T> getProvider(Class<T> componentKey) {
        //noinspection unchecked
        return (Provider<? extends T>) providers.get(componentKey);
    }

    @Override public <T> Module add(Class<T> componentKey) {
        return add(componentKey, new ConstructorProvider<>(componentKey));
    }

    @Override public <T> StandardModule add(Class<T> componentKey, Provider<? extends T> provider) {
        PMap<Class<?>, Provider<?>> newProviders = providers;
        for (Class<? super T> aClass : new InheritanceIterable<>(componentKey)) {
            if (newProviders.containsKey(aClass)) {
                newProviders = newProviders.plus(aClass, new AmbiguousProvider(aClass, providers.get(aClass), provider));
            } else {
                newProviders = newProviders.plus(aClass, provider);
            }
        }
        return new StandardModule(newProviders);
    }
}
