package net.avh4.util.di.magnum;

import net.avh4.util.di.magnum.test.BlackAndWhite;
import net.avh4.util.di.magnum.test.DickVanDyke;
import net.avh4.util.di.magnum.test.SitcomBase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.stub;

public class MagnumDITest {

    private MagnumDI subject;
    @Mock private Provider<DickVanDyke> provider1;
    @Mock private DickVanDyke dvd;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new MagnumDI();
        stub(provider1.get(subject)).toReturn(dvd);
    }

    @Test
    public void shouldUseProviderForClass() throws Exception {
        subject.add(DickVanDyke.class, provider1);
        assertThat(subject.get(DickVanDyke.class)).isSameAs(dvd);
    }

    @Test
    public void shouldUseProviderForSuperClass() throws Exception {
        subject.add(DickVanDyke.class, provider1);
        assertThat(subject.get(SitcomBase.class)).isSameAs(dvd);
    }

    @Test
    public void shouldUseProviderForInterface() throws Exception {
        subject.add(DickVanDyke.class, provider1);
        assertThat(subject.get(BlackAndWhite.class)).isSameAs(dvd);
    }
}
