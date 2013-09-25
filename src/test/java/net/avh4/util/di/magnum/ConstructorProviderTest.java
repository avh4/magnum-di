package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.MerchandisingRights;
import net.avh4.util.di.magnum.test.Series;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.stub;

public class ConstructorProviderTest {

    @Mock private DickVanDyke dvd;
    private ConstructorProvider<?> dvdSubject;
    private ConstructorProvider<MerchandisingRights> mrSubject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dvdSubject = ConstructorProvider.forClass(DickVanDyke.class);
        mrSubject = ConstructorProvider.forClass(MerchandisingRights.class);
    }

    @Test
    public void shouldInstantiateClassWithDefaultConstructor() throws Exception {
        assertThat(dvdSubject.get()).isInstanceOf(DickVanDyke.class);
    }

    @Test
    public void shouldUseDependencies() throws Exception {
        assertThat(mrSubject.get(dvd).series).isSameAs(dvd);
    }

    @Test
    public void shouldNeedConstructorParameters() throws Exception {
        assertThat(dvdSubject.getDependencyTypes()).isEmpty();
        assertThat(mrSubject.getDependencyTypes()).containsExactly(Series.class);
    }

    @Test(expected = RuntimeException.class)
    public void forInterface_shouldThrow() throws Exception {
        ConstructorProvider.forClass(Serializable.class);
    }

    @Test
    public void forClassWithNoAccessibleConstructors_shouldThrow() throws Exception {
        try {
            ConstructorProvider.forClass(NoAccessibleConstructors.class);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertThat(e.getMessage())
                    .contains(NoAccessibleConstructors.class.getName())
                    .containsIgnoringCase("no accessible constructors");
        }
    }

    public static class NoAccessibleConstructors {
        private NoAccessibleConstructors() {
        }
    }
}
