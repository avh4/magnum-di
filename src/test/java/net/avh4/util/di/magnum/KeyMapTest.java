package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.Dragnet;
import net.avh4.util.di.magnum.test.Series;
import net.avh4.util.di.magnum.test.SitcomBase;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyMapTest {
    private KeyMap subject;

    @Before
    public void setUp() throws Exception {
        subject = new KeyMap();
    }

    @Test
    public void addObject_shouldAdd() throws Exception {
        subject = subject.add("Key");
        assertThat(subject.getBestMatch("Key")).isEqualTo("Key");
    }

    @Test
    public void get_unknown_shouldReturnNull() throws Exception {
        assertThat(subject.getBestMatch("Key")).isNull();
    }

    @Test
    public void addClass_shouldAdd() throws Exception {
        subject = subject.add(DickVanDyke.class);
        assertThat(subject.getBestMatch(DickVanDyke.class)).isEqualTo(DickVanDyke.class);
    }

    @Test
    public void addClass_shouldAddSuperclass() throws Exception {
        subject = subject.add(DickVanDyke.class);
        assertThat(subject.getBestMatch(SitcomBase.class)).isEqualTo(DickVanDyke.class);
    }

    @Test
    public void addClass_shouldAddInterface() throws Exception {
        subject = subject.add(DickVanDyke.class);
        assertThat(subject.getBestMatch(Series.class)).isEqualTo(DickVanDyke.class);
    }

    @Test
    public void get_withMultipleMatches_shouldBeAmbiguous() throws Exception {
        subject = subject.add(DickVanDyke.class).add(Dragnet.class);
        assertThat(subject.getBestMatch(Series.class)).isInstanceOf(KeyMap.AmbiguousKey.class);
    }
}
