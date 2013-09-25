package net.avh4.util.di.magnum;

import java.util.Arrays;

public class MagnumDI {
    private final ProviderFactory factory;
    private final KeyMap keyMap;
    private final Module module;
    private final Cache cache;

    public MagnumDI() {
        keyMap = new KeyMap();
        module = new Module();
        factory = new StandardProviderFactory();
        cache = new Cache();
    }

    public MagnumDI(Object... components_classOrInstanceOrProvider) {
        MagnumDI template = new MagnumDI().add(components_classOrInstanceOrProvider);
        keyMap = template.keyMap;
        module = template.module;
        factory = template.factory;
        cache = template.cache;
    }

    private MagnumDI(ProviderFactory factory, KeyMap keyMap, Module module, Cache cache) {
        this.factory = factory;
        this.keyMap = keyMap;
        this.module = module;
        this.cache = cache;
    }

    public MagnumDI addArray(Object[] arrayInstanceComponent) {
        return add(new Object[]{arrayInstanceComponent});
    }

    public MagnumDI add(Object... components_classOrInstanceOrProvider) {
        if (components_classOrInstanceOrProvider.length > 1
                && components_classOrInstanceOrProvider[0].getClass() == components_classOrInstanceOrProvider[1].getClass()
                && !(components_classOrInstanceOrProvider[0] instanceof Class)
                && !(components_classOrInstanceOrProvider[0] instanceof Provider)) {
            throw new IllegalArgumentException("It looks like you're trying to add an array as an instance component, but Java varargs outsmarted you!  You probably want to call addArray() instead--if not, you shouldn't be trying to add two objects of the same type to the container: " + Arrays.toString(components_classOrInstanceOrProvider));
        }
        KeyMap keyMap = this.keyMap;
        Module module = this.module.nextGeneration();
        final Cache cache = new Cache(this.cache);

        for (Object component : components_classOrInstanceOrProvider) {
            final Provider<?> provider = factory.getProvider(component);
            Object key = provider.getProvidedClass();
            keyMap = keyMap.add(key);
            module = module.add(provider);
        }

        return new MagnumDI(factory, keyMap, module, cache);
    }

    public <T> T get(Class<T> componentKey) {
        if (keyMap.getBestMatch(componentKey) == null) {
            try {
                final Provider<?> provider = factory.getProvider(componentKey);
                final Class<?>[] dependencyTypes = provider.getDependencyTypes();
                if (dependencyTypes == null)
                    throw new RuntimeException("Provider must implements getDependencyTypes(): " + provider);
                final GenerationTag<Object[]> taggedDependencies = getDependencies(dependencyTypes);
                final Object[] dependencies = taggedDependencies.object;
                return (T) provider.get(dependencies);
            } catch (RuntimeException e) {
                String prefix = "";
                Throwable cause = e.getCause();
                if (e.getClass() != RuntimeException.class) {
                    prefix = e.getClass().getCanonicalName() + ": ";
                    cause = e;
                }
                throw new RuntimeException(prefix + e.getMessage() + "\n        While getting dependency " + componentKey, cause);
            }
        } else {
            return getWithExceptionReporting(componentKey).object;
        }
    }

    private <T> GenerationTag<T> getWithExceptionReporting(Class<T> componentKey) {
        try {
            return getWithoutAdding(componentKey);
        } catch (RuntimeException e) {
            String prefix = "";
            Throwable cause = e.getCause();
            if (e.getClass() != RuntimeException.class) {
                prefix = e.getClass().getCanonicalName() + ": ";
                cause = e;
            }
            throw new RuntimeException(prefix + e.getMessage() + "\n        While getting dependency " + componentKey, cause);
        }
    }

    public <T> GenerationTag<T> getWithoutAdding(Class<T> componentKey) {
        if (componentKey == MagnumDI.class) return new GenerationTag(-1, this);
        Object key = getBestKey(componentKey);
        return getFromCacheOrCreateFromProvider(key);
    }

    private <T> GenerationTag<T> getFromCacheOrCreateFromProvider(Object key) {
        final GenerationTag<?> cacheResult = cache.get(key);
        if (cacheResult != null) return (GenerationTag<T>) cacheResult;

        final GenerationTag<T> taggedInstance = createFromProvider(key);
        cache.add(key, taggedInstance.object, taggedInstance.generation);
        return taggedInstance;
    }

    private <T> GenerationTag<T> createFromProvider(Object key) {
        final GenerationTag<Provider<?>> taggedProvider = module.getProvider(key);
        final Provider<T> provider = (Provider<T>) taggedProvider.object;
        final Class<?>[] dependencyTypes = provider.getDependencyTypes();
        if (dependencyTypes == null)
            throw new RuntimeException("Provider must implements getDependencyTypes(): " + provider);
        final GenerationTag<Object[]> taggedDependencies = getDependencies(dependencyTypes);
        final Object[] dependencies = taggedDependencies.object;
        final T instance = provider.get(dependencies);

        int youngestGeneration = Math.max(taggedDependencies.generation, taggedProvider.generation);
        return new GenerationTag<>(youngestGeneration, instance);
    }

    private <T> Object getBestKey(Class<T> componentKey) {
        Object key = keyMap.getBestMatch(componentKey);
        if (key == null) throw new RuntimeException("No provider for key: " + componentKey);
        return key;
    }

    private GenerationTag<Object[]> getDependencies(Class<?>[] dependencyTypes) {
        int youngestGeneration = 0;
        final Object[] dependencies = new Object[dependencyTypes.length];
        for (int i = 0; i < dependencyTypes.length; i++) {
            Class<?> dependencyType = dependencyTypes[i];
            final GenerationTag<?> taggedDependency = getWithExceptionReporting(dependencyType);
            youngestGeneration = Math.max(youngestGeneration, taggedDependency.generation);
            dependencies[i] = taggedDependency.object;
        }
        return new GenerationTag<>(youngestGeneration, dependencies);
    }
}
