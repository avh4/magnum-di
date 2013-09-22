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

public class StandardModuleTest {
    private Module subject;
    @Mock private Provider<DickVanDyke> provider1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(provider1.getProvidedClass()).toReturn(DickVanDyke.class);
        subject = new StandardModule();
    }

    @Test
    public void shouldUseProviderForClass() throws Exception {
        subject = subject.add(provider1);
        assertThat(subject.getProvider(DickVanDyke.class)).isSameAs(provider1);
    }

    @Test
    public void shouldUseProviderForSuperClass() throws Exception {
        subject = subject.add(provider1);
        assertThat(subject.getProvider(SitcomBase.class)).isSameAs(provider1);
    }

    @Test
    public void shouldUseProviderForInterface() throws Exception {
        subject = subject.add(provider1);
        assertThat(subject.getProvider(BlackAndWhite.class)).isSameAs(provider1);
    }
}
