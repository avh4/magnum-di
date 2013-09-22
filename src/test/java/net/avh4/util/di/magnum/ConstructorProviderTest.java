package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.MerchandisingRights;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ConstructorProviderTest {

    @Mock private DickVanDyke dvd;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldInstantiateClassWithDefaultConstructor() throws Exception {
        ConstructorProvider<?> subject = ConstructorProvider.forClass(DickVanDyke.class);
        assertThat(subject.get()).isInstanceOf(DickVanDyke.class);
    }

    @Test
    public void shouldGetDependenciesFromContainer() throws Exception {
        ConstructorProvider<MerchandisingRights> subject = ConstructorProvider.forClass(MerchandisingRights.class);
        assertThat(subject.get(dvd).series).isSameAs(dvd);
    }

    @Test
    public void forInterface_shouldReturnNull() throws Exception {
        assertThat(ConstructorProvider.forClass(Serializable.class)).isNull();
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
