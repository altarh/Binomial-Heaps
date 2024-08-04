//BinomialHeap Tester

import java.util.ArrayList;
import java.util.Collections;

public class Test {

    static BinomialHeap.HeapNode heap;
    static BinomialHeap binomialHeap;
    static double grade;
    static double testScore;

    public static void main(String[] args) {

        grade = 100.0;
        testScore = grade / 29;
        System.out.println("trying test0...");
        try {test0();} catch (Exception e){bugFound("test0", e);}
        System.out.println("trying test1...");
        try {test1();} catch (Exception e){bugFound("test1", e);}
        System.out.println("trying test2...");
        try {test2();} catch (Exception e){bugFound("test2", e);}
        System.out.println("trying test3...");
        try {test3();} catch (Exception e){bugFound("test3", e);}
        System.out.println("trying test4...");
        try {test4();} catch (Exception e){bugFound("test4", e);}
        System.out.println("trying test5...");
        try {test5();} catch (Exception e){bugFound("test5", e);}
        System.out.println("trying test6...");
        try {test6();} catch (Exception e){bugFound("test6", e);}
        System.out.println("trying test7...");
        try {test7();} catch (Exception e){bugFound("test7", e);}
        System.out.println("trying test8...");
        try {test8();} catch (Exception e){bugFound("test8", e);}
        System.out.println("trying test9...");
        try {test9();} catch (Exception e){bugFound("test9", e);}
        System.out.println("trying test10...");
        try {test10();} catch (Exception e){bugFound("test10", e);}
        System.out.println("trying test11...");
        try {test11();} catch (Exception e){bugFound("test11", e);}
        System.out.println("trying test12...");
        try {test12();} catch (Exception e){bugFound("test12", e);}
        System.out.println("trying test13...");
        try {test13();} catch (Exception e){bugFound("test13", e);}
        System.out.println("trying test14...");
        try {test14();} catch (Exception e){bugFound("test14", e);}
        System.out.println("trying test15...");
        try {test15();} catch (Exception e){bugFound("test15", e);}
        System.out.println("trying test26...");
        try {test26();} catch (Exception e){bugFound("test26", e);}

        System.out.println(grade);
        System.out.println("Test done");
    }

    static void test0() {
        String test = "test0";
        binomialHeap = new BinomialHeap();

        ArrayList<Integer> numbers = new ArrayList<>();

//        int x = 99999;
        int x = 9;
        for (int i = 0; i < x; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        for (int i = 0; i < x; i++) {
            binomialHeap.insert(numbers.get(i), Integer.toString(i));
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        for (int i = 0; i < x; i++) {
            if (binomialHeap.findMin().key != i) {
            	System.out.println("min should be:" + i);
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
    }

    static void test1() {
        String test = "test1";
        heap = new BinomialHeap.HeapNode();
        binomialHeap = new BinomialHeap();
        addKeys(0);
        HeapPrinter printer = new HeapPrinter(System.out);
        while (!heap.empty()) {
            // System.out.println(heap.size() + " " + binomialHeap.size());
            // printer.print(binomialHeap, false);
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                System.out.println(heap.findMin() + " " + binomialHeap.findMin().key);
                System.out.println(heap.size() + " " + binomialHeap.size());
                printer.print(binomialHeap, false);
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test2() {
        String test = "test2";
        heap = new Heap();
        binomialHeap = new BinomialHeap();
        addKeysReverse(0);
        while (!heap.empty()) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test3() {
        String test = "test3";
        heap = new Heap();
        binomialHeap = new BinomialHeap();
        addKeys(0);
        addKeysReverse(4000);
        addKeys(2000);
        while (!heap.empty()) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
		if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test4() {
        String test = "test4";
        heap = new Heap();
        binomialHeap = new BinomialHeap();
        addKeys(0);
        addKeysReverse(4000);
        addKeys(2000);

        for (int i = 0; i < 1000; i++) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        addKeys(6000);
        addKeysReverse(8000);
        addKeys(10000);
        SanitizeBinomialHeap.sanitize(binomialHeap);

        while (!heap.empty()) {
            if (heap.findMin() != binomialHeap.findMin().key) {
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test5() {
        String test = "test5";
        binomialHeap = new BinomialHeap();
        addKeys(0);
        addKeys(0);
        addKeys(0);

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != binomialHeap.findMin().key) {
                    bugFound(test);
                    return;
                }
                binomialHeap.deleteMin();
                SanitizeBinomialHeap.sanitize(binomialHeap);
            }
        }
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test6() {
        String test = "test6";
        binomialHeap = new BinomialHeap();
        addKeysReverse(1000);
        addKeysReverse(1000);
        addKeys(0);
        addKeys(0);
        addKeys(1000);
        addKeys(1000);
        addKeysReverse(0);
        addKeysReverse(0);
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 0; i < 2000; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != binomialHeap.findMin().key) {
                    bugFound(test);
                    return;
                }
                binomialHeap.deleteMin();
            }
        }
        SanitizeBinomialHeap.sanitize(binomialHeap);
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test7() {
        String test = "test7";
        heap = new Heap();
        binomialHeap = new BinomialHeap();
        addKeys(1000);
        addKeysReverse(3000);

        ArrayList<BinomialHeap.HeapItem> nodes = new ArrayList<>();

        for (int i = 2000; i < 3000; i++) {
            heap.insert(i);
            nodes.add(binomialHeap.insert(i, Integer.toString(i)));
        }

        for (int i = 2000; i < 2500; i++) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.delete(i);
            binomialHeap.delete(nodes.get(i - 2000));
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        while (!heap.empty()) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                System.out.println(2);
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (!binomialHeap.empty()) {
            System.out.println(3);
            bugFound(test);
        }
    }

    static void test8() {
        String test = "test8";
        heap = new Heap();
        binomialHeap = new BinomialHeap();
        addKeys(7000);
        addKeysReverse(9000);

        ArrayList<BinomialHeap.HeapItem> nodes = new ArrayList<>();

        for (int i = 2000; i < 3000; i++) {
            heap.insert(i);
            nodes.add(binomialHeap.insert(i, Integer.toString(i)));
        }
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 2000; i < 2500; i++) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.delete(i);
            binomialHeap.delete(nodes.get(i - 2000));
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        while (!heap.empty()) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
        }
        SanitizeBinomialHeap.sanitize(binomialHeap);
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test9() {
        String test = "test9";
        heap = new Heap();
        binomialHeap = new BinomialHeap();
        addKeys(7000);
        addKeysReverse(9000);

        ArrayList<BinomialHeap.HeapItem> nodes = new ArrayList<>();

        for (int i = 2000; i < 3000; i++) {
            heap.insert(i);
            nodes.add(binomialHeap.insert(i, Integer.toString(i)));
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        for (int i = 2700; i > 2200; i--) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.delete(i);
            binomialHeap.delete(nodes.get(i - 2000));
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        while (!heap.empty()) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
        }
        SanitizeBinomialHeap.sanitize(binomialHeap);
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test10() {
        String test = "test10";
        heap = new Heap();
        binomialHeap = new BinomialHeap();
        addKeys(7000);
        addKeysReverse(9000);

        ArrayList<BinomialHeap.HeapItem> nodes = new ArrayList<>();

        for (int i = 2000; i < 3000; i++) {
            heap.insert(i);
            nodes.add(binomialHeap.insert(i, Integer.toString(i)));
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        heap.deleteMin();
        binomialHeap.deleteMin();
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 2700; i > 2200; i--) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {
                bugFound(test);
                return;
            }
            heap.delete(i);
            binomialHeap.delete(nodes.get(i - 2000));
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        while (!heap.empty()) {
            if (heap.findMin() != binomialHeap.findMin().key || heap.size() != binomialHeap.size()) {

                bugFound(test);
                return;
            }
            heap.deleteMin();
            binomialHeap.deleteMin();
        }
        SanitizeBinomialHeap.sanitize(binomialHeap);
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test11() {
        String test = "test11";
        binomialHeap = new BinomialHeap();
        int key = 9999;
        addKeys(1000);
        BinomialHeap.HeapItem h = binomialHeap.insert(key, Integer.toString(key));
        binomialHeap.decreaseKey(h, 9999);
        SanitizeBinomialHeap.sanitize(binomialHeap);

        if (0 != binomialHeap.findMin().key) {
            bugFound(test);
            return;
        }

        binomialHeap.deleteMin();
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 1000; i < 2000; i++) {
                if (i != binomialHeap.findMin().key) {
                    bugFound(test);
                    return;
                }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test12() {
        String test = "test12";
        binomialHeap = new BinomialHeap();
        int key = 5000;
        addKeys(1000);
        BinomialHeap.HeapItem h = binomialHeap.insert(key, Integer.toString(key));
        binomialHeap.decreaseKey(h, 4000);
        SanitizeBinomialHeap.sanitize(binomialHeap);


        for (int i = 0; i < 2; i ++) {

            if (1000 != binomialHeap.findMin().key) {
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        for (int i = 1001; i < 2000; i++) {
            if (i != binomialHeap.findMin().key) {
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test13() {
        String test = "test13";
        binomialHeap = new BinomialHeap();
        int key = 9000;
        addKeys(1000);
        BinomialHeap.HeapItem h = binomialHeap.insert(key, Integer.toString(key));
        binomialHeap.decreaseKey(h, 4000);
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 1000; i < 2000; i++) {
            if (i != binomialHeap.findMin().key) {
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (5000 != binomialHeap.findMin().key) {
            bugFound(test);
            return;
        }
        binomialHeap.deleteMin();
        SanitizeBinomialHeap.sanitize(binomialHeap);

        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test14() {
        String test = "test14";
        binomialHeap = new BinomialHeap();
        int key = 9000;
        addKeys(1000);
        addKeysReverse(7000);
        BinomialHeap.HeapItem h = binomialHeap.insert(key, Integer.toString(key));
        binomialHeap.decreaseKey(h, 4000);
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 1000; i < 2000; i++) {
            if (i != binomialHeap.findMin().key) {
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
        if (5000 != binomialHeap.findMin().key) {
            bugFound(test);
            return;
        }
        binomialHeap.deleteMin();
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 7000; i < 8000; i++) {
            if (i != binomialHeap.findMin().key) {
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }

        if (!binomialHeap.empty())
            bugFound(test);
    }


    static void test15() {
        String test = "test15";
        binomialHeap = new BinomialHeap();
        int key = 99999;

        for (int i = 1000; i < 10000; i += 1000) {
            addKeys(i);
        }

        binomialHeap.deleteMin();
        SanitizeBinomialHeap.sanitize(binomialHeap);

        BinomialHeap.HeapItem h = binomialHeap.insert(key, Integer.toString(key));
        binomialHeap.decreaseKey(h, 99999);
        SanitizeBinomialHeap.sanitize(binomialHeap);

        if (0 != binomialHeap.findMin().key) {
            bugFound(test);
            return;
        }

        binomialHeap.deleteMin();
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 1001; i < 10000; i++) {
            if (i != binomialHeap.findMin().key) {
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
        }
        SanitizeBinomialHeap.sanitize(binomialHeap);
        if (!binomialHeap.empty())
            bugFound(test);
    }

    static void test26() {
        String test = "test26";
        binomialHeap = new BinomialHeap();

        int size = 1000;

        for (int i = size; i > 0; i--) {
            binomialHeap.insert(i, Integer.toString(i));
        }
        SanitizeBinomialHeap.sanitize(binomialHeap);

        for (int i = 0; i < size / 2; i++) {
            if (binomialHeap.findMin().key != i + 1) {
                bugFound(test);
                return;
            }
            binomialHeap.deleteMin();
            SanitizeBinomialHeap.sanitize(binomialHeap);
        }
    }

    static void bugFound (String test) {
        System.out.println("Bug found in " + test);
        grade -= testScore;
    }
    
    static void bugFound (String test, Exception e) {
        System.out.println("Bug found in " + test);
        System.out.println(e.getMessage());
        grade -= testScore;
    }

   static void addKeys(int start) {
        for (int i = 0; i < 1000; i++) {
            heap.insert(start + i);
            binomialHeap.insert(start + i, Integer.toString(start + i));
        }
    }

    static void addKeysReverse(int start) {
        for (int i = 999; i >= 0; i--) {
            heap.insert(start + i);
            binomialHeap.insert(start + i, Integer.toString(start + i));
        }
    }
}

