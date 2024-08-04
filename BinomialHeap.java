/**
 * BinomialHeap
 *
 * An implementation of binomial heap over positive integers.
 *
 */



public class BinomialHeap {
	public int size;
	public HeapNode last;
	public HeapNode min;
	public HeapNode first;
	public int numOfTrees = 0;
	public int cntr = 0;
	public int rank_cntr = 0;

	//////////////main: for debugging!!
	public static void main(String[] args) {
		BinomialHeap heap = new BinomialHeap();
		for (int i = 2; i < 2000; i++) {
			heap.insert(i, "test");
		}

		System.out.println(heap.last.item.key);
		System.out.println(heap.min.item.key);
		System.out.println(heap.size);
		System.out.println(heap.numOfTrees);
		System.out.println(heap.last.child.child.child.item.key);
		System.out.println(heap.min.rank);
		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		heap.deleteMin();
		System.out.println(heap.size);
		System.out.println(heap.numOfTrees);
		System.out.println(heap.min.item.key);



	}




	public HeapNode link(HeapNode node1, HeapNode node2) {
		cntr += 1;
		numOfTrees -= 1;
		HeapNode newNode2 = HeapNode.clone(node2);
		HeapNode newNode1 = HeapNode.clone(node1);

		if (this.min == node1) {
			this.min = newNode1;
		}
		if (this.min == node2) {
			this.min = newNode2;
		}

		// Ensure newNode1 is the child with the larger key
		if (newNode1.item.key < newNode2.item.key) {
			HeapNode temp = newNode2;
			newNode2 = newNode1;
			newNode1 = temp;
		}

		// Adding node1 to the end of the children-linked-list of node2
		if (newNode2.rank > 0) {
			// Find the last child in the current child list of newNode2
			HeapNode firstChild = newNode2.child.next;
			newNode2.child.next = newNode1;
			newNode1.next = firstChild;

		} else {
			// This case handles when the node2's child list is empty
			newNode2.child = newNode1;
			newNode1.next = newNode2; // pointing to itself as it's the only child
		}
		newNode2.child = newNode1;
		newNode1.parent = newNode2;
		newNode2.rank += 1;

		return newNode2;
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
		HeapNode new_node = new HeapNode();
		new_node_item.key = key;
		new_node_item.info = info;
		new_node_item.node = new_node;
		new_node.item = new_node_item;
		new_node.rank = 0;
		new_node.next = new_node;


		if (this.empty()) {
			this.last = new_node;
			this.last.next = this.last;
			this.size = 1;
			this.numOfTrees = 1;
			this.min = this.last;
		}

		else {
			BinomialHeap newBinHeap = new BinomialHeap();
			newBinHeap.min = new_node;
			newBinHeap.last = new_node;
			newBinHeap.size = 1;
			newBinHeap.numOfTrees = 1;


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

		// if deleted node has 0 children or deleted node is the sole node in our heap.
		if (this.min.child == null){
			if (this.min.next == this.min) {
				this.min = null;
				this.last = null;
				this.numOfTrees = 0;
				size = 0;
				return;
			}

			HeapNode node = this.min.next;
			while (node.next != this.min) {
				node = node.next;
			}
			HeapNode temp = node.next.next;
			node.next.next = null;
			node.next = temp;
			this.min = findNewMin(node);
			this.size -= 1;
			this.numOfTrees -= 1;
			return;

		}

		// if deleted minimum has 1 or more children.
		min_node_children.last = this.min.child;
		min_node_children.min = findChildrenMin(min_node_children.last);
		min_node_children.size = ((int)Math.pow(2, this.min.rank) - 1);
		min_node_children.numOfTrees = this.min.rank;
		this.min.child = null;

		// removing any connection to the previous minimum
		int rank_of_deleted_node = this.min.rank;
		rank_cntr += rank_of_deleted_node;
		HeapNode node = this.min.next;
		if (node == this.min) {
			this.min = min_node_children.min;
			this.last = min_node_children.last;
			this.size = min_node_children.size;
			this.numOfTrees = rank_of_deleted_node;
			return;
		}
		while (node.next != this.min) {
			node = node.next;
		}
		HeapNode temp = node.next.next;
		node.next.next = null;
		node.next = temp;
		this.min = findNewMin(node);
		this.size = this.size - (int) Math.pow(2,rank_of_deleted_node);
		this.last = this.last.rank == rank_of_deleted_node ? node : this.last;
		this.numOfTrees -= 1;

		this.meld(min_node_children);

		return; // should be replaced by student code

	}

	private HeapNode findChildrenMin(HeapNode start) {
		if (start == null) {
			return null;
		}

		HeapNode curr = start;
		HeapNode newMin = curr;

		do {
			curr.parent = null;
			if (curr.item.key < newMin.item.key) {
				newMin = curr;
			}
			curr = curr.next;
		} while (curr != null && curr != start);

		return newMin;
	}

	private HeapNode findNewMin(HeapNode start) {
		if (start == null) {
			return null;
		}

		HeapNode curr = start;
		HeapNode newMin = curr;

		do {
			if (curr.item.key < newMin.item.key) {
				newMin = curr;
			}
			curr = curr.next;
		} while (curr != start);

		return newMin;
	}


	/**
	 *
	 * Return the minimal HeapItem, null if empty.
	 *
	 */
	public HeapItem findMin()
	{
		if (!this.empty()) {
			return this.min.item;
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
	public void decreaseKey(HeapItem item, int diff) {
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
			findNewMin(curr_node);

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
		int inf = Integer.MAX_VALUE;
		decreaseKey(item, inf);
		this.min = findNewMin(this.min);
		this.deleteMin();
	}

	/**
	 *
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)

	{
		int link_cntr = 0;
//		if (heap2.size == 1){
//			meld_single(heap2);
//			return;
//		}
//		if (this.size == 1) {
//			heap2.meld_single(this);//TODO: check if thats ok
//			this.size = heap2.size;
//			this.last = heap2.last;
//			this.min = heap2.min;
//			return;
//		}

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
			this.min = heap2.min;
		}
		newBinHeap.size = this.size + heap2.size;
		newBinHeap.numOfTrees = this.numOfTrees + heap2.numOfTrees;



		if (counter1.rank == counter2.rank) {
			temp = link(counter1, counter2);
			link_cntr += 1;
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
					link_cntr += 1;
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
						link_cntr += 1;
						counter1 = counter1.next;
						counter2 = counter2.next;
						continue;
					}

					if (counter1.rank < counter2.rank) {
						temp = link(temp, counter1);
						link_cntr += 1;
						counter1 = counter1.next;
						continue;
					}
					if (counter2.rank < counter1.rank) {
						temp = link(temp, counter2);
						link_cntr += 1;
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
					link_cntr += 1;
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
		this.numOfTrees = Integer.bitCount(this.size);
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


//	private void meld_single(BinomialHeap heap2) {
//
//		this.numOfTrees += heap2.numOfTrees;
//		if (heap2.min.item.key < this.min.item.key) { //Updating min
//			this.min = heap2.min;
//		}
//		if (this.size == 1) {
//			System.out.println("1");
//			this.last = link(this.last, heap2.last);
//			this.last.next = this.last;
//			this.last.rank = 1;
//			this.size += 1;
//			return;
//		}
//		if (this.last.next.rank != 0) { //If the first item in the heap is not from deg 0
//			//			System.out.println("work" + heap2.last.item.key);
//			HeapNode first = this.last.next;
//			this.last.next = heap2.last;
//			heap2.last.next = first;
//			this.size += 1;
//			return;
//
//		}
//		// If there is a tree from deg 0.
//		else {
//			HeapNode temp = link(this.last.next, heap2.last);
//			temp.next = this.last.next.next;
//			this.last.next = temp;
//			this.size += 1;
//
//			HeapNode curr = temp.next;
//			HeapNode temp2 = new HeapNode();
//
//			do {
//				if (curr.rank == temp.rank) {
//					temp2 = link(this.last.next, curr);
//
//					if (curr.next == temp) { //if there is only one tree in the heap now
//						this.last = temp2;
//						this.last.next = temp2;
//						return;
//					}
//					else {
//						temp2.next = curr.next;
//						this.last.next = temp2;
//						curr = temp2.next;
//
//						curr = temp2.next;
//						temp = temp2;
//					}
//
//				}
//				else {
//					return;
//				}
//
//			}while (curr != this.last.next);
//
//			if (curr.rank == temp.rank) {
//				temp = link(this.last.next, curr);
//				this.last = temp;
//			}
//		}
//
//	}



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
		return numOfTrees; // should be replaced by student code
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

		public int getKey() {
			return this.item.key;
		}

		public HeapNode() {
			this.item = new HeapItem();
		}

		public static HeapNode clone(HeapNode node) {
			HeapNode temp = new HeapNode();
			temp.next = node.next;
			temp.child = node.child;
			node.item.node = temp;
			temp.parent = node.parent;
			temp.rank = node.rank;
			temp.item = node.item;
			return temp;
		}

		public HeapNode getParent() {
			return this.parent;
		}

		public HeapNode getNext() {
			return this.next;
		}

		public HeapNode getChild() {
			return this.child;
		}
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *
	 */
	public static class HeapItem {
		public HeapNode node;
		public int key;
		public String info;

		public HeapItem() {
			this.info = null;
			this.key = -1;
		}
	}

}
