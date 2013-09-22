package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class MagnumDI implements Container {
    private final Module module;
    private final ProviderFactory factory;
    private final PMap<Provider<?>, Object> cache;

    public MagnumDI(Object... components_classOrInstanceOrProvider) {
        this.factory = new StandardProviderFactory();
        Module module = new StandardModule()
                .add(new MissingComponentExplanationProvider<>(MagnumDI.class, "You cannot inject MagnumDI (this would cause a circular dependency).  You probably want to inject " + Container.class.getName() + " instead"));
        for (Object component : components_classOrInstanceOrProvider) {
            module = module.add(factory.getProvider(component));
        }
        this.module = module;
        this.cache = HashTreePMap.empty();
    }

    private MagnumDI(ProviderFactory factory, Module module, PMap<Provider<?>, Object> cache) {
        this.factory = factory;
        this.module = module;
        this.cache = cache;
    }

    @Override public MagnumDI add(Object... components_classOrInstanceOrProvider) {
        Module module = this.module;
        for (Object component : components_classOrInstanceOrProvider) {
            module = module.add(factory.getProvider(component));
        }
        return new MagnumDI(factory, module, cache);
    }

    @Override public <T> T get(Class<T> componentKey) {
        MagnumDI scoped = create(componentKey);
        Provider p = scoped.module.getProvider(componentKey);
        //noinspection unchecked
        return (T) scoped.cache.get(p);
    }

    @Override public MagnumDI create(Class<?>... componentKeys) {
        IncrementalContainer containerUnderConstruction = new IncrementalContainer();
        MagnumDI magnum = add(containerUnderConstruction);

        for (Class<?> componentKey : componentKeys) {
            if (magnum.module.getProvider(componentKey) == null) {
                magnum = magnum.add(componentKey);
            }
        }

        PMap<Provider<?>, Object> cache = magnum.cache;
        for (Class<?> componentKey : componentKeys) {
            cache = createAndAddTraceToExceptions(componentKey, magnum.module, cache);
        }
        final MagnumDI newMagnum = new MagnumDI(magnum.factory, magnum.module, cache);
        containerUnderConstruction.setDelegate(newMagnum);
        return newMagnum;
    }

    private static PMap<Provider<?>, Object> create(Class<?> componentKey, Module module, PMap<Provider<?>, Object> cache) {
        //noinspection unchecked
        final Provider<Object> provider = (Provider<Object>) module.getProvider(componentKey);
        if (provider == null)
            throw new RuntimeException("No provider for key: " + componentKey);

        Object instance;
        if (cache.containsKey(provider)) {
            instance = cache.get(provider);
        } else {
            final Class<?>[] dependencyKeys = provider.getDependencyKeys();
            if (dependencyKeys == null)
                throw new RuntimeException("Provider must implement getDependencyKeys(): " + provider);
            final Object[] dependencies = new Object[dependencyKeys.length];
            for (int i = 0; i < dependencyKeys.length; i++) {
                Class<?> dependencyKey = dependencyKeys[i];
                Provider p = module.getProvider(dependencyKey);
                cache = createAndAddTraceToExceptions(dependencyKey, module, cache);
                dependencies[i] = cache.get(p);
            }

            instance = provider.get(dependencies);
        }
        return cache.plus(provider, instance);
    }

    private static PMap<Provider<?>, Object> createAndAddTraceToExceptions(Class<?> dependencyKey, Module module, PMap<Provider<?>, Object> cache) {
        try {
            cache = create(dependencyKey, module, cache);
        } catch (RuntimeException e) {
            String prefix = "";
            Throwable cause = e.getCause();
            if (e.getClass() != RuntimeException.class) {
                prefix = e.getClass().getCanonicalName() + ": ";
                cause = e;
            }
            throw new RuntimeException(prefix + e.getMessage() + "\n        While finding dependency " + dependencyKey, cause);
        }
        return cache;
    }
}
