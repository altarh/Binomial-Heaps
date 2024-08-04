import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

class Pair2<T, S> {
    public final T first;
    public final S second;

    public Pair2(T first, S second) {
        this.first = first;
        this.second = second;
    }
}

public class SanitizeBinomialHeap {
    public static void sanitize(BinomialHeap heap) {
        verify_size(heap);
//        verify_numTrees(heap);
//        verify_heap_rule(heap);
//        verify_binomial_trees(heap);
        
        verify_over_nodes(heap);
        verify_ranks(heap);
    }

    // verifies several things together to avoid iterating the nodes too much and improve performance
    static void verify_over_nodes(BinomialHeap heap) {
        int numTrees = 0;
        
        if (!heap.empty())
        {
            BinomialHeap.HeapNode heapNode = heap.last.next;
            
            // first, compare ranks of last and first.
            if (heap.last != heap.last.next && !(heap.last.rank > heap.last.next.rank)) {
                // rank of last should be the largest.
                throw new RuntimeException("error - expecting ranks to be sorted from smallest to largest, but last is not larger than first.");
            }
            
            do {
                numTrees++; // numTrees is checked last so if there is an issue with numTrees you will be seeing other errors first.
                
                if (heapNode.next != heapNode && // if not the only node
                    heapNode != heap.last && // not the last node
                    !(heapNode.rank < heapNode.next.rank)) {  // going from first to last
                    HeapPrinter hp = new HeapPrinter(System.out);
                    hp.print(heap, false);
                    throw new RuntimeException("error - expecting ranks to be sorted from smallest to largest");
                }
                
                verify_binomial_tree(heapNode);
                verify_heap_rule(heapNode);
                heapNode = heapNode.next;
            } while (heapNode != heap.last.next);
        }
        
        
        if (numTrees != heap.numTrees()) {
            throw new RuntimeException("error in numTrees. counted " + numTrees + " trees in heap but field is set to " + heap.numTrees());
        }
        
    }
    
    static void verify_binomial_trees(BinomialHeap heap) {
        BinomialHeap.HeapNode heapNode = heap.last;
        
        if (heap.empty())
            return;
        
        do {
            verify_binomial_tree(heapNode);
            heapNode = heapNode.next;
        } while (heapNode != heap.last);
    }
    
    static void verify_binomial_tree(BinomialHeap.HeapNode rootNode) {
        BinomialHeap.HeapNode node = rootNode;
        int rank = rootNode.rank;
        
        verify_binomial_tree_level(rootNode);
    }
    
    static void verify_binomial_tree_level(BinomialHeap.HeapNode rootNode) {
        if (rootNode.rank == 0) {
            if (rootNode.child != null) {
                throw new RuntimeException("error - tree of rank 0 should have no children");
            }
            return;
        }
        if (rootNode.child == null) {
            throw new RuntimeException("error - expected child node but found null");
        }
        if (rootNode.child.rank != rootNode.rank - 1) {
            throw new RuntimeException("error - max rank among subnodes should go down by 1 with each level");
        }        
        
        BinomialHeap.HeapNode childNode = rootNode.child.next;
        Set<BinomialHeap.HeapNode> subnodes = new HashSet<>();
        
        for (int i=0; i < rootNode.rank; i++) {
            subnodes.add(childNode);
            if (i != rootNode.rank - 1 && //not in last node
                !(childNode.rank < childNode.next.rank)) {
                throw new RuntimeException("error - expecting ranks to be sorted from smallest to largest");
            }
            verify_binomial_tree_level(childNode);  // verify also for all subnodes.
            childNode = childNode.next;
        }

        if (subnodes.size() < rootNode.rank) {
            throw new RuntimeException("error - expected " + rootNode.rank + " subnodes but found less - " + subnodes.size());
        }
        if (subnodes.size() > rootNode.rank) {
            throw new RuntimeException("error - expected " + rootNode.rank + " subnodes but found more - " + subnodes.size());
        }
    }
    
    static void verify_heap_rule(BinomialHeap heap) {
        // all nodes most be smaller than their children.
        BinomialHeap.HeapNode heapNode = heap.last;
        
        if (heap.empty())
            return;
        
        do {
            verify_heap_rule(heapNode);
            heapNode = heapNode.next;
        } while (heapNode != heap.last);
    }
    
    static void verify_heap_rule(BinomialHeap.HeapNode rootNode) {
        BinomialHeap.HeapNode childNode = rootNode.child;
        
        if (childNode == null)
            return;
        
        do {
            if (childNode.item.key < rootNode.item.key) {
                throw new RuntimeException("error - child key " +  childNode.item.key + " is smaller than parent key " + rootNode.item.key);
            }
            
            verify_heap_rule(childNode);
            
            childNode = childNode.next;
        } while (childNode != rootNode.child);
    }
    
    static void verify_ranks(BinomialHeap heap) {
        int size = heap.size();
        String binary = Integer.toBinaryString(size);
        int lit_bits = binary.length() - binary.replace("1", "").length();
        
        if (lit_bits != heap.numTrees()) {
            throw new RuntimeException("error in numTrees. should be " + lit_bits + " trees in heap but field is set to " + heap.numTrees());
        }
        
        if (heap.empty())
            return;
        
        BinomialHeap.HeapNode heapNode = heap.last.next;
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(binary.length() - i - 1) == '1') {
                if (heapNode.rank != i) {
                    throw new RuntimeException("error in tree ranks."
                            + " binary represantation is " + binary
                            + " for bit number " + i + " expecting rank " + i
                            + " and got rank " + heapNode.rank);
                }
                heapNode = heapNode.next;
            }
        }
    }
    
    static void verify_numTrees(BinomialHeap heap) {
        BinomialHeap.HeapNode heapNode = heap.last;
        int numTrees = 0;
        
        if (!heap.empty())
        {
            do {
                numTrees++;
                heapNode = heapNode.next;
            } while (heapNode != heap.last);
        }
        
        
        if (numTrees != heap.numTrees()) {
            throw new RuntimeException("error in numTrees. counted " + numTrees + " trees in heap but field is set to " + heap.numTrees());
        }
    }
    
    static void verify_size(BinomialHeap heap) {
        BinomialHeap.HeapNode heapNode = heap.last;
        
        Stack<Pair<BinomialHeap.HeapNode, Integer>> stack = new Stack<>();
        Set<BinomialHeap.HeapNode> visited = new HashSet<>();
        visited.add(null);

        ArrayList<Boolean> nexts = new ArrayList<>();

        nexts.add(false);
        int depth = 1;
        while (!visited.contains(heapNode) || !stack.empty()) {
            if (visited.contains(heapNode)) {
                Pair<BinomialHeap.HeapNode, Integer> pair = stack.pop();
                heapNode = pair.first;
                depth = pair.second;
                while (nexts.size() > depth) {
                    nexts.remove(nexts.size() - 1);
                }
                continue;
            }

            visited.add(heapNode);
            nexts.set(nexts.size() - 1, !visited.contains(heapNode.next));
            stack.push(new Pair<>(heapNode.next, depth));

            heapNode = heapNode.child;
            if (heapNode != null) {
                heapNode = heapNode.next;
                nexts.add(false);
            }
            depth++;
        }
        
        visited.remove(null);
        
        if (visited.size() != heap.size()) {
            throw new RuntimeException("error in heap size. counted " + visited.size() + " nodes in heap but field is set to " + heap.size());
        }
    }
}
