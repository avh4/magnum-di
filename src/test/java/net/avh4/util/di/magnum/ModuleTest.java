package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.Dragnet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class ModuleTest {
    private Module subject;
    @Mock private Provider<DickVanDyke> p1;
    @Mock private Provider<Dragnet> p2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(p1.getProvidedClass()).toReturn(DickVanDyke.class);
        stub(p2.getProvidedClass()).toReturn(Dragnet.class);
        subject = new Module();
    }

    @Test
    public void add_shouldAddProvider() throws Exception {
        subject = subject.add(p1);
        assertThat(subject.getProvider(DickVanDyke.class).object).isSameAs(p1);
    }

    @Test
    public void add_shouldRememberGeneration() throws Exception {
        subject = subject.add(p1);
        assertThat(subject.getProvider(DickVanDyke.class).generation).isEqualTo(0);
    }

    @Test
    public void add_twice_shouldKeepSameGeneration() throws Exception {
        subject = subject.add(p1).add(p2);
        assertThat(subject.getProvider(DickVanDyke.class).generation).isEqualTo(0);
        assertThat(subject.getProvider(Dragnet.class).generation).isEqualTo(0);
    }

    @Test
    public void nextGeneration_shouldIncrementGeneration() throws Exception {
        subject = subject.add(p1).nextGeneration().add(p2);
        assertThat(subject.getProvider(DickVanDyke.class).generation).isEqualTo(0);
        assertThat(subject.getProvider(Dragnet.class).generation).isEqualTo(1);
    }
}
