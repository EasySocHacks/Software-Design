import easy.soc.hacks.utils.LinkedList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedListTest {
    private LinkedList<Integer, Integer> linkedList;

    @Before
    public void beforeTest() {
        linkedList = new LinkedList<>(1_000);
    }

    @Test
    public void createTest() {
        // No operations
    }

    @Test
    public void pushFrontWithoutOverflowTest() {
        for (int i = 0; i < 100; i++) {
             linkedList.pushFront(i, (i + 123) % 23);
        }

        assertEquals(linkedList.getSize(), 100);
    }

    @Test(expected = AssertionError.class)
    public void pushFrontWithOverflowTest() {
        for (int i = 0; i < 1_000_000; i++) {
            linkedList.pushFront(i, (i + 321) % 34);
        }
    }

    @Test
    public void pushFrontWithoutOverflowAndCheckTest() {
        for (int i = 0; i < 1_000; i++) {
            linkedList.pushFront(i, (i + 123_45) % 123);
        }

        int i = 0;
        LinkedList<Integer, Integer>.Node node = linkedList.getEnd();

        while (true) {
            assertEquals(((i++) + 123_45) % 123, (int) node.getElement());

            if (node.equals(linkedList.getStart())) {
                break;
            }

            node = node.getPrev();
        }

        i = 999;
        node = linkedList.getStart();
        while (true) {
            assertEquals(((i--) + 123_45) % 123, (int) node.getElement());

            if (node.equals(linkedList.getEnd())) {
                break;
            }

            node = node.getNext();
        }
    }

    @Test
    public void pushFrontWithoutOverflowThanSetFrontAndCheckTest() {
        for (int i = 0; i < 1_000; i++) {
            linkedList.pushFront(i, (i + 999) % 123_456_7);
        }

        linkedList.setFront(linkedList.getEnd());

        assertEquals(999 % 123_456_7, (int) linkedList.getStart().getElement());

        int i = 999;

        LinkedList<Integer, Integer>.Node node = linkedList.getStart();
        node = node.getNext();

        while (true) {
            assertEquals(((i--) + 999) % 123_456_7, (int) node.getElement());

            if (node.equals(linkedList.getEnd())) {
                break;
            }

            node = node.getNext();
        }
    }

    @Test(expected = AssertionError.class)
    public void popBackWithUnderflowTest() {
        for (int i = 0; i < 1_000; i++) {
            linkedList.popBack();
        }
    }

    @Test
    public void popBackWithoutUnderflowTest() {
        for (int i = 0; i < 1_000; i++) {
            linkedList.pushFront(i, i);
        }

        for (int i = 0; i < 1_000; i++) {
            linkedList.popBack();
        }

        assertEquals(linkedList.getSize(), 0);

        for (int i = 0; i < 1_000; i++) {
            linkedList.pushFront(i, i);
        }

        for (int i = 0; i < 999; i++) {
            linkedList.popBack();
        }

        assertEquals(linkedList.getSize(), 1);

        linkedList.popBack();

        assertEquals(linkedList.getSize(), 0);
    }
}
