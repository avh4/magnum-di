package net.avh4.util.di.magnum;

public class MissingComponentExplanationProvider<T> implements Provider<T> {
    private final Class<T> aClass;
    private final String message;

    public MissingComponentExplanationProvider(Class<T> aClass, String message) {
        this.aClass = aClass;
        this.message = message;
    }


    @Override public Class<T> getProvidedClass() {
        return aClass;
    }

    @Override public Class<?>[] getDependencyKeys() {
        return new Class<?>[0];
    }

    @Override public T get(Object... dependencies) {
        throw new RuntimeException(message);
    }
}
