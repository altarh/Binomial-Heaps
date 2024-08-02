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
	public HeapNode first;
	
	//////////////main: for debugging!!
	public static void main(String[] args) {
		 BinomialHeap heap = new BinomialHeap();

	        // Test case: Insert elements
	        heap.insert(10, "Ten");
	        heap.insert(20, "Twenty");
	        heap.insert(5, "Five");
	        heap.insert(3, "Five");
	        heap.insert(7, "Five");
	        heap.insert(8, "Five");
	        heap.insert(9, "Five");
	        heap.insert(11, "Five");
	        heap.insert(12, "Five");
	        heap.insert(22, "Ten");
	        heap.insert(33, "Twenty");
	        heap.insert(6, "Five");
	        heap.insert(13, "Five");
	        heap.insert(17, "Five");
	        heap.insert(18, "Five");
	        heap.insert(19, "Five");
	        heap.insert(111, "Five");
	        heap.insert(1, "Five");
	        
	        
//	        
//	        System.out.println("size is " + heap.size);
//	        System.out.println("last is " + heap.last.item.key);
//	        System.out.println("last.next is " + heap.last.next.item.key);
//	        System.out.println("last.next rank is " + heap.last.next.rank);
//	        System.out.println("last.next.next is " + heap.last.next.next.item.key);
//	        System.out.println("last.next.next rank is " + heap.last.next.next.rank);

	        // Create the second heap
	        BinomialHeap heap2 = new BinomialHeap();
	        heap2.insert(15, "Fifteen");
	        heap2.insert(25, "Twenty-Five");
	        heap2.insert(35, "Thirty-Five");
	        heap2.insert(2, "Two");
	        heap2.insert(4, "Four");
	        heap2.insert(151, "Fifteen");
	        heap2.insert(251, "Twenty-Five");
	        heap2.insert(351, "Thirty-Five");
	        heap2.insert(21, "Two");
	        heap2.insert(41, "Four");

	        System.out.println("Heap 2:");
	        System.out.println(heap2.toString());

	        System.out.println("Heap 1:");
	        System.out.println(heap.toString());
	        
	        heap.meld(heap2);

	        System.out.println("Heap 1 after melding with Heap 2:");
	        System.out.println(heap.toString());
	        System.out.println("last is " + heap.last.item.key);
	        System.out.println("last.rank is " + heap.last.rank);
	        System.out.println("last.next is " + heap.last.next.item.key);
	        System.out.println("last.next.rank is " + heap.last.next.rank);
	        System.out.println("last.next.next is " + heap.last.next.next.item.key);
	        System.out.println("last.next..next.rank is " + heap.last.next.next.rank);
	        

	}
	/////////////////for deletion


	public HeapNode link(HeapNode node1, HeapNode node2) {
	    HeapNode newNode2 = HeapNode.clone(node2);
	    HeapNode newNode1 = HeapNode.clone(node1);

	    // Ensure newNode1 is the child with the larger key
	    if (newNode1.item.key < newNode2.item.key) {
	        HeapNode temp = newNode2;
	        newNode2 = newNode1;
	        newNode1 = temp;
	    }

	    // Adding node1 to the end of the children-linked-list of node2
	    if (newNode2.rank > 0) {
	        // Find the last child in the current child list of newNode2
	        HeapNode lastChild = newNode2.child;
	        while (lastChild.next != newNode2.child) {
	            lastChild = lastChild.next;
	        }
	        // Append newNode1 at the end of the list
	        lastChild.next = newNode1;
	        newNode1.next = newNode2.child; // maintain circular property
	    } else {
	        // This case handles when the node2's child list is empty
	        newNode2.child = newNode1;
	        newNode1.next = newNode1; // pointing to itself as it's the only child
	    }
	    newNode2.child = newNode1;
	    newNode1.parent = newNode2;
	    newNode2.rank += 1;

	    return newNode2;
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
		new_node.next = new_node;
		new_node.item.node = new_node;

		if (this.empty()) {
			this.last = new_node;
			this.last.next = this.last;
			this.size = 1;
			this.min = this.last;
		}
		
		else {
			BinomialHeap newBinHeap = new BinomialHeap();
			newBinHeap.min = new_node;
			newBinHeap.last = new_node;
			newBinHeap.size = 1;
			
	
			this.meld(newBinHeap);
		}

		// here altar should set new_node_item.node to be new_node.parent; then it's complete

		return new_node_item; // should be replaced by student code
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
		min_node_children.last = min_node.child;
		min_node_children.min = min_node.child;
		if (min_node_children.last == null){
			HeapNode node = this.min.next, travel = this.min.next;
			this.min.next = null;
			int key = node.item.key;
			while (travel.next != this.min) {
				travel = travel.next;
				if(travel.item.key < key) { node = travel;}

			}
			this.min = node;
			travel.next = null;

		}
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

		while (parent_node != null && curr_node.item.key < parent_node.item.key) {
			HeapItem temp = parent_node.item;
			parent_node.item = curr_node.item;
			curr_node.item = parent_node.item;

			curr_node = parent_node;
			parent_node = curr_node.parent;
		}

		if (curr_node.parent == null) { // Fix the condition for sentinel!!!! then we know it is one of the roots
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
	public void meld(BinomialHeap heap2) 
	
	{	
		if (heap2.size == 1){
			meld_single(heap2);
			return;
		}
		if (this.size == 1) {
			heap2.meld_single(this);//TODO: check if thats ok
			this.size = heap2.size;
			this.last = heap2.last;
			this.min = heap2.min;
			return;
		}
		
		BinomialHeap newBinHeap = new BinomialHeap();
		HeapNode temp = null;

		HeapNode counter1 = this.last.next;
		HeapNode counter2 = heap2.last.next;
		
		HeapNode last1 = this.last;
		HeapNode last2 = heap2.last;
		
		// Cutting the loop for the stop condition.
		this.last.next = null;
		heap2.last.next = null;

		if (heap2.min.item.key < this.min.item.key) { // Fixing min and size fields
			newBinHeap.min = heap2.min;			
		}
		newBinHeap.size = this.size + heap2.size;

		
		
		if (counter1.rank == counter2.rank) {
			temp = link(counter1, counter2);
			counter1 = counter1.next;
			counter2 = counter2.next;
		}
		else {
			if (counter1.rank < counter2.rank) {//TODO: CHECK
				Add(newBinHeap, counter1);
				counter1 = counter1.next;
			}
		}


		while (counter1 != null && counter2 != null) {
			if (temp == null) {
				if (counter1.rank == counter2.rank) {
					temp = link(counter1, counter2);
					counter1 = counter1.next;
					counter2 = counter2.next;
					continue;
				}
				if (counter1.rank < counter2.rank) {
					Add(newBinHeap, counter1);
					counter1 = counter1.next;
					continue;

				}
				if (counter2.rank < counter1.rank) {
					Add(newBinHeap, counter2);
					counter2 = counter2.next;
					continue;
				}
			}
			else {// if there is a temp
				if (counter1.rank == temp.rank || counter2.rank == temp.rank) {
					if (counter1.rank == counter2.rank) { //if they are equal they are for sure not equal to the temp
						Add(newBinHeap, temp);
						
						temp = link(counter1, counter2);
						counter1 = counter1.next;
						counter2 = counter2.next;
						continue;
					}

					if (counter1.rank < counter2.rank) {
						temp = link(temp, counter1);
						counter1 = counter1.next;
						continue;
					}
					if (counter2.rank < counter1.rank) {
						temp = link(temp, counter2);
						counter2 = counter2.next;
						continue;
					}
				}
				else {
//					System.out.println(newBinHeap);
					Add(newBinHeap, temp);
					temp = null;
					continue;

				}
			}
		}
		//TODO: change so that it will just connect to the last part of the heap
		
	 if (counter1 != null) {
		 counter2 = counter1; // Switching names
		 last2 = last1;
	 }

	 if (temp != null) {
		 while (counter2 != null) {
			 if(counter2.rank == temp.rank) {
				 temp = link(counter2, temp);
				 counter2 = counter2.next;
			 }
			 else {
				 break;
			 }	 
		 }
		 Add(newBinHeap, temp);
	 }
	 
	 if (counter2 != null) {
		 Add(newBinHeap, counter2);
		 newBinHeap.last = last2;
	 }
	 
	 	
	    this.last = newBinHeap.last;
	    this.last.next = newBinHeap.first;
	    this.size = newBinHeap.size;
	    this.min = newBinHeap.min;
	}
	
	private void Add(BinomialHeap BinHeap, HeapNode heapNode) { //For meld
		if (BinHeap.empty()) {
			BinHeap.last = heapNode;
			BinHeap.first = heapNode;
		}
		else {
			BinHeap.last.next = heapNode;
			BinHeap.last = heapNode;
		}
	}
	
	
	private void meld_single(BinomialHeap heap2) {
		
		if (heap2.min.item.key < this.min.item.key) { //Updating min
			this.min = heap2.min;
		}
		if (this.size == 1) {
			System.out.println("1");
			this.last = link(this.last, heap2.last);
			this.last.next = this.last;
			this.last.rank = 1;
			this.size += 1;
			return;
		}
		if (this.last.next.rank != 0) { //If the first item in the heap is not from deg 0
//			System.out.println("work" + heap2.last.item.key);
			HeapNode first = this.last.next;
			this.last.next = heap2.last;
			heap2.last.next = first;
			this.size += 1;
			return;
			
		}
		// If there is a tree from deg 0.
		else {
			HeapNode temp = link(this.last.next, heap2.last);
			temp.next = this.last.next.next;
			this.last.next = temp;
			this.size += 1;
			
			HeapNode curr = temp.next;
			HeapNode temp2 = new HeapNode();
			
			do {
				if (curr.rank == temp.rank) {
					temp2 = link(this.last.next, curr);
					
					if (curr.next == temp) { //if there is only one tree in the heap now
						this.last = temp2;
						this.last.next = temp2; 
						return;
					}
					else {
						temp2.next = curr.next;
						this.last.next = temp2;
						curr = temp2.next;
						
						curr = temp2.next;
						temp = temp2;
					}
					
				}
				else {
					return;
				}
				
			}while (curr != this.last.next);
			
			if (curr.rank == temp.rank) {
				temp = link(this.last.next, curr);
				this.last = temp;
			}
		}
		
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
		return (this.size == 0 || this.last == null); // should be replaced by student code
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
		
		public static HeapNode clone(HeapNode node) {
			HeapNode temp = new HeapNode();
			temp.next = node.next;
			temp.child = node.child;
			temp.parent = node.parent;
			temp.rank = node.rank;
			temp.item = node.item;
			return temp;
		}
		
		   @Override
		    public String toString() {
		        return  ""+ item.key;
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





/////////////////////print
	public String toString() {
	    if (this.empty()) {
	        return "Heap is empty";
	    }
	
	    StringBuilder sb = new StringBuilder();
	    sb.append("BinomialHeap\n");
	
	    BinomialHeap.HeapNode current = this.last.next;
	    do {
	        if (current != null) {
	            sb.append("Tree with root: ").append(current.toString()).append("\n");
	            appendTree(sb, current, "", true);
	            current = current.next;
	        }
	    } while (current != this.last.next);
	
	    return sb.toString();
	}

	private void appendTree(StringBuilder sb, BinomialHeap.HeapNode node, String indent, boolean last) {
	    if (node == null) return;
	
	    sb.append(indent);
	    if (last) {
	        sb.append("└── ");
	        indent += "    ";
	    } else {
	        sb.append("├── ");
	        indent += "│   ";
	    }
	    sb.append(node.toString()).append("\n");
	
	    if (node.child != null) {
	        BinomialHeap.HeapNode child = node.child;
	        do {
	            appendTree(sb, child, indent, child.next == node.child);
	            child = child.next;
	        } while (child != node.child);
	    }
	}
}

