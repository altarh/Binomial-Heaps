class BinomialHeapTester {

	
	public static void testIsEmpty() {
		BinomialHeap heap = new BinomialHeap();
		assertTrue(heap.empty());
		heap.insert(2, "info");
		SanitizeBinomialHeap.sanitize(heap);
		assertFalse(heap.empty());
	}
	
	
	public static void testInsert() {
		BinomialHeap heap = new BinomialHeap();
		heap.insert(2, "info");
		BinomialHeap.HeapItem node = heap.insert(1, "info");
		assertEquals(heap.size(), 2);
		assertEquals(heap.findMin(), node);
		SanitizeBinomialHeap.sanitize(heap);
	}
	
	
	public static void testDeleteMin() {
		BinomialHeap heap = new BinomialHeap();
		BinomialHeap.HeapItem node = heap.insert(2, "info");
		heap.insert(1, "info");
		heap.insert(3, "info");
		heap.deleteMin();
		assertEquals(heap.size(), 2);
		assertEquals(heap.findMin(), node);
		SanitizeBinomialHeap.sanitize(heap);
	}
	
	
	public static void testFindMin() {
		BinomialHeap heap = new BinomialHeap();
		BinomialHeap.HeapItem node = heap.insert(1, "info");
		heap.insert(2, "info");
		heap.insert(3, "info");
		heap.insert(4, "info");
		assertEquals(heap.size(), 4);
		assertEquals(heap.findMin(), node);	
		SanitizeBinomialHeap.sanitize(heap);
	}
	
	
	public static void testMeld() {
		BinomialHeap heap1 = new BinomialHeap();
		heap1.insert(6, "info");
		heap1.insert(2, "info");
		heap1.insert(3, "info");
		heap1.insert(4, "info");
		heap1.insert(5, "info");
		
		BinomialHeap heap2 = new BinomialHeap();
		heap2.insert(7, "info");
		heap2.insert(8, "info");
		heap2.insert(9, "info");
		heap2.insert(10, "info");
		BinomialHeap.HeapItem min = heap2.insert(1, "info");
		
		heap1.meld(heap2);
		assertEquals(heap1.size(), 10);
		assertEquals(heap1.findMin(), min);
		SanitizeBinomialHeap.sanitize(heap1);
	}
	
	
	public static void testSize() {
		BinomialHeap heap = new BinomialHeap();
		heap.insert(1, "info");
		heap.insert(2, "info");
		heap.insert(3, "info");
		heap.deleteMin();
		heap.deleteMin();
		assertEquals(heap.size(), 1);
		SanitizeBinomialHeap.sanitize(heap);
	}
	
	
	public static void testDelete() {
		BinomialHeap heap = new BinomialHeap();
		heap.insert(1, "info");
		BinomialHeap.HeapItem node = heap.insert(2, "info");
		heap.insert(3, "info");
		heap.delete(node);
		assertEquals(heap.size(), 2);
		SanitizeBinomialHeap.sanitize(heap);
	}
	
	
	public static void testDecreaseKey() {
		BinomialHeap heap = new BinomialHeap();
		int size = 5;
		BinomialHeap.HeapItem node = null;
		for (int i = 0; i < size; i++) {
			 node = heap.insert(i, "info");
		}
		
		SanitizeBinomialHeap.sanitize(heap);
		heap.deleteMin();
		SanitizeBinomialHeap.sanitize(heap);
		size -= 1;
		heap.decreaseKey(node, 6);
		assertEquals(heap.size(), size);
		assertEquals(heap.findMin(), node);
		SanitizeBinomialHeap.sanitize(heap);
	}
	
	private static void assertEqualArrays(int[] array1, int[] array2) {
		assertEquals(array1.length, array2.length);
		for (int i = 0; i < array1.length; i++) {
			assertEquals(array1[i], array2[i]);
		}		
	}
	
	private static void assertEquals(int a, int b) {
	    if (a != b)
	        throw new RuntimeException("assertEquals failed for " + Integer.toString(a) + " != " + Integer.toString(b));
	}
        
        private static void assertEquals(BinomialHeap.HeapItem a, BinomialHeap.HeapItem b) {
            if (a != b)
                throw new RuntimeException("assertEquals failed for " + Integer.toString(a.key) + " != " + Integer.toString(b.key));
        }
        
        private static void assertTrue(boolean b) {
            if (!b)
                throw new RuntimeException("assertTrue failed"); 
        }
        
        private static void assertFalse(boolean b) {
            if (b)
                throw new RuntimeException("assertFalse failed"); 
        }
        
	public static void main(String[] args) {
	    testIsEmpty();
	    testInsert();
	    testDeleteMin();
	    testFindMin();
	    testMeld();
	    testSize();
	    testDelete();
	    testDecreaseKey();
	    System.out.println("BinomialHeapTester done");
	}
}
