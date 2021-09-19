package easy.soc.hacks.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A double linked list implementation with fixed size.
 * This implementation allows push, pop, move to front elements
 * in case list is not full yet.
 *
 * @param <V> An element's type to save in {@link Node}.
 */
public class LinkedList<K, V> {
    /**
     * A node of {@link LinkedList}.
     * {@link Node} contains a value typed {@code T},
     * {@code next} and {@code prev} references to next and previous {@link Node}
     * in {@link LinkedList}.
     */
    public class Node {
        private final K key;
        private V element;
        private Node prev, next;

        public Node(K key, V element) {
            this.key = key;
            this.element = element;
        }

        public Node(K key, V element, Node prev, Node next) {
            this.key = key;
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        public @Nullable V getElement() {
            return element;
        }

        public void setElement(V element) {
            this.element = element;
        }

        public @Nullable Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public @Nullable Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public K getKey() {
            return key;
        }
    }

    private final int maxSize;

    private int size;
    private Node start, end;

    public LinkedList(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Push an {@code element} typed {@code T} to the front for current {@link LinkedList}.
     * Excepted an element to push.
     * Element will be placed in the front of {@link LinkedList} in case of
     * {@code size} if less than {@code maxSize}.
     *
     * Contract:
     * Inv:
     * {@link LinkedList}'s {@code size} must be less than or equals to {@code maxSize}.
     * Pre:
     * {@link LinkedList}'s {@code size} must be less than {@code maxSize}.
     * Post:
     * {@code element} must be set to the front of current list after push operation.
     *
     * Throws {@link AssertionError} otherwise.
     *
     * @param element An element to push.
     * @return An {@link Node} with {@code element} inside, placed to the front of list.
     */
    public @NotNull Node pushFront(K key, V element) {
        assert (getSize() < getMaxSize());

        size++;

        Node node = new Node(key, element, null, start);

        if (start == null) {
            start = node;
            end = node;
        } else {
            start.prev = node;
            start = node;
        }

        assert (node.equals(getStart()));
        assert (getSize() <= getMaxSize());

        return node;
    }

    /**
     * Remove an {@code element} typed {@code T} from the back of  current {@link LinkedList}.
     * The list must be not empty.
     *
     * Contract:
     * Inv:
     * {@link LinkedList}'s {@code size} must be less than or equals to {@code maxSize}.
     * Pre:
     * {@link LinkedList}'s {@code size} must be greater than 0 before pop operation.
     * Post:
     * {@code end} element of list must be replaced with {@code prev} of previous {@code end}.
     *
     * Throws {@link AssertionError} otherwise.
     *
     * @return Removed {@link Node} from the back of list.
     */
    public @NotNull Node popBack() {
        assert (getSize() <= getMaxSize());
        assert (getSize() > 0);

        size--;

        if (start.equals(end)) {
            Node popNode = start;

            start = null;
            end = null;

            return popNode;
        }

        Node popNode = end;

        end = end.prev;

        if (popNode.prev == null) {
            start = null;
        } else {
            end.next = null;
        }

        assert (getEnd().equals(popNode.prev));

        popNode.prev = null;

        assert (getSize() <= getMaxSize());

        return popNode;
    }

    /**
     * Move the {@code node} element to the front of current {@link LinkedList}.
     * The {@code node} element must contain in list.
     *
     * Contract:
     * Inv:
     * {@link LinkedList}'s {@code size} must be less than or equals to {@code maxSize}.
     * Pre:
     * {@code node} contains in current {@link LinkedList}.
     * Post:
     * {@code node} is the start of current {@link LinkedList}.
     *
     * Throws {@link AssertionError} otherwise.
     *
     * @param node Node to move it to front of list.
     */
    public void setFront(@NotNull Node node) {
        assert (getSize() <= getMaxSize());
        assert (contains(node));

        if (start.equals(node)) {
            return;
        }

        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        }

        if (end.equals(node)) {
            end = end.prev;
        }

        node.prev = null;
        node.next = start;

        start.prev = node;

        start = node;

        assert (getStart().equals(node));
        assert (getSize() <= getMaxSize());
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public int getSize() {
        return size;
    }

    public int getMaxSize() {
        return maxSize;
    }

    private boolean contains(Node node) {
        Node currentNode = start;

        while (!(currentNode.equals(end))) {
            if (currentNode.equals(node)) {
                return true;
            }

            currentNode = currentNode.next;
        }

        return currentNode.equals(node);
    }
}
