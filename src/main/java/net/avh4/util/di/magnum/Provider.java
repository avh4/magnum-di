package net.avh4.util.di.magnum;

public interface Provider<T> {
    public Class<T> getProvidedClass();

    public Class<?>[] getDependencyTypes();

    public T get(Object... dependencies);
}
