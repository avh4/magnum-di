package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class MagnumDI implements Container {
    private final PMap<Class<?>, Provider<?>> providers;

    public MagnumDI(Class<?>... components) {
        PMap<Class<?>, Provider<?>> newProviders = HashTreePMap.empty();
        for (Class<?> component : components) {
            newProviders = providersPlus(newProviders, component);
        }
        providers = newProviders;
    }

    protected MagnumDI(PMap<Class<?>, Provider<?>> providers) {
        this.providers = providers;
    }

    public MagnumDI add(Class<?>... componentClass) {
        PMap<Class<?>, Provider<?>> newProviders = providers;
        for (Class<?> component : componentClass) {
            newProviders = providersPlus(newProviders, component);
        }
        return new MagnumDI(newProviders);
    }

    public <T> MagnumDI add(Class<T> componentClass, Provider<T> provider) {
        return new MagnumDI(providersPlus(providers, componentClass, provider));
    }

    protected static <T> PMap<Class<?>, Provider<?>> providersPlus(PMap<Class<?>, Provider<?>> providers, Class<T> componentClass) {
        return providersPlus(providers, componentClass, new ConstructorProvider<>(componentClass));
    }

    protected static <T> PMap<Class<?>, Provider<?>> providersPlus(PMap<Class<?>, Provider<?>> providers, Class<T> componentClass, Provider<T> provider) {
        PMap<Class<?>, Provider<?>> newProviders = providers;
        for (Class<? super T> aClass : new InheritanceIterable<>(componentClass)) {
            if (newProviders.containsKey(aClass)) {
                newProviders = newProviders.plus(aClass, new AmbiguousProvider(aClass, providers.get(aClass), provider));
            } else {
                newProviders = newProviders.plus(aClass, provider);
            }
        }
        return newProviders;
    }

    @Override public <T> T get(Class<T> componentClass, Class<?>... scopedDependencies) {
        return add(scopedDependencies)._get(componentClass);
    }

    protected <T> T _get(Class<T> componentClass) {
        //noinspection unchecked
        final Provider<T> provider = (Provider<T>) providers.get(componentClass);
        if (provider == null)
            throw new RuntimeException("No provider for component: " + componentClass);

        return provider.get(this);
    }
}
