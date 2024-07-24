import java.util.Arrays;

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
	
	//////////////main: for debugging!!
	public static void main(String[] args) {
	BinomialHeap heap = new BinomialHeap();
	
	// Test case: Insert elements
	heap.insert(10, "Ten");
	System.out.println("insert 10");
	heap.insert(20, "Twenty");
	System.out.println("insert 20");
	heap.insert(5, "Five");
	System.out.println("insert5");
	
	
	System.out.println("next of something: " + heap.last.next.item.key);
	// Test case: Find minimum
	System.out.println("Minimum key: " + heap.findMin().item.key); // Should print 5
	
	// Test case: Delete minimum
	heap.deleteMin();
	System.out.println("Minimum key after deleteMin: " + heap.findMin().item.key); // Should print 10
	
	// Test case: Decrease key
	HeapItem item = heap.insert(15, "Fifteen");
	heap.decreaseKey(item, 10); // Change 15 to 5
	System.out.println("Minimum key after decreaseKey: " + heap.findMin().item.key); // Should print 5
	
	// Test case: Delete an item
	heap.delete(item);
	System.out.println("Minimum key after delete: " + heap.findMin().item.key); // Should print 10 or 20
	}
	/////////////////for deletion


	public HeapNode link(HeapNode node1, HeapNode node2) {
		if (node1.item.key < node2.item.key) {
			HeapNode temp = node2;
			node2 = node1;
			node1 = temp;
		}
		// Adding node1 to children-linked-list
		if (node2.rank > 0) {
			HeapNode temp = node2.child.next;
			node2.child.next = node1;
			node1.next = temp;
			node2.child = node1;
			node1.parent = node2;
			node2.rank += 1;
		}

		return node2;

	}

	public void find_min(HeapNode node) {
		HeapNode next_node = node.next;
		int min = node.item.key;
		while (next_node != node) {
			if (next_node.item.key < min) {
				node = next_node;
			}
			next_node = next_node.next;
		}
		this.min = node;

	}


	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	
	public HeapItem insert(int key, String info) 
	{
		HeapItem new_node_item = new HeapItem();
		new_node_item.key = key;
		new_node_item.info = info;
		HeapNode new_node = new HeapNode();
		new_node.item = new_node_item;
		new_node.rank = 0;

		if (this.empty()) {
			this.last = new_node;
			this.last.next = this.last;
			this.size += 1;
			this.min = this.last;
		}
		
		else {
			BinomialHeap newBinHeap = new BinomialHeap();
			newBinHeap.min = new_node;
			newBinHeap.last = new_node;
			newBinHeap.last.next = new_node;
			
	
			this.meld(newBinHeap);
		}

		// here altar should set new_node_item.node to be new_node.parent; then it's complete

		return new_node.item; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		HeapNode min_node = this.min;
		BinomialHeap min_node_children = new BinomialHeap();
		min_node_children.last = this.min.child;
		min_node_children.min = this.min.child;
		int min_num = min_node_children.min.item.key;
		for (int i = 0; i < this.min.rank; i ++) {
			HeapNode node = min_node_children.min.next;
			if (min_num > node.item.key)  min_node_children.min = node;
		}
		// removing any connection to the previous minimum
		HeapNode node = this.min.next;
		this.min.child = null;
		while (node.next != this.min) {node = node.next;}
		node.next = node.next.next;
		this.min = node;
		min_node_children.last.parent = null;

		// Should I determine a minimum node after deleting min? or should I leave it for this.meld


		this.meld(min_node_children);
		return; // should be replaced by student code

	}

	/**
	 * 
	 * Return the minimal HeapItem, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		if (!this.empty()) {
			return this.min;
		}; // should be replaced by student code
		return null; //Change to sentinel.
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
		item.key = item.key - diff;
		HeapNode curr_node = item.node;
		HeapNode parent_node = item.node.parent;

		while (curr_node.item.key < parent_node.item.key) {
			HeapItem temp = parent_node.item;
			parent_node.item = curr_node.item;
			curr_node.item = parent_node.item;

			curr_node = parent_node;
			parent_node = curr_node.parent;
		}

		if (curr_node.parent.equals(null)) { // Fix the condition for sentinel!!!! then we know it is one of the roots
			find_min(curr_node);

		}
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item)
	{
		int inf = Integer.MIN_VALUE;
		decreaseKey(item, inf);
		this.deleteMin();
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2) //TODO: change parents-brothers fields.
	{
		BinomialHeap newBinHeap = new BinomialHeap();
		newBinHeap.last = new HeapNode(); //sentinal

		HeapNode counter1 = last.next;
		HeapNode counter2 = heap2.last.next;
		HeapNode temp = new HeapNode();
		
		if (heap2.min.item.key < this.min.item.key) {
			this.min = heap2.min;
		}
		
		
		if (counter1.rank == counter2.rank) {
			System.out.println("==");
			temp = link(counter1, counter2);
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
		
		
		do {
			System.out.println(temp.item.key);
			if (temp.item.key != -1) {
				if (counter1.rank == counter2.rank) {
					temp = link(counter1, counter2);
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
				if (counter1.rank == temp.rank || counter2.rank == temp.rank) {
					if (counter1.rank == counter2.rank) { //if they are equal they are for sure not equal to the temp
						newBinHeap.last.next = temp;
						newBinHeap.size += Math.pow(2, temp.rank); //fixing size  

						newBinHeap.last = newBinHeap.last.next;
						temp = link(counter1, counter2); 
					}
					
					if (counter1.rank < counter2.rank) {
						temp = link(temp, counter1); 
						counter1 = counter1.next;
					}
					if (counter2.rank < counter1.rank) {
						temp = link(temp, counter2); 
						counter2 = counter2.next;
					}
				}
				else {
					newBinHeap.last.next = temp;
					newBinHeap.size += Math.pow(2, temp.rank); 
				}
				
			}
			
		}
		while (counter1 != this.last && counter2 != heap2.last || (counter1.rank != 0 && counter2.rank != 0));
		
		 if (temp.item.key!= -1) {
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
		return this.size; // should be replaced by student code
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return (this.size == 0); // should be replaced by student code
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
        return Integer.bitCount(this.size); // should be replaced by student code
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
			this.item = new HeapItem();
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
