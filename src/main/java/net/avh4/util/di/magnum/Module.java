package net.avh4.util.di.magnum;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class Module {
    private final PMap<Class<?>, GenerationTag<Provider<?>>> map;
    private final int generation;

    public Module() {
        map = HashTreePMap.empty();
        generation = 0;
    }

    private Module(PMap<Class<?>, GenerationTag<Provider<?>>> map, int generation) {
        this.map = map;
        this.generation = generation;
    }

    public GenerationTag<Provider<?>> getProvider(Object componentKey) {
        //noinspection unchecked
        return map.get(componentKey);
    }

    public <T> Module add(Provider<T> provider) {
        if (provider == null) return this;
        final Class<T> providedClass = provider.getProvidedClass();
        if (providedClass == null)
            throw new RuntimeException("Provider does not provide a key--you must implement getProvidedClass(): " + provider);
        return add(providedClass, provider);
    }

    private <T> Module add(Class<T> componentKey, Provider<? extends T> provider) {
        PMap<Class<?>, GenerationTag<Provider<?>>> newProviders = map.plus(componentKey, new GenerationTag(generation, provider));
        return new Module(newProviders, generation);
    }

    public Module nextGeneration() {
        return new Module(map, generation + 1);
    }
}
