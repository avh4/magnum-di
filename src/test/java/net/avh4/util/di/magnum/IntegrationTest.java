package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.Dragnet;
import net.avh4.util.di.magnum.test.Series;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class IntegrationTest {

    private MagnumDI magnum;

    @Before
    public void setUp() throws Exception {
        magnum = new MagnumDI();
        magnum.add(DickVanDyke.class);
    }

    @Test
    public void testTopLevelDependencies() throws Exception {
        assertThat(magnum.get(DickVanDyke.class)).isInstanceOf(DickVanDyke.class);
    }

    @Test(expected = RuntimeException.class)
    public void whenComponentDoesntExist_shouldThrow() throws Exception {
        magnum = new MagnumDI();
        magnum.get(DickVanDyke.class);
    }

    @Test
    public void shouldCacheDepedencies() throws Exception {
        assertThat(magnum.get(DickVanDyke.class)).isSameAs(magnum.get(DickVanDyke.class));
    }


    @Test
    public void shouldProvideInterfaces() throws Exception {
        assertThat(magnum.get(Series.class)).isSameAs(magnum.get(DickVanDyke.class));
    }

    @Test
    public void withAmbiguousInterface_shouldThrowWithMessage() throws Exception {
        magnum.add(Dragnet.class);
        try {
            magnum.get(Series.class);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertThat(e.getMessage())
                    .contains(DickVanDyke.class.getCanonicalName())
                    .contains(Dragnet.class.getCanonicalName())
                    .contains(Series.class.getCanonicalName());
        }
    }
}
