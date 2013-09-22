package net.avh4.util.di.magnum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class StandardProviderFactoryTest {

    private StandardProviderFactory subject;
    @Mock private Provider<Object> provider;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new StandardProviderFactory();
    }

    @Test
    public void withClass_shouldGiveConstructorProvider() throws Exception {
        assertThat(subject.getProvider(ArrayList.class)).isEqualTo(ConstructorProvider.forClass(ArrayList.class));
    }

    @Test
    public void withProvider_shouldGiveTheProvider() throws Exception {
        assertThat(subject.getProvider(provider)).isSameAs(provider);
    }

    @Test
    public void withOtherObject_shouldGiveInstanceProvider() throws Exception {
        assertThat(subject.getProvider("instancetaneous")).isEqualTo(new InstanceProvider<>("instancetaneous"));
    }
}
