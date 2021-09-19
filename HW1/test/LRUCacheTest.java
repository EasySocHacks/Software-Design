import easy.soc.hacks.cache.LRUCache;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LRUCacheTest {
    private LRUCache<Integer, Integer> lruCache;

    @Before
    public void beforeTest() {
        lruCache = new LRUCache<>(1_000);
    }

    @Test
    public void createTest() {
        // No operations
    }

    @Test
    public void putWithoutOverflowTest() {
        for (int i = 0; i < 1_000; i++) {
            lruCache.put(i, i);
        }
    }

    @Test
    public void pushWithOverflowTest() {
        for (int i = 0; i < 1_000_000; i++) {
            lruCache.put(i, i);
        }
    }

    public void getWithoutOverflowTest() {
        for (int i = 0; i < 1_000; i++) {
            lruCache.put(i, i);
        }

        for (int i = 0; i < 1_000; i++) {
            assertEquals(i, (int) lruCache.get(i));
        }

        for (int i = 999; i >= 0; i--) {
            assertEquals(i, (int) lruCache.get(i));
        }
    }

    @Test
    public void getWithOverflow() {
        for (int i = 0; i < 2_000; i++) {
            lruCache.put(i, i);
        }

        for (int i = 0; i < 1_000; i++) {
            assertNull(lruCache.get(i));
        }

        for (int i = 1_000; i < 2_000; i++) {
            assertEquals(i, (int) lruCache.get(i));
        }
    }
}
