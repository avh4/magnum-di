package net.avh4.util.di.magnum;

import net.avh4.test.junit.Nested;
import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.Dragnet;
import net.avh4.util.di.magnum.test.Series;
import net.avh4.util.di.magnum.test.SitcomBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Nested.class)
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

    @SuppressWarnings("UnusedDeclaration")
    public class BoxedPrimitives {
        @Test
        public void get_int_shouldMatchInteger() throws Exception {
            subject = subject.add(Integer.class);
            assertThat(subject.getBestMatch(Integer.TYPE)).isSameAs(Integer.class);
        }

        @Test
        public void get_byte_shouldMatchByte() throws Exception {
            subject = subject.add(Byte.class);
            assertThat(subject.getBestMatch(Byte.TYPE)).isSameAs(Byte.class);
        }

        @Test
        public void get_char_shouldMatchCharacter() throws Exception {
            subject = subject.add(Character.class);
            assertThat(subject.getBestMatch(Character.TYPE)).isSameAs(Character.class);
        }

        @Test
        public void get_short_shouldMatchShort() throws Exception {
            subject = subject.add(Short.class);
            assertThat(subject.getBestMatch(Short.TYPE)).isSameAs(Short.class);
        }

        @Test
        public void get_long_shouldMatchLong() throws Exception {
            subject = subject.add(Long.class);
            assertThat(subject.getBestMatch(Long.TYPE)).isSameAs(Long.class);
        }

        @Test
        public void get_float_shouldMatchFloat() throws Exception {
            subject = subject.add(Float.class);
            assertThat(subject.getBestMatch(Float.TYPE)).isSameAs(Float.class);
        }

        @Test
        public void get_double_shouldMatchDouble() throws Exception {
            subject = subject.add(Double.class);
            assertThat(subject.getBestMatch(Double.TYPE)).isSameAs(Double.class);
        }

        @Test
        public void get_bool_shouldMatchBoolean() throws Exception {
            subject = subject.add(Boolean.class);
            assertThat(subject.getBestMatch(Boolean.TYPE)).isSameAs(Boolean.class);
        }
    }
}
