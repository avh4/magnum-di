package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class StandardModule implements Module {
    private final PMap<Class<?>, Provider<?>> providers;

    public StandardModule() {
        providers = HashTreePMap.empty();
    }

    private StandardModule(PMap<Class<?>, Provider<?>> providers) {
        this.providers = providers;
    }

    @Override public <T> Provider<T> getProvider(Class<T> componentKey) {
        //noinspection unchecked
        return (Provider<T>) providers.get(componentKey);
    }

    @Override public <T> Module add(Provider<T> provider) {
        if (provider == null) return this;
        final Class<T> providedClass = provider.getProvidedClass();
        if (providedClass == null)
            throw new RuntimeException("Provider does not provide a key--you must implement getProvidedClass(): " + provider);
        return add(providedClass, provider);
    }

    private <T> StandardModule add(Class<T> componentKey, Provider<? extends T> provider) {
        PMap<Class<?>, Provider<?>> newProviders = providers;
        for (Class<? super T> aClass : new InheritanceIterable<>(componentKey)) {
            final Provider<?> oldProvider = providers.get(aClass);
            if (oldProvider == null || oldProvider instanceof MissingComponentExplanationProvider) {
                newProviders = newProviders.plus(aClass, provider);
            } else {
                //noinspection unchecked
                final AmbiguousProvider ambiguousProvider = new AmbiguousProvider(aClass, oldProvider, provider);
                newProviders = newProviders.plus(aClass, ambiguousProvider);
            }
        }
        return new StandardModule(newProviders);
    }
}
