import java.util.Arrays;
import java.util.Random;

public class main {
    public static void main(String[] args) throws Exception {

        //		test1(17);
        //		test1(2049);
        test1(1025);
        test1(2049);
        test1(4097);

//        test2(11); // 11
//        test2(21); // 31
//        test2(51); // 71
//        test2(101); // 172
        test2(1001);
        test2(2001);
        test2(3001);

//        specific_test();
//        test_findMin_after_deleteMin();
        
//        System.out.println("finished test_findMin_after_deleteMin");
//        return;
//        BinomialHeapTest.main(args);
//        BinomialHeapTester.main(args);
//        Test.main(args);
//        BinHeapTest.main(args);

        Tester tester = new Tester();
        tester.run();

        System.out.println("Done");
    }

    static void test2(int size) {
        System.out.println("test2 " + (size - 1));
        BinomialHeap heap = new BinomialHeap();
        long startTime = System.nanoTime();
        insert(size, heap, 1);
        SanitizeBinomialHeap.sanitize(heap);
        //		// Make binom
        //		insert(1, heap, 0);
        //		heap.deleteMin();
        //		links = FibonacciHeap.totalLinks();
        //		System.out.println(heap.size());
        //		System.out.println(heap.findMin().getKey());

//        HeapPrinter hp = new HeapPrinter(System.out);
//        hp.print(heap, false);

        for (int i = 0; i < (size-1)/2; i++) {
            heap.deleteMin();
            SanitizeBinomialHeap.sanitize(heap);
//            hp.print(heap, false);
        }

        long endTime = System.nanoTime();
        double timeDiff = (endTime - startTime) / 1000000.0;
        System.out.println("Time: " + timeDiff);
        System.out.println();
    }

    static void test1(int size) {
        System.out.println("test1 " + size);
        BinomialHeap heap = new BinomialHeap();
        long startTime = System.nanoTime();
        BinomialHeap.HeapItem[] nodes = insert(size, heap, 0);
        SanitizeBinomialHeap.sanitize(heap);

        //                HeapPrinter hp = new HeapPrinter(System.out);
        //                hp.print(heap, false);

        heap.deleteMin();
        SanitizeBinomialHeap.sanitize(heap);
        decreaseKey(heap, nodes, size - 1);
        SanitizeBinomialHeap.sanitize(heap);
        long endTime = System.nanoTime();
        double timeDiff = (endTime - startTime) / 1000000.0;
        System.out.println("Time: " + timeDiff);
        System.out.println();
    }

    static BinomialHeap.HeapItem[] insert(int size, BinomialHeap heap, int minimum) {
        BinomialHeap.HeapItem[] nodes = new BinomialHeap.HeapItem[size];
        for (int i = size - 1; i  >= minimum; i--) {
            nodes[i] = heap.insert(i, Integer.toString(i));

            //	                HeapPrinter hp = new HeapPrinter(System.out);
            //	                hp.print(heap, false);
        }

        return nodes;
    }

    static void decreaseKey(BinomialHeap heap, BinomialHeap.HeapItem[] nodes, int m) {
        final int delta = 100;
        int bounds = (int) (Math.log(m)/Math.log(2)) - 2;
        double sum = 0;
        for (int i = 0; i <= bounds; i++) {
            if (i > 0) {
                sum += Math.pow(0.5, i);	
            }

            //			int index = (int)(m * sum + 2);
            //			System.out.println(index);
            heap.decreaseKey(nodes[(int)(m * sum + 2)], delta);
        }

        heap.decreaseKey(nodes[m - 1], delta);
    }
    
    private static int[] createValues(int n) {
        int[] values = new int[n];
        int maxValue = n * 10;
        Random randomGenerator = new Random();

        for (int i = 0; i < n; ++i){
            while (true) {
                int j, randInt = randomGenerator.nextInt(maxValue);

                for (j = 0; j < i && randInt != values[j]; ++j);
                if (j < i) { // already in values 
                    continue;
                }
                values[i] = randInt;
                break;
            }
        }

        return values;
    }
    
    static void specific_test() {
        // tests out a specific tree structure and values
        BinomialHeap heap = new BinomialHeap();
        final int MIN = 0;
        int [] values = {1,2,3,4,5,6,7,8,MIN,11,12,13,9};
        HeapPrinter hp = new HeapPrinter(System.out);

        for (int v : values) {
            heap.insert(v, Integer.toString(v));
        }
        
        if (heap.size() != values.length) {
            System.out.println("error in size. got " + heap.size() + " exptected " + values.length);
        }
        if (heap.findMin().key != MIN) {
            System.out.println("error in min. got " + heap.findMin().key + " exptected " + MIN);
        }
        if (heap.last.item.key != 1 ||
            heap.last.next.item.key != 9 ||
            heap.last.next.next.item.key != 0 ||
            heap.last.child.item.key != 5 ||
            heap.last.child.child == null ||
            heap.last.child.child.item.key != 7 ||
            heap.last.next.child != null ||
            heap.last.next.next.child.item.key != 12 ||
            heap.last.next.next.child.child == null ||
            heap.last.next.next.child.child.item.key != 13
            ) {
            System.out.println("error in structure. got:");
            hp.print(heap, false);
            /*
             * expecting:
             *  | - 9
             *  | 
             *  | - 1 - 2
             *  |       |
             *  |       3 - 4
             *  |       |
             *  |       5 - 6
             *  |           |
             *  |           7 - 8
             *  |
             *  | - 0 - 11
             *  |       |
             *  |       12 - 13
             */
        }
        SanitizeBinomialHeap.sanitize(heap);
        heap.deleteMin();
        SanitizeBinomialHeap.sanitize(heap);
        
        if (heap.size() != values.length - 1) {
            System.out.println("error in size. got " + heap.size() + " exptected " + (values.length - 1));
        }
        if (heap.findMin().key != 1) {
            System.out.println("error in min. got " + heap.findMin().key + " exptected " + 1);
        }
        if (heap.last.item.key != 1 ||
            heap.last.next.item.key != 9 ||
            heap.last.next.next != heap.last || // back to root
            heap.last.child.item.key != 5 ||
            heap.last.child.child == null ||
            heap.last.child.child.item.key != 7 ||
            heap.last.next.child == null ||
            heap.last.next.child.item.key != 12 ||
            heap.last.next.child.child == null ||
            heap.last.next.child.child.item.key != 13 ||
            heap.last.next.child.next.item.key != 11
            ) {
            System.out.println("error in structure. got:");
            hp.print(heap, false);
            /*
             * expecting:
             *  | - 1 - 2
             *  |       |
             *  |       3 - 4
             *  |       |
             *  |       5 - 6
             *  |           |
             *  |           7 - 8
             *  |
             *  | - 9 - 11
             *  |       |
             *  |       12 - 13
             */
        }
    }
    
    static void test_findMin_after_deleteMin() {
        int[] vals = createValues(20);
        BinomialHeap heap1 = new BinomialHeap();
        
        HeapPrinter hp = new HeapPrinter(System.out);

        for (int v : vals) {
            heap1.insert(v, Integer.toString(v));
            SanitizeBinomialHeap.sanitize(heap1);
        }
        Arrays.sort(vals);
        for (int v : vals) {
            if (heap1.findMin().key != v) {
                System.out.println("min is "+v+" but findMin() says "+
                        heap1.findMin());
                break;
            }
//            System.out.println("\ndeleting " + v + " from:");
//            hp.print(heap1, false);
            heap1.deleteMin();
//            System.out.println("result:");
//            hp.print(heap1, false);
            SanitizeBinomialHeap.sanitize(heap1);
        }
    }
    
    static void test_check_size_after_insert_and_delete() {
        HeapPrinter hp = new HeapPrinter(System.out);
        BinomialHeap heap1 = new BinomialHeap();
        int size = 0;

        for (int i = 10; i < 30; ++i) {
            if (!heap1.empty()) {
                System.out.println("0. empty but empty() returns false" + " | i=" + i);
                break;
            }
            if (heap1.size() != size) {
                System.out.println("0. size is "+size+
                        " but size() returns "+
                        heap1.size() + " | i=" + i);
                break;
            }
            for (int j = 0; j < i; ++j) {
                heap1.insert(j, Integer.toString(j));
                ++size;
                if (heap1.empty()) {
                    System.out.println(
                            "1. not empty but empty() returns true" + " | i=" + i + ", j=" + j);
                    break;
                }
                if (heap1.size() != size) {
                    System.out.println("1. size is "+size+
                            " but size() returns "+
                            heap1.size() + " | i=" + i + ", j=" + j);
                    break;
                }
            }
            for (int j = 0; j < i; ++j) {
                if (heap1.empty()) {
                    System.out.println(
                            "2. not empty but empty() returns true" + " | i=" + i + ", j=" + j);
                    break;
                }
//                System.out.println("\ndeleting " + heap1.findMin().key);
//                System.out.println("before:");
//                hp.print(heap1, false);
                heap1.deleteMin();
//                System.out.println("after:");
//                hp.print(heap1, false);
                --size;
                if (heap1.size() != size) {
                    System.out.println("2. size is "+size+
                            " but size() returns "+
                            heap1.size() + " | i=" + i + ", j=" + j);
                    break;
                }
            }
            if (!heap1.empty()) {
                System.out.println("3. empty but empty() returns false" + " | i=" + i);
                break;
            }
        }
    }
}
