package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.MerchandisingRights;
import net.avh4.util.di.magnum.test.Series;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class ConstructorProviderTest {

    @Mock private Container container;
    @Mock private DickVanDyke dvd;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(container.get(DickVanDyke.class)).toReturn(dvd);
        stub(container.get(Series.class)).toReturn(dvd);
    }

    @Test
    public void shouldInstantiateClassWithDefaultConstructor() throws Exception {
        ConstructorProvider<?> subject = new ConstructorProvider<>(DickVanDyke.class);
        assertThat(subject.get(container)).isInstanceOf(DickVanDyke.class);
    }

    @Test
    public void shouldGetDependenciesFromContainer() throws Exception {
        ConstructorProvider<MerchandisingRights> subject = new ConstructorProvider<>(MerchandisingRights.class);
        assertThat(subject.get(container).series).isSameAs(dvd);
    }
}
