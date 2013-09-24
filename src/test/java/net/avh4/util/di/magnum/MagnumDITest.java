package net.avh4.util.di.magnum;

import net.avh4.test.junit.Nested;
import net.avh4.util.di.magnum.test.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.stub;

@RunWith(Nested.class)
public class MagnumDITest {

    private MagnumDI subject0;
    private MagnumDI subject1;
    @Mock private Provider<DickVanDyke> dvdProvider;
    @Mock private DickVanDyke dvd1;
    @Mock private DickVanDyke dvd2;
    @Mock private Provider<MerchandisingRights> mrProvider;
    @Mock private MerchandisingRights mr1_dvd1;
    @Mock private MerchandisingRights mr1_dvd2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stub(dvdProvider.getProvidedClass()).toReturn(DickVanDyke.class);
        stub(dvdProvider.getDependencyTypes()).toReturn(new Class[]{});
        stub(dvdProvider.get()).toReturn(dvd1).toReturn(dvd2).toThrow(new RuntimeException("Ran out of fake dvds"));
        stub(mrProvider.getProvidedClass()).toReturn(MerchandisingRights.class);
        stub(mrProvider.getDependencyTypes()).toReturn(new Class[]{Series.class});
        stub(mrProvider.get(dvd1)).toReturn(mr1_dvd1).toThrow(new RuntimeException("Ran out of fake mrs (for dvd1)"));
        stub(mrProvider.get(dvd2)).toReturn(mr1_dvd2).toThrow(new RuntimeException("Ran out of fake mrs (for dvd2)"));
        subject0 = new MagnumDI(dvdProvider);
        subject1 = subject0.add();
    }

    @Test
    public void shouldUseProviderForClass() throws Exception {
        assertThat(subject0.get(DickVanDyke.class)).isSameAs(dvd1);
    }

    @Test
    public void shouldCacheInstance() throws Exception {
        assertThat(subject0.get(DickVanDyke.class)).isSameAs(subject0.get(DickVanDyke.class));
    }

    @Test
    public void shouldUseProviderForSuperClass() throws Exception {
        assertThat(subject0.get(SitcomBase.class)).isSameAs(dvd1);
    }

    @Test
    public void shouldUseProviderForInterface() throws Exception {
        assertThat(subject0.get(BlackAndWhite.class)).isSameAs(dvd1);
    }

    @Test
    public void shouldLookUpDependencies() throws Exception {
        MagnumDI subject = new MagnumDI(mrProvider, dvdProvider);
        assertThat(subject.get(MerchandisingRights.class)).isSameAs(mr1_dvd1);
    }

    @SuppressWarnings("UnusedDeclaration")
    public class WhenParentHasTheComponent {
        @Before
        public void setUp() throws Exception {
            subject0 = new MagnumDI(dvdProvider);
            subject1 = subject0.add();
        }

        @Test
        public void child_shouldFallBackOnParent() throws Exception {
            assertThat(subject1.get(DickVanDyke.class)).isSameAs(dvd1);
        }

        @Test
        public void get_throughChild_shouldCacheInParent() throws Exception {
            assertThat(subject1.get(DickVanDyke.class)).isSameAs(subject0.get(DickVanDyke.class));
        }

        @Test
        public void get_throughParent_shouldCacheForChild() throws Exception {
            assertThat(subject0.get(DickVanDyke.class)).isSameAs(subject1.get(DickVanDyke.class));
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public class WhenChildHasTheComponent {
        private MagnumDI subject1a;
        private MagnumDI subject1b;

        @Before
        public void setUp() throws Exception {
            subject0 = new MagnumDI();
            subject1a = subject0.add(dvdProvider);
            subject1b = subject0.add(dvdProvider);
        }

        @Test
        public void get_inChild_ShouldNotCacheForOtherChildren() throws Exception {
            assertThat(subject1a.get(DickVanDyke.class)).isSameAs(dvd1);
            assertThat(subject1b.get(DickVanDyke.class)).isSameAs(dvd2);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public class WhenChildHasDependencyOfComponentInParent {
        private MagnumDI subject1a;
        private MagnumDI subject1b;

        @Before
        public void setUp() throws Exception {
            subject0 = new MagnumDI(mrProvider);
            subject1a = subject0.add(dvdProvider);
            subject1b = subject0.add(dvdProvider);
        }

        @Test(expected = RuntimeException.class)
        public void get_inParent_shouldThrow() throws Exception {
            subject0.get(MerchandisingRights.class);
        }

        @Test
        public void get_inChild_shouldNotCacheForParent() throws Exception {
            subject1a.get(MerchandisingRights.class);
            try {
                subject0.get(MerchandisingRights.class);
                fail("Expected RuntimeException");
            } catch (RuntimeException e) {
                // pass
            }
        }

        @Test
        public void get_inChild_ShouldNotCacheForOtherChildren() throws Exception {
            assertThat(subject1a.get(MerchandisingRights.class)).isSameAs(mr1_dvd1);
            assertThat(subject1b.get(MerchandisingRights.class)).isSameAs(mr1_dvd2);
        }
    }
}
