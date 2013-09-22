package net.avh4.util.di.magnum;

public interface Container {
    MagnumDI add(Object... components_classOrInstanceOrProvider);

    <T> T get(Class<T> componentClass);

    MagnumDI create(Class<?>... componentKeys);
}
