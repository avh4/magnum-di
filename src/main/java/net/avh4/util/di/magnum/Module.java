package net.avh4.util.di.magnum;

public interface Module {
    public <T> Provider<? extends T> getProvider(Class<T> componentKey);

    public <T> Module add(Class<T> componentKey, Provider<? extends T> provider);

    public <T> Module add(Class<T> componentKey);
}
