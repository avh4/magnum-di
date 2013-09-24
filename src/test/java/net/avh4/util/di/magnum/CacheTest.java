package net.avh4.util.di.magnum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheTest {

    private final String i0 = "P0";
    private final String i1 = "P1";
    private final String i2 = "P2";
    private final String i3 = "P3";
    private Cache subject0;
    private Cache subject1;
    private Cache subject2;
    @Mock private Object k0;
    @Mock private Object k1;
    @Mock private Object k2;
    @Mock private Object k3;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject0 = new Cache();
        subject1 = new Cache(subject0);
        subject2 = new Cache(subject1);
        subject0.add(k0, i0, 0);
        subject1.add(k1, i1, 1);
        subject2.add(k2, i2, 2);
    }

    @Test
    public void shouldStoreValues() throws Exception {
        assertThat(subject0.get(k0).object).isEqualTo(i0);
    }

    @Test
    public void childrenShouldFallBackOnParents() throws Exception {
        assertThat(subject1.get(k0).object).isEqualTo(i0);
        assertThat(subject2.get(k0).object).isEqualTo(i0);
    }

    @Test
    public void get_shouldGiveGeneration() throws Exception {
        assertThat(subject0.get(k0).generation).isEqualTo(0);
        assertThat(subject1.get(k0).generation).isEqualTo(0);
        assertThat(subject2.get(k0).generation).isEqualTo(0);

        assertThat(subject1.get(k1).generation).isEqualTo(1);
        assertThat(subject2.get(k1).generation).isEqualTo(1);

        assertThat(subject2.get(k2).generation).isEqualTo(2);
    }

    @Test
    public void children_shouldStoreValues() throws Exception {
        assertThat(subject1.get(k1).object).isEqualTo(i1);
        assertThat(subject2.get(k2).object).isEqualTo(i2);
    }

    @Test
    public void parent_shouldNotFallBackOnChildren() throws Exception {
        assertThat(subject0.get(k1)).isNull();
        assertThat(subject0.get(k2)).isNull();
        assertThat(subject1.get(k2)).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void store_toNegativeGeneration_shouldThrow() throws Exception {
        subject2.add(k3, i3, -1);
    }

    @Test(expected = RuntimeException.class)
    public void store_toFutureGeneration_shouldThrow1() throws Exception {
        subject2.add(k3, i3, 3);
    }

    @Test(expected = RuntimeException.class)
    public void store_toFutureGeneration_shouldThrow2() throws Exception {
        subject1.add(k3, i3, 2);
    }

    @Test(expected = RuntimeException.class)
    public void store_toFutureGeneration_shouldThrow3() throws Exception {
        subject0.add(k3, i3, 1);
    }

    @Test
    public void addingToChild_shouldModifyParent() throws Exception {
        subject2.add(k3, i3, 0);
        assertThat(subject0.get(k3).object).isEqualTo(i3);
        assertThat(subject0.get(k3).generation).isEqualTo(0);
    }
}
