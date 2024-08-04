// Created by Ido Weinsteinת Yuval Ramot & Leah London Arazi

// NOTE: This module requires implementation of the following getters:
// 1. BinomialHeap.getFirst
// 2. HeapNode.getRank
// 3. HeapNode.getMarked
// 4. HeapNode.getParent
// 5. HeapNode.getNext
// 6. HeapNode.getPrev
// 7. HeapNode.getChild

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

//BinomialHeap Tester

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Map.Entry;

class Heap2 {
    private TreeSet<Integer> set;

    Heap2() {
        this.set = new TreeSet<>();
    }

    public int size() {
        return this.set.size();
    }

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public void insert(int v) {
        this.set.add(v);
    }

    public int deleteMin() {
        int min = this.set.first();
        this.set.remove(this.set.first());
        return min;
    }

    public int findMin() {
        if (this.isEmpty())
            return -1;
        return this.set.first();
    }

    public void delete(int i) {
        this.set.remove(i);

    }
}

class LabeledPair extends Pair<Integer, String> {
    public LabeledPair(int k, String label) {
        super(k, label);
    }

    public int k() {
        return this.first;
    }

    public String label() {
        return this.second;
    }
}

class Pair<T, S> {
    public final T first;
    public final S second;

    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }
}

@TestMethodOrder(OrderAnnotation.class)
public class TestBinomialHeap {
    static HeapPrinter heapPrinter = new HeapPrinter(System.out);

    BinomialHeap heap = new BinomialHeap();
    Heap heapModel;
    boolean uniqueValues = true;


    static void print(BinomialHeap heap) {
        boolean verbose = true;
        heapPrinter.print(heap, verbose);
    }
    
    private boolean isTreeInHeap(int size, int bitnum) {
        return !((size & (1 << bitnum)) == 0);
    }

    void assertValidHeapRoots(BinomialHeap heap, boolean checkSingularMin) {
        int numberOfTrees = 0;
//        Map<Integer, Integer> actualRanks = new HashMap<>();
        BinomialHeap.HeapNode node = heap.last;
        BinomialHeap.HeapNode actualMin = node;
        BinomialHeap.HeapItem min = heap.findMin();

        /* Check roots */
        do {
            numberOfTrees++;
            assertNull(node.parent);
            assertFalse(node.next == null);
            if (node.item.key < actualMin.item.key) {
                actualMin = node;
            }
            assertTrue(isTreeInHeap(heap.size(), node.rank));
//            actualRanks.merge(node.rank, 1, Integer::sum); // increase value by 1 or put 1 if absent
            node = node.next;
        } while (node != null && node != heap.last);

        String details = min.key < actualMin.item.key ? "findMin() node is NOT a sibling of last node" : "";
        if (checkSingularMin) {
            assertSame(
                min.node, actualMin,
                String.format(
                        "received key %d from findMin but found min root key %d in heap. %s",
                        min.key, actualMin.item.key, details));
        } else {
            assertEquals(
                min.key, actualMin.item.key,
                String.format(
                    "received key %d from findMin but found min root key %d in heap. %s",
                    min.key, actualMin.item.key, details));
        }


        assertEquals(heap.numTrees(), numberOfTrees);
    }

    void assertValidHeapNodeChildren(BinomialHeap.HeapNode node) {
        // Check its relations to its children
        if (node.child == null) {
            assertEquals(0, node.rank);
            return;
        }

        int childrenCount = 0;
        BinomialHeap.HeapNode currentChild = node.child;
        assertNotSame(node, currentChild);

        do {
            childrenCount++;
            assertSame(node, currentChild.parent);
            // Check heap property
            if (this.uniqueValues) {
                assertTrue(currentChild.item.key > node.item.key);
            } else {
                assertTrue(currentChild.item.key >= node.item.key);
            }
            currentChild = currentChild.next;
        } while (currentChild != null && currentChild != node.child);

        if (childrenCount != node.rank) {
            assertEquals(
                    childrenCount, node.rank,
                    String.format(
                            "Node with key %d has rank %d but only %d %s",
                            node.item.key, node.rank, childrenCount,
                            childrenCount == 1 ? "child" : "children"));
        }
    }


    void assertValidHeapNodes(BinomialHeap heap) {
        /* Check all nodes */
        BinomialHeap.HeapNode node = heap.last;
        Stack<BinomialHeap.HeapNode> stack = new Stack<>();
        Set<BinomialHeap.HeapNode> visited = new HashSet<>();
        int actualSize = 0;

        visited.add(null);
        stack.add(node);

        /* Traverse the heap using 'pre-order' DFS */
        while (!visited.contains(node) || !stack.empty()) {
            if (visited.contains(node)) {
                node = stack.pop();
                continue;
            }
            visited.add(node);

            // Check current node
            actualSize++;
            stack.push(node.next);

            assertValidHeapNodeChildren(node);

            node = node.child;
        }

        assertEquals(heap.size(), actualSize);
    }

    void assertValidHeap(BinomialHeap heap) {
        int size = heap.size();

        BinomialHeap.HeapNode node = heap.last;
        BinomialHeap.HeapItem min = heap.findMin();

        /* Handle empty heap */
        assertTrue(heap.empty() == (size == 0));
        if (heap.empty()) {
            assertNull(min);
            assertNull(node);
            return;
        }

        assertNotNull(node);
        assertNotNull(min);
        assertNotNull(min.node);
        assertNull(min.node.parent);

        assertValidHeapRoots(heap, this.uniqueValues);
        assertValidHeapNodes(heap);
    }

    Map<Integer, BinomialHeap.HeapItem> testInsertion(BinomialHeap heap, Iterable<Integer> keys) {
        Map<Integer, BinomialHeap.HeapItem> nodes = new HashMap<>();
        BinomialHeap.HeapItem minNode = heap.findMin();
        int startSize = heap.size();
        int length = 0;

        for (int key : keys) {
            length++;
            BinomialHeap.HeapItem current = heap.insert(key, Integer.toString(key));
            assertValidHeap(heap);
            minNode = minNode == null || key < minNode.key ? current : minNode;
            nodes.put(key, current);
        }

        if (this.uniqueValues) {
            assertSame(minNode, heap.findMin());
        } else {
            assertEquals(minNode.key, heap.findMin().key);
        }

        assertEquals(length, heap.size() - startSize);

        return nodes;
    }

    static Iterable<Integer> toArray(int[] array) {
        return () -> Arrays.stream(array).iterator();
    }

    Map<Integer, BinomialHeap.HeapItem> testInsertion(BinomialHeap heap, int... keys) {
        return this.testInsertion(heap, toArray(keys));
    }

    Map<Integer, BinomialHeap.HeapItem> testInsertionReverse(BinomialHeap heap, int lower, int upper) {
        return this.testInsertion(heap, IntStream.range(lower, upper + 1).map(i -> upper - i + lower)::iterator);
    }

    Map<Integer, BinomialHeap.HeapItem> testInsertionReverse(BinomialHeap heap, int lower) {
        return this.testInsertionReverse(heap, lower, lower + 999);
    }

    void testDeletion(BinomialHeap heap, BinomialHeap.HeapItem... nodes) {
        int size = heap.size();

        for (BinomialHeap.HeapItem node : nodes) {
            heap.delete(node);
            size--;
            assertValidHeap(heap);
            assertEquals(size, heap.size());
        }
    }

    void testDeletion(BinomialHeap heap, List<BinomialHeap.HeapItem> nodes) {
        this.testDeletion(heap, nodes.toArray(new BinomialHeap.HeapItem[nodes.size()]));
    }


    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {
        this.heap = new BinomialHeap();
        this.heapModel = new Heap();
        this.uniqueValues = !testInfo.getTags().contains("DuplicateValues");
    }

    @AfterEach
    void afterEachTest(TestInfo testInfo) throws IOException {
        if (testInfo.getTags().contains("NoCompare")) {
            return;
        }
        String expectedFile = "./expected2/" + testInfo.getDisplayName() + ".txt";
        String resultFile = "./result/" + testInfo.getDisplayName() + ".txt";


        File dir = new File("result");
        dir.mkdirs();
        File file = new File(dir, testInfo.getDisplayName() + ".txt");
        file.createNewFile();
        try (PrintStream stream = new PrintStream(file)) {
            HeapPrinter printer = new HeapPrinter(stream);
            printer.print(heap, true);
        }

        String result = new String(
                Files.readAllBytes(Paths.get(resultFile)),
                StandardCharsets.UTF_8);

        String expected = null;
        try {
            expected = new String(
                Files.readAllBytes(Paths.get(expectedFile)),
                StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("Missing expected file");
        }
        if (!expected.replace("\r\n", "\n").equals(result)) {
            assertTrue(false,
                String.format("Expected file %s and result file %s do not match",
                    expectedFile, resultFile));
        }
    }

//    @Tag("NoCompare")
//    @Test
//    @Order(0)
//    public void testNodeSanity() {
//        BinomialHeap.HeapItem node = new BinomialHeap.HeapItem(5, "5");
//        assertEquals(5, node.key);
//    }

    @Test
    @Order(2)
    public void testConstructorSanity() {
        assertValidHeap(heap);
        assertTrue(heap.empty());
    }

    @Test
    @Order(2)
    public void testInsertDeleteSanity() {
        // case 2
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 2);
        testDeletion(heap, nodes.get(2));
        assertTrue(heap.empty());
    }

    @Test
    @Order(2)
    public void testkMinSanity() {
        // case 14
        testInsertion(heap, 1);
//        int[] keys = BinomialHeap.kMin(heap, 1);
//        assertEquals(1, keys.length);
//        assertEquals(1, keys[0]);
        assertEquals(1, heap.size());
        assertEquals(1, heap.findMin().key);
        assertNotNull(heap.last);
        assertSame(heap.findMin(), heap.last.item);
        assertSame(heap.last, heap.last.next);
    }

//    @Test
//    @Order(2)
//    public void testCountersRepSanity() {
//        // case 12
//        testInsertion(heap, 1);
//        int[] arr = heap.countersRep();
//        assertValidHeap(heap);
//        assertEquals(1, heap.size());
//        assertEquals(1, heap.findMin().key);
//        assertTrue(Arrays.equals(new int[] { 1 }, arr));
//    }

    @Test
    @Order(6)
    public void testInsertionDeletion1() {
        // case 1
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(
                heap, 2, 1, 3, 7, 4, 8, 6, 5, 9, 10, 11);
        heap.deleteMin();
        assertValidHeap(heap);
        testDeletion(heap, nodes.get(9));
    }

    @Test
    @Order(3)
    public void testInsertionDeletion2() {
        // case 3
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(
                heap, 2, 1, 3);
        testDeletion(heap, nodes.get(2));
    }

    @Test
    @Order(4)
    public void testInsertionDeletion3() {
        // case 4
        testInsertion(heap, 20, 8, 3, 100, 15, 18, 1);
        heap.deleteMin();
        assertValidHeap(heap);
        assertEquals(3, heap.findMin().key);
    }

    @Test
    @Order(7)
    public void testInsertionDeletion4() {
        // case 5
        testInsertion(heap, 7, 2, 1, 18, 15, 100, 3, 8, 20);
        heap.deleteMin();
        assertValidHeap(heap);
        assertEquals(2, heap.findMin().key);
        testInsertion(heap, 500);
    }

    @Test
    @Order(42)
    public void testCut() {
        // case 8
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(
                heap, 2, 1, 3, 7, 4, 8, 6, 5, 9, 10, 11);
        heap.deleteMin();
        assertValidHeap(heap);
        testDeletion(heap, nodes.get(5));
    }

    @Test
    @Order(7)
    public void testCutDirectIndirectChild() {
        // case 13
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(
                heap, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(1), heap.findMin());
        heap.decreaseKey(nodes.get(5), 9);
        assertValidHeap(heap);
        heap.decreaseKey(nodes.get(6), 20);
        assertValidHeap(heap);
    }

    @Test
    @Order(61)
    public void testCascadingCuts() {
        // case 9
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(
                heap, 2, 1, 3, 7, 4, 8, 6, 5, 9, 10, 11, 12, 13, 14, 15, 16, 17);
        heap.deleteMin();
        assertValidHeap(heap);
        heap.decreaseKey(nodes.get(16), 17);
        assertValidHeap(heap);
        heap.decreaseKey(nodes.get(12), 14);
        assertValidHeap(heap);
        testDeletion(heap, nodes.get(15));
    }

//    @Test
//    @Order(1)
//    public void testkMinEmpty() {
//        int[] keys = BinomialHeap.kMin(heap, 0);
//        assertEquals(0, keys.length);
//        assertTrue(heap.empty());
//        assertValidHeap(heap);
//    }

//    @Test
//    @Order(7)
//    public void testkMinBinomial() {
//        // case 6
//        testInsertion(heap, 7, 2, 1, 18, 15, 100, 3, 8, 20);
//        heap.deleteMin();
//        assertValidHeap(heap);
//        int[] arr = BinomialHeap.kMin(heap, 8);
//        assertValidHeap(heap);
//        assertEquals(8, heap.size());
//        assertTrue(Arrays.equals(new int[] { 2, 3, 7, 8, 15, 18, 20, 100 }, arr));
//    }

//    @Test
//    @Order(3)
//    public void testkMinSingle() {
//        // case 7
//        testInsertion(heap, 7, 6);
//        heap.deleteMin();
//        assertValidHeap(heap);
//        int[] arr = BinomialHeap.kMin(heap, 1);
//        assertTrue(Arrays.equals(new int[] { 7 }, arr));
//    }

    Map<Integer, BinomialHeap.HeapItem> addKeys(int start) {
        Map<Integer, BinomialHeap.HeapItem> nodes = this.testInsertion(heap, IntStream.rangeClosed(start, start + 999)::iterator);
        for (int i = 0; i < 1000; i++) {//@@@@@@@ i<1000 @@@@@
            heapModel.insert(start + i);
        }
        return nodes;
    }

    Map<Integer, BinomialHeap.HeapItem> addKeysReverse(int start) {
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertionReverse(heap, start);
        for (int i = 999; i >= 0; i--) {
            heapModel.insert(start + i);
        }
        return nodes;
    }

    @Test
    @Order(2)
    void testInsertionSanity2() {
        // test0
        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        testInsertion(heap, numbers);

        for (int i = 0; i < 5; i++) {
            assertEquals(i, heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }
    }

    @Test
    @Order(525)
    void testInOrderInsert() {
        // test1
        addKeys(0);
        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(199)
    void testReverseOrderInsert() {
        // test2
        addKeysReverse(0);
        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }


    @Test
    @Order(1300)
    void testMixedOrderInsert() {
        // test3
        addKeys(0);
        addKeysReverse(4000);
        addKeys(2000);
        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(3900)
    void testMixedOrderTwoStepsInsert() {
        // test4
        addKeys(0);
        addKeysReverse(4000);
        addKeys(2000);

        for (int i = 0; i < 1000; i++) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
        }

        addKeys(6000);
        addKeysReverse(8000);
        addKeys(10000);

        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    //@Disabled // Same value insertion is not supported in our version
    @Tag("DuplicateValues")
    @Test
    @Order(1200)
    void testSameValueInsert() {
        // test5
        addKeys(0);
        addKeys(0);
        addKeys(0);

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(i,  heap.findMin().key);
                heap.deleteMin();
                assertValidHeap(heap);
            }
        }

        assertTrue(heap.empty());
    }


    //@Disabled // Same value insertion is not supported in our version
    @Tag("DuplicateValues")
    @Test
    @Order(9000)
    void testSameValueMixedOrderInsert() {
        // test6
        addKeysReverse(1000);
        addKeysReverse(1000);
        addKeys(0);
        addKeys(0);
        addKeys(1000);
        addKeys(1000);
        addKeysReverse(0);
        addKeysReverse(0);

        for (int i = 0; i < 2000; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(i,  heap.findMin().key);
                heap.deleteMin();
                assertValidHeap(heap);
            }
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(1600)
    void testMixedOrderInsertDelete() {
        // test7
        addKeys(1000);
        addKeysReverse(3000);

        Map<Integer, BinomialHeap.HeapItem> nodes = addKeys(2000);

        for (int i = 2000; i < 2500; i++) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.delete(i);
            heap.delete(nodes.get(i));
            assertValidHeap(heap);
        }

        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(1400)
    void testMixedOrderInsertDelete2() {
        // test8
        addKeys(7000);
        addKeysReverse(9000);

        Map<Integer, BinomialHeap.HeapItem> nodes = addKeys(2000);

        for (int i = 2000; i < 2500; i++) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.delete(i);
            heap.delete(nodes.get(i));
            assertValidHeap(heap);
        }

        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(1600)
    void testMixedOrderInsertDelete3() {
        // test9
        addKeys(7000);
        addKeysReverse(9000);

        Map<Integer, BinomialHeap.HeapItem> nodes = addKeys(2000);

        for (int i = 2700; i > 2200; i--) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.delete(i);
            heap.delete(nodes.get(i));
            assertValidHeap(heap);
        }

        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(1300)
    void testConsolidatedMixedOrderInsertDelete() {
        // test10
        addKeys(7000);
        addKeysReverse(9000);

        Map<Integer, BinomialHeap.HeapItem> nodes = addKeys(2000);

        heapModel.deleteMin();
        heap.deleteMin();

        assertValidHeap(heap);

        for (int i = 2700; i > 2200; i--) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.delete(i);
            heap.delete(nodes.get(i));
            assertValidHeap(heap);
        }

        while (!heapModel.empty()) {
            assertEquals(heapModel.findMin(),  heap.findMin().key);
            assertEquals(heapModel.size(), heap.size());
            heapModel.deleteMin();
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(587)
    void testDecreaseKey() {
        // test11
        addKeys(1000);
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 9999);
        heap.decreaseKey(nodes.get(9999), 9999);
        assertValidHeap(heap);
        assertEquals(0, heap.findMin().key);

        heap.deleteMin();
        assertValidHeap(heap);

        for (int i = 1000; i < 2000; i++) {
            assertEquals(i,  heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    //@Disabled // Same value insertion is not supported in our version
    @Tag("DuplicateValues")
    @Test
    @Order(235)
    void testMultipleMinDecreaseKey() {
        // test12
        addKeys(1000);
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 5000);
        heap.decreaseKey(nodes.get(5000), 4000);
        assertValidHeap(heap);

        for (int i = 0; i < 2; i++) {
            assertEquals(1000,  heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }

        for (int i = 1001; i < 2000; i++) {
            assertEquals(i, heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(162)
    void testDecreaseKey2() {
        // test13
        addKeys(1000);
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 9000);
        heap.decreaseKey(nodes.get(9000), 4000);
        assertValidHeap(heap);

        for (int i = 1000; i < 2000; i++) {
            assertEquals(i,  heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertEquals(5000, heap.findMin().key);
        heap.deleteMin();
        assertValidHeap(heap);

        assertTrue(heap.empty());
    }

    @Test
    @Order(602)
    void testMixedOrderDecreaseKey() {
        // test14
        addKeys(1000);
        addKeysReverse(7000);

        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 9000);
        heap.decreaseKey(nodes.get(9000), 4000);
        assertValidHeap(heap);

        for (int i = 1000; i < 2000; i++) {
            assertEquals(i,  heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }
        assertEquals(5000, heap.findMin().key);
        heap.deleteMin();
        assertValidHeap(heap);

        for (int i = 7000; i < 8000; i++) {
            assertEquals(i,  heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    @Test
    @Order(10300)
    void testConsolidatedDecreaseKey() {
        // test15

        for (int i = 1000; i < 10000; i += 1000) {
            addKeys(i);
        }

        heap.deleteMin();
        assertValidHeap(heap);

        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 99999);
        heap.decreaseKey(nodes.get(99999), 99999);

        assertEquals(0, heap.findMin().key);
        heap.deleteMin();
        assertValidHeap(heap);

        for (int i = 1001; i < 10000; i++) {
            assertEquals(i, heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }

        assertTrue(heap.empty());
    }

    //@Disabled // Same value insertion is not supported in our version
    @Tag("DuplicateValues")
    @Test
    @Order(2)
    void testDuplicateKeysDecreaseKeyAuxVariables() {
        // test20

        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 4, 5, 6);
        heap.deleteMin();
        assertValidHeap(heap);

        testInsertion(heap, 1, 2, 3);
        heap.deleteMin();
        assertValidHeap(heap);

        testInsertion(heap, 1);
        heap.deleteMin();
        assertValidHeap(heap);

//        int cuts = BinomialHeap.totalCuts();
//        int links = BinomialHeap.totalLinks();

        heap.decreaseKey(nodes.get(6), 2);
        assertValidHeap(heap);
        heap.decreaseKey(nodes.get(5), 1);
        assertValidHeap(heap);

//        assertEquals(4, heap.potential());
//        assertEquals(1, BinomialHeap.totalCuts() - cuts);
//        assertEquals(0, BinomialHeap.totalLinks() - links);
    }

    @Test
    @Order(94900)
    void testLargeTreeInsertDeleteMinPotential() {
        // test21

        int treeSize = 32768;
        int sizeToDelete = 1000;

        testInsertion(heap, IntStream.rangeClosed(treeSize, treeSize * 2 - 1)::iterator);
        testInsertion(heap, IntStream.rangeClosed(0, sizeToDelete - 1)::iterator);

        for (int i = 0; i < sizeToDelete; i++) {
            heap.deleteMin();
            assertValidHeap(heap);
        }

//        assertEquals(1, heap.potential());
    }

    //@Disabled // Same value insertion is not supported in our version
    @Tag("DuplicateValues")
    @Tag("NoCompare") // Test uses Collections.shuffle
    @Test
    @Order(202300)
    void testLargeTreeInsertDeleteMinDecreaseKeyAuxVariables() {
        // test22

        List<BinomialHeap.HeapItem> nodes = new ArrayList<>();
        int treeSize = 32768;
        int sizeToDelete = 1000;

        for (int i = treeSize; i < treeSize * 2; i++) {
            nodes.add(heap.insert(i, Integer.toString(i))); // don't check assertValidHeap each time in order to reduce runtime
        }
        for (int i = 0; i < 1000; i++) {
            heap.insert(i, Integer.toString(i)); // don't check assertValidHeap each time in order to reduce runtime
        }
        assertValidHeap(heap);

        for (int i = 0; i < sizeToDelete; i++) {
            heap.deleteMin(); // don't check assertValidHeap each time in order to reduce runtime
        }
        assertValidHeap(heap);
//        assertEquals(1, heap.potential());
//
//        int totalCuts = BinomialHeap.totalCuts();
//        int links = BinomialHeap.totalLinks();

//        boolean noCascading = true;
//        int iterationCuts;

        Collections.shuffle(nodes);

        for (int i = 0; i < treeSize; i++) {
//            iterationCuts = BinomialHeap.totalCuts();

            heap.decreaseKey(nodes.get(i), nodes.get(i).key - (treeSize - i));
            assertValidHeap(heap);

//            if (BinomialHeap.totalCuts() - iterationCuts > 1)
//                noCascading = false;
        }

        assertValidHeap(heap);

//        assertEquals(treeSize, heap.potential());
//        assertEquals(treeSize - 1, BinomialHeap.totalCuts() - totalCuts);
//        assertEquals(0, BinomialHeap.totalLinks() - links);
//        assertTrue(Arrays.equals(new int[] {treeSize}, heap.countersRep()));
//        assertFalse(noCascading);
    }

    @Test
    @Order(1100)
    void testInsertionPartialDeleteMinAuxVariables2() {
        // test27

        int size = 2000;
//        int totalCuts = BinomialHeap.totalCuts();
//        int links = BinomialHeap.totalLinks();

        int remaining = size;
        while (remaining > 0) {
            addKeysReverse(remaining - 999);
            remaining -= 1000;
        }

        for (int i = 0; i < size / 2; i++) {
            assertEquals(i + 1,  heap.findMin().key);
            heap.deleteMin();
            assertValidHeap(heap);
        }

//        assertTrue(heap.potential() <= 100);
//        assertEquals(0, BinomialHeap.totalCuts() - totalCuts);
//        assertTrue(BinomialHeap.totalLinks() - links >= size - 100);
    }

//    @Test
//    @Order(55)
//    void testkMinSanity2() {
//        // test29
//        // kMin
//        testInsertion(heap, IntStream.rangeClosed(0, 32)::iterator);
//
//        heap.deleteMin();
//        assertValidHeap(heap);
//
//        int[] kmin = BinomialHeap.kMin(heap, 10);
//        assertTrue(Arrays.equals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, kmin));
//    }

    @Test
    @Order(98)
    void testMeldSanity() {
        // test30
        // insert and meld
        BinomialHeap firstBinomialHeap = new BinomialHeap();
        BinomialHeap secondBinomialHeap = new BinomialHeap();
        testInsertion(firstBinomialHeap, IntStream.rangeClosed(0, 99)::iterator);
        testInsertion(secondBinomialHeap, IntStream.rangeClosed(100, 199)::iterator);

        firstBinomialHeap.meld(secondBinomialHeap);
        assertValidHeap(heap);

        for (int i = 0; i < 200; i++) {
            BinomialHeap.HeapItem min = firstBinomialHeap.findMin();
            assertValidHeap(heap);
            assertNotNull(min);
            assertEquals(i, min.key);
            firstBinomialHeap.deleteMin();
            assertValidHeap(heap);
        }
    }

    Random rand = new Random();
    private void insertKRandomKeys(int k, Map<Integer, BinomialHeap.HeapItem> nodes,
            Map<Integer, BinomialHeap.HeapItem> otherNodes, BinomialHeap heap, int lowBound,
            int upperBound) {
        if (lowBound >= upperBound) {
            return;
        }

        for (int i = 0; i < k; i++) {
            int toIns = rand.nextInt(upperBound - lowBound) + lowBound;
            inserNode(nodes, otherNodes, heap, toIns);
        }
    }

    private void inserNode(Map<Integer, BinomialHeap.HeapItem> nodes, Map<Integer,
            BinomialHeap.HeapItem> otherNodes, BinomialHeap heap, int num) {
        if (!nodes.containsKey(num) && !otherNodes.containsKey(num)) {
            nodes.put(num, heap.insert(num, Integer.toString(num)));
        }
    }

    private void deleteOrDecreaseOrMin(Map<Integer, BinomialHeap.HeapItem> nodes,
            Map<Integer, BinomialHeap.HeapItem> otherNodes, BinomialHeap heap) {
        if (heap.empty()) {
            return;
        }

        int indicator = rand.nextInt(3);
        if (indicator == 2) {
            nodes.remove(heap.findMin().key);
            heap.deleteMin();
            return;
        }

        List<Entry<Integer, BinomialHeap.HeapItem>> keys = new LinkedList<>(nodes.entrySet());
        int index = rand.nextInt(keys.size());

        if (indicator == 0) {
            // delete
            heap.delete(keys.get(index).getValue());
            nodes.remove(keys.get(index).getKey());
        } else {
            // decreaseKey
            int decreaseVal = rand.nextInt(2000);
            int key = keys.get(index).getKey();
            if (!(nodes.containsKey(key - decreaseVal)) && !(otherNodes.containsKey(key - decreaseVal))) {
                heap.decreaseKey(keys.get(index).getValue(), decreaseVal);
                nodes.remove(keys.get(index).getKey());
                nodes.put(keys.get(index).getKey() - decreaseVal, keys.get(index).getValue());
            }
        }
    }

    private void meld(Map<Integer, BinomialHeap.HeapItem> nodes, BinomialHeap heap,
            Map<Integer, BinomialHeap.HeapItem> nodes2, BinomialHeap heap2) {
        heap.meld(heap2);
        nodes.putAll(nodes2);
    }

    @Tag("NoCompare") // Test uses Collections.shuffle and Random
    @Test
    @Order(864400)
    public void stressTest() {
        final int changes = 35;
        final int meldings = 5;
        final int iterations = 10000;
        BinomialHeap stressHeap = null;
        BinomialHeap stressHeap2 = null;
        Map<Integer, BinomialHeap.HeapItem> nodes = null;
        Map<Integer, BinomialHeap.HeapItem> nodes2 = null;
        for (int j = 0; j < iterations; j++) {
            System.out.println("Iteration #" + j);
            stressHeap = new BinomialHeap();
            nodes = new HashMap<>();
            for (int i = 0; i < meldings; i++) {
                nodes2 = new HashMap<>();
                stressHeap2 = new BinomialHeap();
                insertKRandomKeys(1000, nodes, nodes, stressHeap, 0, (i+1) * 1000);
                insertKRandomKeys(1000, nodes2, nodes2, stressHeap2, (i + 1) * 1000 + 1, (i + 2) * 1000);
                assertValidHeap(stressHeap);
                assertValidHeap(stressHeap2);
                for (int k = 0; k < changes; k++) {
                    deleteOrDecreaseOrMin(nodes, nodes2, stressHeap);
                    deleteOrDecreaseOrMin(nodes2, nodes, stressHeap2);
                    assertValidHeap(stressHeap);
                    assertValidHeap(stressHeap2);

                }
                meld(nodes, stressHeap, nodes2, stressHeap2);
                assertValidHeap(stressHeap);
            }
        }
    }

    @Test
    @Order(64)
    public void testInsertionOrder() {
        testInsertion(heap, 5, 8, 2, 0);

        BinomialHeap.HeapNode last = heap.last;
        assertEquals(last.item.key, 0);
        assertEquals(last.next.item.key, 2);
        assertEquals(last.next.next.item.key, 8);
        assertEquals(last.next.next.next.item.key, 5);
    }

    @Test
    @Order(84)
    public void testConsolidationOrder() {
        testInsertion(heap, IntStream.rangeClosed(0, 7)::iterator);
        heap.deleteMin();
        assertValidHeap(heap);

        BinomialHeap.HeapNode last = heap.last;
        assertEquals(0, last.rank);
        assertEquals(1, last.item.key);
        assertEquals(last.next.rank, 1);
        assertEquals(2, last.next.item.key);
        assertEquals(last.next.next.rank, 2);
        assertEquals(4, last.next.next.item.key);
    }

    @Test
    @Order(72)
    public void testDecreaseKeyOrder() {
        Map<Integer, BinomialHeap.HeapItem> nodes =
            testInsertion(heap, IntStream.rangeClosed(0, 7)::iterator);
        heap.deleteMin();
        assertValidHeap(heap);

        heap.decreaseKey(nodes.get(6), 6);
        assertValidHeap(heap);
        assertSame(nodes.get(6), heap.last);
    }

    @Test
    @Order(48)
    public void testDeleteMinEdge1() {
        // Delete min when min = first, it no children and no siblings
        testInsertion(heap, 0);
        heap.deleteMin();
        assertValidHeap(heap);
        assertTrue(heap.empty());
    }

    @Test
    @Order(56)
    public void testDeleteMinEdge2() {
        // Delete min when min = first, it has has no children and has a sibling
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 2, 0);
        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(2), heap.findMin());
    }

    @Test
    @Order(55)
    public void testDeleteMinEdge3() {
        // Delete min when min = first, it has a child but has no siblings
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 2, 1, 0);
        heap.deleteMin();
        assertValidHeap(heap);
        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(2), heap.findMin());
    }

    @Test
    @Order(63)
    public void testDeleteMinEdge4() {
        // Delete min when min = first, it has multiple children but has no siblings
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 4, 3, 2, 1, 0);
        heap.deleteMin();
        assertValidHeap(heap);
        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(2), heap.findMin());
        assertSame(nodes.get(2), heap.last);
        assertSame(nodes.get(3), heap.last.next);
        assertSame(nodes.get(4), heap.last.next.child);
    }

    @Test
    @Order(62)
    public void testDeleteMinEdge5() {
        // Delete min when min = first, it has a child and has a sibling
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 3, 1, 2, 0);
        heap.deleteMin();
        assertValidHeap(heap);
        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(2), heap.findMin());
    }

    @Test
    @Order(72)
    public void testDeleteMinEdge6() {
        // Delete min when min = first, it has multiple children and has a sibling
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 4, 1, 2, 3, 0);
        heap.deleteMin();
        assertValidHeap(heap);

        BinomialHeap heap2 = new BinomialHeap();
        nodes.putAll(testInsertion(heap2, 5));
        heap.meld(heap2);
        assertValidHeap(heap);

        heap.deleteMin();
        assertValidHeap(heap);
        assertEquals(4, heap.size());
        assertSame(nodes.get(2), heap.findMin());
        assertSame(nodes.get(2), heap.last);
        assertSame(nodes.get(4), heap.last.child);
        assertSame(nodes.get(3), heap.last.child.next);
        assertSame(nodes.get(5), heap.last.child.child);
    }

    @Test
    @Order(58)
    public void testDeleteMinEdge7() {
        // Delete min when min != first, it has no child and has a siblings
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 0, 2);
        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(2), heap.findMin());
    }

    @Test
    @Order(72)
    public void testDeleteMinEdge8() {
        // Delete min when min != first, it has no child and has multiple siblings
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 3, 0, 2);
        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(2), heap.findMin());
    }

    @Test
    @Order(77)
    public void testDeleteMinEdge9() {
        // Delete min when min != first, it has a child and has a sibling
        testInsertionReverse(heap, 5, 6);
        testInsertion(heap, 0);

        BinomialHeap heap2 = new BinomialHeap();
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap2, 0, 1, 2, 3, 4);

        heap.deleteMin();
        assertValidHeap(heap);
        heap2.deleteMin();
        assertValidHeap(heap2);

        heap.meld(heap2);
        assertValidHeap(heap);

        heap.deleteMin();
        assertValidHeap(heap);

        assertSame(nodes.get(2), heap.last);
        assertSame(nodes.get(3), heap.last.next);
    }

    @Test
    @Order(86)
    public void testDeleteMinEdge10() {
        // Delete min when min != first, it has multiple children and has a sibling
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, -20);

        BinomialHeap heap2 = new BinomialHeap();
        nodes.putAll(testInsertion(heap2, -100, -30, -25, -15, -10));
        heap2.deleteMin();
        assertValidHeap(heap2);

        heap.meld(heap2);
        assertValidHeap(heap);

        heap.deleteMin();
        assertValidHeap(heap);
        assertEquals(4, heap.size());
        assertSame(nodes.get(-25), heap.findMin());
        assertSame(nodes.get(-25), heap.last);
        assertSame(nodes.get(-15), heap.last.child);
        assertSame(nodes.get(-20), heap.last.child.next);
        assertSame(nodes.get(-10), heap.last.child.child);
    }

    @Test
    @Order(82)
    public void testDeleteMinEdge11() {
        // Delete min when min != first, it has a child and has multiple siblings
        testInsertionReverse(heap, 5, 6);
        testInsertion(heap, 0);
        BinomialHeap heap2 = new BinomialHeap();
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap2, 0, 1, 2, 3, 4);
        BinomialHeap heap3 = new BinomialHeap();
        testInsertion(heap3, 100);
        heap.deleteMin();
        assertValidHeap(heap);
        heap2.deleteMin();
        assertValidHeap(heap2);
        heap.meld(heap2);
        heap.meld(heap3);
        assertValidHeap(heap);
        heap.deleteMin();
        assertSame(nodes.get(2), heap.last);
        assertSame(nodes.get(3), heap.last.next);
    }

    @Test
    @Order(132)
    public void testDeleteMinEdge12() {
        // Delete min when min != first, it has multiple children and has multiple siblings
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, IntStream.rangeClosed(9, 16)::iterator);

        BinomialHeap heap2 = new BinomialHeap();
        nodes.putAll(testInsertion(heap2, IntStream.rangeClosed(1, 8)::iterator));

        BinomialHeap heap3 = new BinomialHeap();
        nodes.putAll(testInsertion(heap3, IntStream.rangeClosed(17, 32)::iterator));

        for (BinomialHeap h : Arrays.asList(heap, heap2, heap3)) {
            testInsertion(h, Integer.MIN_VALUE);
            h.deleteMin();
            assertValidHeap(h);
        }

        heap.meld(heap2);
        assertValidHeap(heap);

        heap.meld(heap3);
        assertValidHeap(heap);

        heap.deleteMin();
        assertValidHeap(heap);

        assertSame(nodes.get(2), heap.findMin());

        BinomialHeap.HeapNode node = heap.last;
        assertSame(nodes.get(2), heap.last);
        node = node.next;
        assertSame(nodes.get(3), node);
        node = node.next;
        assertSame(nodes.get(5), node);
        node = node.next;
        assertSame(nodes.get(9), node);
        node = node.next;
        assertSame(nodes.get(17), node);
    }

    @Test
    @Order(51)
    public void testEmptyMeld() {
        heap.meld(new BinomialHeap());
        assertValidHeap(heap);
        assertTrue(heap.empty());

        BinomialHeap.HeapItem node = testInsertion(heap, 0).get(0);
        heap.meld(new BinomialHeap());
        assertValidHeap(heap);
        assertEquals(1, heap.size());
        assertSame(node, heap.last);
        assertSame(node, heap.findMin());

        BinomialHeap heap2 = heap;
        heap = new BinomialHeap();
        heap.meld(heap2);
        assertEquals(1, heap.size());
        assertSame(node, heap.last);
        assertSame(node, heap.findMin());
    }

    @Test
    @Order(57)
    public void testDeleteMinValue() {
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, Integer.MIN_VALUE, 0);
        testDeletion(heap, nodes.get(0));
        assertSame(nodes.get(Integer.MIN_VALUE), heap.findMin());
        assertSame(nodes.get(Integer.MIN_VALUE), heap.last);
    }

    @Test
    @Order(84)
    public void testDeleteFirst1() {
        // delete first when it has no children
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertion(heap, 10);

        BinomialHeap heap2 = new BinomialHeap();
        nodes.putAll(testInsertionReverse(heap2, 0, 8));

        heap2.deleteMin();
        assertValidHeap(heap2);

        testDeletion(heap2, nodes.get(6), nodes.get(4)); // delete few nodes to make it non binomial

        heap.meld(heap2);
        assertValidHeap(heap);
        assertSame(nodes.get(10), heap.last);
        assertEquals(0, heap.last.rank);

        testDeletion(heap, heap.last.item); // delete last

        assertSame(nodes.get(1), heap.last);
        assertSame(nodes.get(1), heap.findMin());
    }

    @Test
    @Order(85)
    public void testDeleteFirst2() {
        // delete first when it has multiple children
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertionReverse(heap, 0, 8);

        BinomialHeap heap2 = new BinomialHeap();
        nodes.putAll(testInsertion(heap2, 10));

        heap.deleteMin();
        assertValidHeap(heap);

        testDeletion(heap, nodes.get(6), nodes.get(4)); // delete few nodes to make it non binomial

        heap.meld(heap2);
        assertValidHeap(heap);
        assertSame(nodes.get(1), heap.last);
        assertEquals(3, heap.last.rank);

        testDeletion(heap, heap.last.item); // delete last

        assertSame(nodes.get(10), heap.last);
        assertSame(nodes.get(2), heap.last.next);
        assertSame(nodes.get(2), heap.findMin());
    }

    @Tag("NoCompare")
    @Test
    @Order(4900)
    public void testSpecialMarkedChainTree() {
        int depth = 1000000;
        // case 10
        int n = depth * 5; // must divide by 5

        // base
        Map<Integer, BinomialHeap.HeapItem> nodes = testInsertionReverse(heap, n - 5, n - 1);
        heap.deleteMin();
        assertValidHeap(heap);
        testDeletion(heap, nodes.get(n - 1));

        // loop
        Map<Integer, BinomialHeap.HeapItem> currentNodes = null;
        BinomialHeap.HeapItem middle, first, second;
        int i = 1;
        for (;i < Math.min(10, n / 5); i++) { // test the first 10 iterations
            middle = heap.last.child.next.item;
            currentNodes = testInsertionReverse(heap, n - ((i + 1) * 5), n - ((i + 1) * 5) + 4);
            heap.deleteMin();
            assertValidHeap(heap);
            testDeletion(
                heap,
                currentNodes.get(n - ((i + 1) * 5) + 4),
                currentNodes.get(n - ((i + 1) * 5) + 3),
                middle);
        }

        for (; i < n / 5; i++) { // complete all iterations (unchecked)
            middle = heap.last.child.next.item;
            first = heap.insert(n - ((i + 1) * 5) + 4, "info");
            second = heap.insert(n - ((i + 1) * 5) + 3, "info");
            heap.insert(n - ((i + 1) * 5) + 2, "info");
            heap.insert(n - ((i + 1) * 5) + 1, "info");
            heap.insert(n - ((i + 1) * 5), "info");
            heap.deleteMin();

            heap.delete(first);
            heap.delete(second);
            heap.delete(middle);
        }

        assertValidHeap(heap);


        testDeletion(heap, heap.last.child.next.item);
        BinomialHeap.HeapNode node = heap.last;
        while (node != null) {
            assertTrue(node.child != null ? node.rank == 1 : node.rank == 0);
//            assertEquals(node.parent != null, node.getMarked());
            node = node.child;
        }


        /*
             ▄▄▄▄    ▒█████   ▒█████   ███▄ ▄███▓ ▐██▌
            ▓█████▄ ▒██▒  ██▒▒██▒  ██▒▓██▒▀█▀ ██▒ ▐██▌
            ▒██▒ ▄██▒██░  ██▒▒██░  ██▒▓██    ▓██░ ▐██▌
            ▒██░█▀  ▒██   ██░▒██   ██░▒██    ▒██  ▓██▒
            ░▓█  ▀█▓░ ████▓▒░░ ████▓▒░▒██▒   ░██▒ ▒▄▄
            ░▒▓███▀▒░ ▒░▒░▒░ ░ ▒░▒░▒░ ░ ▒░   ░  ░ ░▀▀▒
            ▒░▒   ░   ░ ▒ ▒░   ░ ▒ ▒░ ░  ░      ░ ░  ░
             ░    ░ ░ ░ ░ ▒  ░ ░ ░ ▒  ░      ░       ░
             ░          ░ ░      ░ ░         ░    ░
                  ░
                         __,-~~/~    `---.
                       _/_,---(      ,    )
                   __ /        <    /   )  \___
    - ------===;;;'====------------------===;;;===----- -  -
                      \/  ~"~"~"~"~"~\~"~)~"/
                      (_ (   \  (     >    \)
                       \_( _ <         >_>'
                          ~ `-i' ::>|--"
                              I;|.|.|
                             <|i::|i|`.
                            (` ^'"`-' ")
        */
        heap.decreaseKey(nodes.get(n - 2), n);

        assertValidHeap(heap);

        node = heap.last;
        while (node != null && node != heap.last) {
            assertNull(node.child);
            node = node.next;
        }
    }
}
