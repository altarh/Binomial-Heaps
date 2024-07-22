/**
 * BinomialHeap
 *
 * An implementation of binomial heap over positive integers.
 *
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public BinomialHeap() {
		HeapNode fake = new HeapNode()
		this.last = hea
	}
	
	
	public HeapItem insert(int key, String info) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		return; // should be replaced by student code

	}

	/**
	 * 
	 * Return the minimal HeapItem, null if empty.
	 *
	 */
	public HeapItem findMin()
	{
		return null; // should be replaced by student code
	} 

	/**
	 * 
	 * pre: 0<diff<item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		BinomialHeap newBinHeap = new BinomialHeap();
		HeapNode counter1 = last.next;
		HeapNode counter2 = heap2.last.next;
		HeapNode temp = new HeapNode();
		
		
		if (counter1.rank == counter2.rank) {
			temp = link(counter1, counter2); //TODO: add link
			counter1 = counter1.next;
			counter2 = counter2.next;
		}
		
		if (counter1.rank < counter2.rank) {
			newBinHeap.min = counter1;
			newBinHeap.last = counter1;
			newBinHeap.size += Math.pow(2, counter1.rank);
			counter1 = counter1.next;

		}
		
		if (counter1.rank > counter2.rank){
			newBinHeap.min = counter2;
			newBinHeap.last = counter2;
			newBinHeap.size += Math.pow(2, counter2.rank);
			counter2 = counter2.next;
		}
		
		
		while (counter1 != this.last.next && counter2 != heap2.last.next) {
			if (temp.item.key != -1) {
				if (counter1.rank == counter2.rank) {
					temp = link(counter1, counter2); //TODO: add link
					counter1 = counter1.next;
					counter2 = counter2.next;					
				}
				if (counter1.rank < counter2.rank) {
					newBinHeap.last.next = counter1;
					newBinHeap.size += Math.pow(2, counter1.rank); //fixing size  
					newBinHeap.last = newBinHeap.last.next;
					newBinHeap.min = newBinHeap.findMin();
					counter1 = counter1.next;
					
				}
				if (counter2.rank < counter1.rank) {
					newBinHeap.last.next = counter2;
					newBinHeap.size += Math.pow(2, counter2.rank); //fixing size  
					newBinHeap.last = newBinHeap.last.next;
					counter2 = counter2.next;
					newBinHeap.size += Math.pow(2, counter2.rank); //fixing size  
				}
			}
			else {// if there is a temp
				if (counter1.rank == counter2.rank) {
					newBinHeap.last.next = temp;
					newBinHeap.size += Math.pow(2, temp.rank); //fixing size  

					newBinHeap.last = newBinHeap.last.next;
					temp = link(counter1, counter2); //TODO: add link
				}
				
				if (counter1.rank < counter2.rank) {
					temp = link(temp, counter1); //TODO: add link
					counter1 = counter1.next;
				}
				if (counter2.rank < counter1.rank) {
					temp = link(temp, counter2); //TODO: add link
					counter2 = counter2.next;
				}
				
			}
		}
		
		 if (temp != null) {
		        newBinHeap.last.next = temp;
		        newBinHeap.last = temp;
		    }

		    this.last = newBinHeap.last;
		    this.size = newBinHeap.size;
		    this.min = newBinHeap.min;
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return 42; // should be replaced by student code
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		if (this.last.key)
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public static class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
		
		public HeapNode() {
			HeapItem fake = new HeapItem();
		}
		
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public static class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		public HeapItem() {
			this.info = null;
			this.key = -1;
		}
	}
}
