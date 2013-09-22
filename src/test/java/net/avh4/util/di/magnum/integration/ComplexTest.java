package net.avh4.util.di.magnum.integration;

import net.avh4.util.di.magnum.Container;
import net.avh4.util.di.magnum.MagnumDI;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * In these test cases, we have the following components:
 * <p/>
 * A:  has dependencies B, C, D
 * B:  has dependency E, and a container that can create F and G
 * C:  has no dependencies
 * D:  has no dependencies
 * E:  has dependency C
 * F:  has dependencies C, D, G
 * G:  has no dependencies
 * <p/>
 * The scenario is as follows:
 * 1. The caller creates a C without assistance.
 * 2. The caller asks the container to instantiate an object graph starting at A
 * 3. The caller asks for the reference to the newly-created A and D
 * 4. An event is triggered, causing B to create F and G
 * 4a. B asks its container to instantiate an object graph starting at F
 * 4b. B asks its for the reference to the newly-created F and G
 * <p/>
 * ∀ a ∈ A:  a.c ≣ a.b.e.c  (E will get the same C that its associated A has)
 * ∀ a ∈ A, ∀ f ∈ a.b.fs:   f.c ≣ a.c  (All Fs created by B will use the C associated with that B)
 * ∀ a ∈ A, ∀ f ∈ a.b.fs:   f.d ≣ a.d  (All Fs created by B will use the D associated with that B)
 */
public class ComplexTest {
    private A a;
    private C c;
    private D d;

    @Before
    public void setUp() throws Exception {
        c = new C() {
        };
        MagnumDI magnum = new MagnumDI(A.class, B.class, c, D.class, E.class, F.class, G.class);
        MagnumDI cache = magnum.create(A.class, D.class);
        a = cache.get(A.class);
        d = cache.get(D.class);

        a.b.makeF();
        a.b.makeF();
    }

    @Test
    public void shouldGetAsD() throws Exception {
        assertThat(d).isSameAs(a.d);
    }

    @Test
    public void asDependenciesShouldUseProvidedC() throws Exception {
        assertThat(a.c).isSameAs(a.b.e.c).isSameAs(c);
    }

    @Test
    public void separateGraphsShouldBeUnique() throws Exception {
        assertThat(a.b.fs.get(0)).isNotSameAs(a.b.fs.get(1));
        assertThat(a.b.gs.get(0)).isNotSameAs(a.b.gs.get(1));
    }

    @Test
    public void shouldGetFsG() throws Exception {
        assertThat(a.b.gs.get(0)).isSameAs(a.b.fs.get(0).g);
        assertThat(a.b.gs.get(1)).isSameAs(a.b.fs.get(1).g);
    }

    @Test
    public void fsShouldGetCommonC() throws Exception {
        assertThat(a.b.fs.get(0).c).isSameAs(a.c).isSameAs(c);
        assertThat(a.b.fs.get(1).c).isSameAs(a.c).isSameAs(c);
    }

    @Test
    public void fsShouldGetCommonD() throws Exception {
        assertThat(a.b.fs.get(0).d).isSameAs(a.d).isSameAs(d);
        assertThat(a.b.fs.get(1).d).isSameAs(a.d).isSameAs(d);
    }

    public static class A {
        final B b;
        final C c;
        final D d;

        public A(B b, C c, D d) {
            this.b = b; this.c = c; this.d = d;
        }
    }

    public static class B {
        final E e;
        final Container container;
        final ArrayList<F> fs = new ArrayList<>();
        final ArrayList<G> gs = new ArrayList<>();

        public B(E e, Container container) {
            this.e = e;
            this.container = container;
        }

        public void makeF() {
            MagnumDI cache = container.create(F.class, G.class);
            fs.add(cache.get(F.class));
            gs.add(cache.get(G.class));
        }
    }

    public interface C {
    }

    public static class D {
    }

    public static class E {
        final C c;

        public E(C c) {
            this.c = c;
        }
    }

    public static class F {
        final C c;
        final D d;
        final G g;

        public F(C c, D d, G g) {
            this.c = c;
            this.d = d;
            this.g = g;
        }
    }

    public static class G {
    }
}
