package net.avh4.util.di.magnum.integration;

import net.avh4.util.di.magnum.MagnumDI;
import net.avh4.util.di.magnum.test.*;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class IntegrationTest {

    private MagnumDI magnum;

    @Before
    public void setUp() throws Exception {
        magnum = new MagnumDI(DickVanDyke.class, MerchandisingRights.class);
    }

    @Test
    public void testTopLevelDependencies() throws Exception {
        assertThat(magnum.get(DickVanDyke.class)).isInstanceOf(DickVanDyke.class);
    }

    @Test(expected = RuntimeException.class)
    public void whenComponentDoesntExist_shouldThrow() throws Exception {
        magnum = new MagnumDI();
        magnum.get(Series.class);
    }

    @Test
    public void shouldProvideInterfaces() throws Exception {
        assertThat(magnum.get(Series.class)).isInstanceOf(DickVanDyke.class);
    }

    @Test
    public void withAmbiguousInterface_shouldUseLastAdded() throws Exception {
        magnum = magnum.add(Dragnet.class);
        assertThat(magnum.get(Series.class)).isInstanceOf(Dragnet.class);
    }

    @Test
    public void testDependenciesWithDependencies() throws Exception {
        assertThat(magnum.get(MerchandisingRights.class).series).isInstanceOf(DickVanDyke.class);
    }

    @Test
    public void testDependencyFromProviderWithAddedScope() throws Exception {
        magnum = new MagnumDI(MerchandisingRights.class, AdCampaign.class);
        final AdCampaign adCampaign = magnum.add(DickVanDyke.class).get(AdCampaign.class);
        assertThat(adCampaign.merchandisingRights.series).isSameAs(adCampaign.series);
    }
}
