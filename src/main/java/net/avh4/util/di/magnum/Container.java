package net.avh4.util.di.magnum;

public interface Container {
    <T> T get(Class<T> componentClass, Class<?>... scopedDependencies);
}
