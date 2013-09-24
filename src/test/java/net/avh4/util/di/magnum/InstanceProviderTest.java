package net.avh4.util.di.magnum;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstanceProviderTest {

    private InstanceProvider<String> subject;

    @Before
    public void setUp() throws Exception {
        subject = new InstanceProvider<>("Test");
    }

    @Test
    public void shouldProvideInstance() throws Exception {
        assertThat(subject.get()).isEqualTo("Test");
    }

    @Test
    public void shouldNotRequireParameters() throws Exception {
        assertThat(subject.getDependencyTypes()).isEmpty();
    }

    @Test
    public void shouldHaveProvidedClass() throws Exception {
        assertThat(subject.getProvidedClass()).isEqualTo(String.class);
    }
}
