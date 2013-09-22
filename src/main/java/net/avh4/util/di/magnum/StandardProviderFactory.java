package net.avh4.util.di.magnum;

public class StandardProviderFactory implements ProviderFactory {
    @Override public Provider<?> getProvider(Object anything) {
        if (anything instanceof Class) {
            return ConstructorProvider.forClass((Class) anything);
        } else if (anything instanceof Provider) {
            return (Provider<?>) anything;
        } else {
            return new InstanceProvider<>(anything);
        }
    }
}
