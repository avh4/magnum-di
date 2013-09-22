package net.avh4.util.di.magnum;

public class IncrementalContainer implements Container {
    private MagnumDI delegate;

    public void setDelegate(MagnumDI delegate) {
        if (this.delegate != null) throw new IllegalStateException("Container is being initialized twice");
        this.delegate = delegate;
    }

    @Override public MagnumDI add(Object... components_classOrInstanceOrProvider) {
        if (delegate == null)
            throw new RuntimeException("Container has not been initialized (you probably need to wait for the constructor to complete)");
        return delegate.add(components_classOrInstanceOrProvider);
    }

    @Override public <T> T get(Class<T> componentClass) {
        if (delegate == null)
            throw new RuntimeException("Container has not been initialized (you probably need to wait for the constructor to complete)");
        return delegate.get(componentClass);
    }

    @Override public MagnumDI create(Class<?>... componentKeys) {
        if (delegate == null)
            throw new RuntimeException("Container has not been initialized (you probably need to wait for the constructor to complete)");
        return delegate.create(componentKeys);
    }
}
