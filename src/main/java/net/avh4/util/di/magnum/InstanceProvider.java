package net.avh4.util.di.magnum;

public class InstanceProvider<T> implements Provider<T> {
    private final T instance;

    public InstanceProvider(T instance) {
        this.instance = instance;
    }

    @Override public Class<T> getProvidedClass() {
        //noinspection unchecked
        return (Class<T>) instance.getClass();
    }

    @Override public Class<?>[] getDependencyKeys() {
        return new Class<?>[0];
    }

    @Override public T get(Object[] dependencies) {
        return instance;
    }

    @Override public String toString() {
        return "InstanceProvider{" +
                "instance=" + instance +
                '}';
    }

    @SuppressWarnings("RedundantIfStatement") @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceProvider that = (InstanceProvider) o;

        if (instance != null ? !(instance == that.instance) : that.instance != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return instance != null ? System.identityHashCode(instance) : 0;
    }
}
