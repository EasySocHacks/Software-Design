package easy.soc.hacks.cache;

import easy.soc.hacks.utils.LinkedList;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A cache with LRU technique.
 *
 * @param <K> Type of {@code key}.
 * @param <V> Type of {@code value}.
 */
public class LRUCache<K, V> {
    private final int maxSize;

    private final Map<K, LinkedList<K, V>.Node> nodeMap = new HashMap<>();
    private final LinkedList<K, V> nodeList;

    public LRUCache(int maxSize) {
        this.maxSize = maxSize;

        nodeList = new LinkedList<>(maxSize);
    }

    /**
     * Gets element from cache with specific {@code key}.
     * {@code key} must be presented in cache.
     * Returns either a {@code value} specified by {@code key} if present or
     * {@code null} otherwise.
     *
     * Contract:
     * Inv:
     * Cache size must be less than or equals to {@code maxSize}.
     * Each element belonging cache must be present in {@code nodeList} as an
     * {@code element} of one of the {@link easy.soc.hacks.utils.LinkedList.Node}.
     * Each {@link easy.soc.hacks.utils.LinkedList.Node} presents in {@code nodeList}
     * must be present in {@code nodeMap} as value of a key.
     * Pre:
     * true
     * Post:
     * {@link easy.soc.hacks.utils.LinkedList.Node} belongs to {@code key} must
     * be placed in the start of {@code nodeList}.
     *
     * Throws {@link AssertionError} otherwise.
     *
     * @param key A {@code key} of getting element.
     * @return An element with {@code key}.
     */
    public @Nullable V get(K key) {
        assert (nodeList.getSize() <= nodeList.getMaxSize());

        if (nodeMap.containsKey(key)) {
            LinkedList<K, V>.Node node = nodeMap.get(key);

            nodeList.setFront(node);

            assert (nodeList.getStart().equals(node));
            assert (nodeMap.containsKey(key));

            return node.getElement();
        }

        return null;
    }

    /**
     * Puts element to cache with specific {@code key} and {@code value}.
     * If {@code size} of cache is equals to {@code maxSize} then
     * removes the element from cache belongs to {@link easy.soc.hacks.utils.LinkedList.Node}
     * in the end of {@code nodeList}.
     *
     * Contract:
     * Inv:
     * Cache size must be less than or equals to {@code maxSize}.
     * Each element belonging cache must be present in {@code nodeList} as an
     * {@code element} of one of the {@link easy.soc.hacks.utils.LinkedList.Node}.
     * Each {@link easy.soc.hacks.utils.LinkedList.Node} presents in {@code nodeList}
     * must be present in {@code nodeMap} as value of a key.
     * Pre:
     * true
     * Post:
     * {@link easy.soc.hacks.utils.LinkedList.Node} with element {@code value} must be
     * placed in the start of {@code nodeList}.
     * Element with specific {@code key} and {@code value} must contains in {@code nodeMap}.
     * In case of cache overflowing, redundant element must be removed from cache.
     *
     * Throws {@link AssertionError} otherwise.
     *
     * @param key Key to specify element.
     * @param value Value of element specified by {@code key}.
     */
    public void put(K key, V value) {
        assert (nodeList.getSize() <= nodeList.getMaxSize());

        if (nodeMap.containsKey(key)) {
            LinkedList<K, V>.Node node = nodeMap.get(key);
            node.setElement(value);

            nodeList.setFront(node);

            nodeMap.put(key, node);

            assert (nodeList.getStart().equals(node));
            assert (nodeMap.containsKey(key));

            return;
        }

        if (nodeList.getSize() == nodeList.getMaxSize()) {
            nodeMap.remove(nodeList.popBack().getKey());
        }

        LinkedList<K, V>.Node newNode = nodeList.pushFront(key, value);
        nodeMap.put(key, newNode);

        assert (nodeList.getStart().equals(newNode));
        assert (nodeMap.containsKey(key));
    }
}
