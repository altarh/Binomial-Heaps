import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinHeapTest {

    static class DecreasedKey {
        private int key;
        private int delta;

        public DecreasedKey(int key, int delta) {
            this.key = key;
            this.delta = delta;
        }

        public int getKey() {
            return key;
        }

        public int getDelta() {
            return delta;
        }

        @Override
        public String toString() {
            return "DecreasedKey{" +
                    "key=" + key +
                    ", delta=" + delta +
                    '}';
        }
    }

    static class DeleteException extends Exception {
        int deleteKey;

        public DeleteException(int deleteKey, String s) {
            super(s);
            this.deleteKey = deleteKey;
        }

        public int getDeleteKey() {
            return deleteKey;
        }
    }

    static class DecreasedKeyException extends Exception {
        private List<DecreasedKey> keys;

        public DecreasedKeyException(List<DecreasedKey> keys, String msg, Throwable ex) {
            super(msg, ex);
            this.keys = keys;
        }

        public List<DecreasedKey> getKeys() {
            return keys;
        }
    }

    public static class CreateHeapResult {
        private List<Integer> insertOrder;
        private BinomialHeap heap;

        public CreateHeapResult(List<Integer> insertOrder, BinomialHeap heap) {
            this.insertOrder = insertOrder;
            this.heap = heap;
        }

        public List<Integer> getInsertOrder() {
            return insertOrder;
        }

        public BinomialHeap getHeap() {
            return heap;
        }
    }

    public static class ExpectedFields {
        private int size;
        private Integer numOfTrees;
        private Integer minKeyVal;

        public ExpectedFields(int size, Integer numOfTrees, Integer minKeyVal) {
            this.size = size;
            this.numOfTrees = numOfTrees;
            this.minKeyVal = minKeyVal;
        }

        public int getSize() {
            return size;
        }

        public int getNumOfTrees() {
            return numOfTrees;
        }

        public int getMinKeyVal() {
            return minKeyVal;
        }
    }

    public static Logger logger = Logger.getLogger("Test");


    public static CreateHeapResult createHeap(int from, int to) {
        return createHeap(from, to, false);
    }

    public static CreateHeapResult createHeap(int from, int to, boolean testAfterInsert) {
        List<Integer> l = IntStream.rangeClosed(from, to).boxed().collect(Collectors.toList());
        Collections.shuffle(l);
        BinomialHeap heap = new BinomialHeap();
        for (Integer n : l) {
            //logger.info("inserting " + n);
            heap.insert(n, n.toString());
            if (testAfterInsert) testFibHeap(heap);
        }
        return new CreateHeapResult(l, heap);
    }

    public static int expectedNumOfTrees(int size) {
        return (int) Integer.toBinaryString(size).chars().filter(ch -> ch == '1').count();
    }


    public static Map<Integer, BinomialHeap.HeapItem> testInserts(BinomialHeap heap, List<Integer> keys) {
        int size = heap.size();
        double minKey = Double.POSITIVE_INFINITY;
        Map<Integer, BinomialHeap.HeapItem> nodes = new HashMap<>();
        int i = 0;
        for (Integer n : keys) {
            if (n < minKey) {
                minKey = n;
            }
            nodes.put(n, heap.insert(n, n.toString()));
            testFibHeap(heap);
            size++;
            testHeapFields(
                    heap, new ExpectedFields(
                            size, null, (int) minKey
                    )
            );
        }
        return nodes;
    }

    public static void testDeletes(BinomialHeap heap, List<Integer> orderedKeys) throws DeleteException {
        int size = heap.size();
        Integer expectedSize;
        for (Integer minVal : orderedKeys) {
            if (heap.findMin().key != minVal) {
                throw new RuntimeException(String.format(
                        "wrong minimum value found. should have been %d but was %d", minVal, heap.findMin().key)
                );
            }
            expectedSize = size == heap.size() ? null : expectedNumOfTrees(size);
            testHeapFields(heap, new ExpectedFields(size, expectedSize, minVal));
            try {
                heap.deleteMin();
            } catch (Exception ex) {
                throw new DeleteException(heap.findMin().key, "error deleting key " + heap.findMin());
            }
            size--;
        }
    }

    public static int[] dumbKMin(BinomialHeap heap, int k) {
        int vals[] = new int[k];
        for (int i = 0; i < k; i++) {
            vals[i] = heap.findMin().key;
            heap.deleteMin();
        }
        return vals;
    }

    public static void testDecreaseKey(BinomialHeap heap, Map<Integer, BinomialHeap.HeapItem> nodes, int amount) throws DecreasedKeyException {
        int randIndex, delta;
        BinomialHeap.HeapItem[] nodesArr = new BinomialHeap.HeapItem[nodes.size()];
        List<DecreasedKey> decreasedKeys = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Integer, BinomialHeap.HeapItem> entry : nodes.entrySet()) {
            nodesArr[i++] = entry.getValue();
        }
        Random random = new Random(0L);
        int newKey, oldKey;
        try {
            for (i = 0; i < amount; i++) {
                do {
                    randIndex = random.nextInt(nodesArr.length);
                    delta = Math.max(1, random.nextInt(nodesArr.length));
                    oldKey = nodesArr[randIndex].key;
                    newKey = oldKey - delta;
                } while (nodes.containsKey(newKey));
                int old = nodes.get(oldKey).key;
                decreasedKeys.add(new DecreasedKey(oldKey, delta));
                heap.decreaseKey(nodes.get(oldKey), delta);
                nodes.put(newKey, nodes.remove(oldKey));
                testFibHeap(heap);
            }
        } catch (Exception ex) {
            throw new DecreasedKeyException(decreasedKeys, "Error during decreasedKey", ex);
        }
    }

    public static void testMeld(BinomialHeap heap, int upperBound) {
        CreateHeapResult res = createHeap(upperBound, upperBound + 20, false);
        BinomialHeap heap2 = res.getHeap();
        int size = heap.size();
        int othersize = heap2.size();
        int numOfTrees = heap.numTrees();
        int other_numOfTrees = heap2.numTrees();
        
        heap.meld(heap2);
        if (heap.size() != size + othersize) {
            throw new RuntimeException("size after meld is not sum of both heaps");
        }
        testFibHeap(heap);
    }

//    public static void testKMin(BinomialHeap heap) {
//        if (heap.size() == 0) {
//            int[] kMinVals = BinomialHeap.kMin(heap, 0);
//            if (kMinVals.length != 0) {
//                throw new RuntimeException("kMin did not return empty array for empty heap");
//            }
//        } else {
//            BinomialHeap.HeapItem cur = heap.getFirst();
//            BinomialHeap.HeapItem next;
//            Random rand = new Random(0L);
//            int kMin;
//            int[] kMinVals, dumbKMinVals;
//            BinomialHeap heaps[] = new BinomialHeap[heap.numTrees()];
//            int heapIndex = 0;
//            do {
//                next = cur.next;
//                cur.setPrev(cur);
//                cur.setNext(cur);
//                heaps[heapIndex] = new BinomialHeap(cur, countTreeItems(cur));
//                heapIndex++;
//                cur = next;
//            } while (next != null && next != heap.getFirst());
//            for (BinomialHeap fHeap : heaps) {
//                if (fHeap.size() > 0) {
//                    kMin = rand.nextInt(fHeap.size());
//                    kMinVals = BinomialHeap.kMin(fHeap, kMin);
//                    dumbKMinVals = dumbKMin(fHeap, kMin);
//                    if (kMinVals.length != dumbKMinVals.length) {
//                        throw new RuntimeException("kMin has wrong amount of values");
//                    } else {
//                        for (int i = 0; i < kMinVals.length; i++) {
//                            if (kMinVals[i] != dumbKMinVals[i]) {
//                                throw new RuntimeException("kMin returns wrong values");
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    public static void testRootPointers(BinomialHeap heap) {
        BinomialHeap.HeapNode cur = heap.last;
        do {
            if (cur == null) {
                throw new RuntimeException("we have a tree with root null");
            }
//            if (cur.next == cur)
//                throw new RuntimeException("next set to null");
            if (cur.parent != null) {
                throw new RuntimeException(String.format("tree root %d has parent", cur.item.key));
            }
            cur = cur.next;
        } while (cur != heap.last);
    }

    public static void testRanks(BinomialHeap.HeapNode rootNode) {
        if (rootNode == null) return;
        int rank = rootNode.rank;
        BinomialHeap.HeapNode child = rootNode.child;
        BinomialHeap.HeapNode curNode = child;
        int childrenAmount = 0;
        if (rank == 0) {
            if (child != null) {
                throw new RuntimeException("rank is zero but node " + rootNode.item.key + " has child " + child.item.key);
            }
        } else {
            do {
                childrenAmount++;
                if (curNode.child != null) testRanks(curNode.child);
                curNode = curNode.next;
            } while (curNode != child);
            if (rank != childrenAmount) {
                throw new RuntimeException(String.format(
                        "node %d has rank %d but found %d children",
                        rootNode.item.key, rank, childrenAmount)
                );
            }
        }
    }

    public static void testFibMin(BinomialHeap heap) {
        BinomialHeap.HeapNode cur = heap.last;
        do {
            if (cur.item.key < heap.findMin().key) {
                throw new RuntimeException(String.format("minimum must be %s but is %s", heap.findMin().key, cur.item.key));
            }
            cur = cur.next;
        } while (cur != heap.last);
    }

    public static int calculateNumOfTrees(BinomialHeap heap) {
        int i = heap.size() == 0 ? 0 : 1;
        BinomialHeap.HeapNode cur = heap.last.next;
        while (cur != heap.last) {
            i++;
            cur = cur.next;
        }
        return i;
    }


    public static void testNumOfTrees(BinomialHeap heap) {
        int actualNumOfTrees = calculateNumOfTrees(heap);
        if (actualNumOfTrees != heap.numTrees()) {
            throw new RuntimeException(String.format("should have %s trees but counted %s", heap.numTrees(), actualNumOfTrees));
        }
    }

    public static void testFibHeap(BinomialHeap heap) {
        BinomialHeap.HeapNode last = heap.last;
        BinomialHeap.HeapNode cur = last;
        testRootPointers(heap);
        testNumOfTrees(heap);
        do {
            testHeapMinProp(cur);
            testChildren(cur);
//            testSiblings(cur);
            testRanks(cur);
            cur = cur.next;
        } while (cur != last);
        testFibMin(heap); // Minimum must be one of the roots otherwise testHeapMinProp would have failed
        SanitizeBinomialHeap.sanitize(heap);
    }

    /**
     * Checks only siblings for root's first child
     */
    public static void testSiblings(BinomialHeap.HeapNode root) {
        if (root.child != null) {
            BinomialHeap.HeapNode child = root.child;
            BinomialHeap.HeapNode cur = child;
            do {
                if (cur.next == null)
                    throw new RuntimeException("next returned null");
                if (cur.parent != null)
                    throw new RuntimeException("root node with parent " + cur.parent.item.key);
                cur = cur.next;
            } while (cur != child);
        }
    }

    public static void testChildren(BinomialHeap.HeapNode node) {
        if (node.child != null) {
            if (node.child.parent != node)
                throw new RuntimeException("getChild.getParent did not return cur node: " + node.item.key);
            BinomialHeap.HeapNode child = node.child;
            testChildren(child);
            BinomialHeap.HeapNode cur = child;
            do {
                if (cur.parent != node)
                    throw new RuntimeException("getParent of a sibling did not return current node: " + node.item.key);
                cur = cur.next;
            } while (cur != child);
        }
    }

    public static void testHeapMinProp(BinomialHeap.HeapNode node) {
        if (node.child != null) {
            BinomialHeap.HeapNode firstChild = node.child;
            BinomialHeap.HeapNode curChild = firstChild;
            do {
                if (curChild.item.key < node.item.key)
                    throw new RuntimeException("child found with smaller key than parent: " + node.item.key);
                testHeapMinProp(curChild);
                curChild = curChild.next;
            } while (curChild != firstChild);
        }
    }

    private static int countTreeItemsRec(BinomialHeap.HeapNode child) {
        if (child == null) return 0;
        int cnt = 0;
        BinomialHeap.HeapNode cur = child;
        do {
            cnt++;
            if (cur.child != null) cnt += countTreeItemsRec(cur.child);
            cur = cur.next;
        } while (cur != child);
        return cnt;

    }

    public static int countTreeItems(BinomialHeap.HeapNode root) {
        return root == null ? 0 : 1 + countTreeItemsRec(root.child);

    }

    public static void testHeapFields(BinomialHeap heap, ExpectedFields expected) {
        if (expected.numOfTrees != null && expected.numOfTrees != heap.numTrees()) {
            throw new RuntimeException(
                    String.format("wrong amount of trees. found %d but expected %d",
                            heap.numTrees(), expected.numOfTrees)
            );
        }
        if (expected.size != heap.size()) {
            throw new RuntimeException(
                    String.format("wrong size of tree. found %d but expected %d",
                            heap.size(), expected.size)
            );
        }
        if (expected.minKeyVal == null) {
            if (heap.findMin() != null) {
                throw new RuntimeException(
                        String.format("wrong minimum found. found %d but expected null", heap.findMin().key)
                );
            }
        } else {
            if (expected.minKeyVal != heap.findMin().key) {
                throw new RuntimeException(
                        String.format("wrong min key in tree. found %d but expected %d",
                                heap.findMin().key, expected.minKeyVal)
                );
            }
        }
    }

    public static void testRandomHeap(int size, int jumps) throws Exception {
        List<Integer> keys = IntStream.rangeClosed(0, size).boxed().map(x -> x * jumps).collect(Collectors.toList());

        Collections.shuffle(keys);
        List<Integer> orderedKeys = new ArrayList<>(keys);
        try {
            Collections.sort(orderedKeys);
            BinomialHeap heap = new BinomialHeap();
            System.out.println("-testing inserts");
            testInserts(heap, keys);
            System.out.println("-testing deletes");
            testDeletes(heap, orderedKeys);
            System.out.println("-testing kMin");
            testInserts(heap, keys);  // Repopulate after delete
            heap.deleteMin();  // Make valid heap structure
//            testKMin(heap);
            // After kMin, tree is destroyed, reset
            heap = new BinomialHeap();
            testInserts(heap, keys);
            System.out.println("-testing meld");
            testMeld(heap, size + 1);
            System.out.println("-testing decreaseKey");
            heap = new BinomialHeap();
            Map<Integer, BinomialHeap.HeapItem> nodes = testInserts(heap, keys);
            nodes.remove(heap.findMin().key);
            heap.deleteMin();
            testDecreaseKey(heap, nodes, nodes.size() / 2);


        } catch (DeleteException ex) {
            System.out.println("inserted: " + keys.toString());
            System.out.println("failed deleting minimum when min = " + ex.deleteKey);
            throw ex;
        } catch (DecreasedKeyException ex) {
            System.out.println("inserted: " + keys.toString());
            System.out.println("order of decreased keys: " + ex.getKeys().toString());
            throw ex;
        } catch (Exception ex) {
            System.out.println("inserted: " + keys.toString());
            throw ex;
        }
    }

    public static void testTrees() throws Exception {

        System.out.println("testing tree of size 0");
        CreateHeapResult res = createHeap(0, -1);
        testHeapFields(res.getHeap(), new ExpectedFields(0, 0, null));
//        testKMin(res.getHeap());
        System.out.println("testing tree of size 1");
        res = createHeap(1, 1);
        testHeapFields(res.getHeap(), new ExpectedFields(1, 1, 1));
//        testKMin(res.getHeap());
        int delta = 5;
        res.getHeap().decreaseKey(res.getHeap().findMin(), delta);
        testHeapFields(res.getHeap(), new ExpectedFields(1, 1, 1 - delta));
        res.getHeap().delete(res.getHeap().findMin());
        testHeapFields(res.getHeap(), new ExpectedFields(0, 0, null));

        Random rand = new Random(0L);
        int jumps;
        for (int i = 0; i < 10; i++) {
            int size = rand.nextInt(10000);
            jumps = rand.nextInt(10) + 3;
            System.out.println("test " + (i+1) + " of 10 - testing tree of size " + size);
            testRandomHeap(size, jumps);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("I <3 male Guy");
        testTrees();
        System.out.println("Dankovich");
    }
}
