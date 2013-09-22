package net.avh4.util.di.magnum;

import java.util.HashMap;
import java.util.Map;

public class MagnumDI {
    private final Module module;

    public MagnumDI(Class<?>... components) {
        Module module = new StandardModule();
        for (Class<?> component : components) {
            module = module.add(component);
        }
        this.module = module;
    }

    protected MagnumDI(Module module) {
        this.module = module;
    }

    public MagnumDI add(Class<?>... componentClass) {
        Module module = this.module;
        for (Class<?> component : componentClass) {
            module = module.add(component);
        }
        return new MagnumDI(module);
    }

    public <T> MagnumDI add(Class<T> componentClass, Provider<? extends T> provider) {
        return new MagnumDI(module.add(componentClass, provider));
    }

    public <T> T get(Class<T> componentClass, Class<?>... scopedDependencies) {
        MagnumDI scoped = add(scopedDependencies);
        if (scoped.module.getProvider(componentClass) == null) {
            scoped = scoped.add(componentClass);
        }
        return scoped._get(componentClass);
    }

    protected <T> T _get(Class<T> componentClass) {
        try {
            return _get(componentClass, new HashMap<Class<?>, Object>());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage() + "\n        While finding dependency " + componentClass, e.getCause());
        }
    }

    private <T> T _get(Class<T> componentClass, final Map<Class<?>, Object> cache) {
        //noinspection unchecked
        final Provider<T> provider = (Provider<T>) module.getProvider(componentClass);
        if (provider == null)
            throw new RuntimeException("No provider for component: " + componentClass);

        return provider.get(new Container() {
            @Override public <T> T get(Class<T> componentClass) {
                if (cache.containsKey(componentClass)) return (T) cache.get(componentClass);
                T instance = _get(componentClass, cache);
                cache.put(componentClass, instance);
                return instance;
            }
        });
    }
}
