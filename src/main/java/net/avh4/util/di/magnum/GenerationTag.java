package net.avh4.util.di.magnum;

public class GenerationTag<T> {
    public final int generation;
    public final T object;

    public GenerationTag(int generation, T object) {
        this.generation = generation;
        this.object = object;
    }

    @Override public String toString() {
        return "GenerationTag{" +
                "generation=" + generation +
                ", object=" + object +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenerationTag that = (GenerationTag) o;

        if (generation != that.generation) return false;
        if (object != null ? !object.equals(that.object) : that.object != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = generation;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}
