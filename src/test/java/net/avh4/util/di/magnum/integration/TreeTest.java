package net.avh4.util.di.magnum.integration;

import net.avh4.util.di.magnum.MagnumDI;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeTest {

    private RootNode root;
    private MagnumDI magnum;

    @Before
    public void setUp() throws Exception {
        magnum = new MagnumDI(Service.class);
        int[][] data = new int[][]{{1, 2}, {3, 4}};
        root = magnum.addArray(data).get(RootNode.class);
    }

    @Test
    public void test() throws Exception {
        assertThat(root.children()).hasSize(2);
        assertThat(root.children()[0].children()).hasSize(2);
        assertThat(root.children()[1].children()).hasSize(2);

        assertThat(root).isInstanceOf(RootNode.class);
        assertThat(root.children()[0]).isInstanceOf(InternalNode.class);
        assertThat(root.children()[1]).isInstanceOf(InternalNode.class);
        assertThat(root.children()[0].children()[0]).isInstanceOf(LeafNode.class);
        assertThat(root.children()[0].children()[1]).isInstanceOf(LeafNode.class);
        assertThat(root.children()[1].children()[0]).isInstanceOf(LeafNode.class);
        assertThat(root.children()[1].children()[1]).isInstanceOf(LeafNode.class);

        assertThat(root.parent()).isNull();
        assertThat(root.children()[0].parent()).isSameAs(root);
        assertThat(root.children()[1].parent()).isSameAs(root);
        assertThat(root.children()[0].children()[0].parent()).isSameAs(root.children()[0]);
        assertThat(root.children()[0].children()[1].parent()).isSameAs(root.children()[0]);
        assertThat(root.children()[1].children()[0].parent()).isSameAs(root.children()[1]);
        assertThat(root.children()[1].children()[1].parent()).isSameAs(root.children()[1]);
    }

    @Test
    public void childrenShouldHaveService() throws Exception {
        Service service = magnum.get(Service.class);
        assertThat(((LeafNode) root.children()[0].children()[0]).service).isSameAs(service);
        assertThat(((LeafNode) root.children()[0].children()[1]).service).isSameAs(service);
        assertThat(((LeafNode) root.children()[1].children()[0]).service).isSameAs(service);
        assertThat(((LeafNode) root.children()[1].children()[1]).service).isSameAs(service);
    }

    @Test
    public void childrenShouldHaveCorrectValues() throws Exception {
        assertThat(((LeafNode) root.children()[0].children()[0]).value).isEqualTo(1);
        assertThat(((LeafNode) root.children()[0].children()[1]).value).isEqualTo(2);
        assertThat(((LeafNode) root.children()[1].children()[0]).value).isEqualTo(3);
        assertThat(((LeafNode) root.children()[1].children()[1]).value).isEqualTo(4);
    }

    public interface Node {
        Node parent();
        Node[] children();
    }

    public static class Service {}

    public static class RootNode implements Node {
        private final Node[] children;

        public RootNode(int[][] data, MagnumDI magnum) {
            children = new InternalNode[data.length];

            for (int i = 0; i < data.length; i++) {
                int[] ints = data[i];
                children[i] = magnum.add(this, ints).get(InternalNode.class);
            }
        }

        @Override public Node parent() {
            return null;
        }

        @Override public Node[] children() {
            return children;
        }
    }

    public static class InternalNode implements Node {
        private final Node parent;
        private final LeafNode[] children;

        public InternalNode(Node parent, int[] data, MagnumDI magnum) {
            this.parent = parent;
            children = new LeafNode[data.length];

            for (int i = 0; i < data.length; i++) {
                int value = data[i];
                children[i] = magnum.add(this, value).get(LeafNode.class);
            }
        }

        @Override public Node parent() {
            return parent;
        }

        @Override public Node[] children() {
            return children;
        }
    }

    public static class LeafNode implements Node {
        private final Node parent;
        final int value;
        final Service service;

        public LeafNode(Node parent, int value, Service service) {
            this.parent = parent;
            this.value = value;
            this.service = service;
        }

        @Override public Node parent() {
            return parent;
        }

        @Override public Node[] children() {
            return new Node[0];
        }
    }
}
