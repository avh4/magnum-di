package net.avh4.util.di.magnum;

public interface Module {
    public <T> Provider<T> getProvider(Class<T> componentKey);

    public <T> Module add(Provider<T> provider);
}
