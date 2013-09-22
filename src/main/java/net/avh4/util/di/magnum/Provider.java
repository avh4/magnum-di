package net.avh4.util.di.magnum;

public interface Provider<T> {
    public T get(Container container);
}
