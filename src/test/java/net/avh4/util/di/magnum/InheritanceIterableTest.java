package net.avh4.util.di.magnum;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class InheritanceIterableTest {
    @Test
    public void shouldIncludeClass() throws Exception {
        assertThat(new InheritanceIterable<>(Object.class)).contains(Object.class);
    }

    @Test
    public void shouldIncludeSuperclasses() throws Exception {
        assertThat(new InheritanceIterable<>(String.class)).contains(Object.class);
    }

    @Test
    public void shouldIncludeInterfaces() throws Exception {
        assertThat(new InheritanceIterable<>(Date.class)).contains(Serializable.class);
    }

    @Test
    public void shouldIncludeInterfacesOfSuperclasses() throws Exception {
        assertThat(new InheritanceIterable<>(ArrayList.class)).contains(Collection.class);
    }

    @Test
    public void shouldIncludeSuperInterfaces() throws Exception {
        assertThat(new InheritanceIterable<>(ArrayList.class)).contains(Iterable.class);
    }
}
