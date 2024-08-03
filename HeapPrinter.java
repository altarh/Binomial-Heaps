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

class HeapPrinter {
    static final String NULL = "(null)";
    final PrintStream stream;

    public HeapPrinter(PrintStream stream) {
        this.stream = stream;
    }

    void printIndentPrefix(ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        for (int i = 0; i < size - 1; ++i) {
            this.stream.format("%c   ", hasNexts.get(i).booleanValue() ? '│' : ' ');
        }
    }

    void printIndent(BinomialHeap.HeapNode heapNode, ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        printIndentPrefix(hasNexts);

        this.stream.format("%c── %s\n",
                hasNexts.get(size - 1).booleanValue() ? '├' : '╰',
                heapNode == null ? NULL : String.valueOf(heapNode.item.key));
    }

    static String repeatString(String s, int count) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < count; i++) {
            r.append(s);
        }
        return r.toString();
    }

    void printIndentVerbose(BinomialHeap.HeapNode heapNode, ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        if (heapNode == null) {
            printIndentPrefix(hasNexts);
            this.stream.format("%c── %s\n", hasNexts.get(size - 1).booleanValue() ? '├' : '╰', NULL);
            return;
        }

        Function<Supplier<BinomialHeap.HeapNode>, String> keyify = f -> {
            BinomialHeap.HeapNode node = f.get();
            return node == null ? NULL : String.valueOf(node.item.key);
        };
        String title = String.format(" Key: %d ", heapNode.item.key);
        List<String> content = Arrays.asList(
                String.format(" Rank: %d ", heapNode.rank),
                String.format(" Parent: %s ", keyify.apply(heapNode::getParent)),
                String.format(" Next: %s ", keyify.apply(heapNode::getNext)),
                String.format(" Child: %s", keyify.apply(heapNode::getChild)));

        /* Print details in box */
        int length = Math.max(
                title.length(),
                content.stream().map(String::length).max(Integer::compareTo).get());
        String line = repeatString("─", length);
        String padded = String.format("%%-%ds", length);
        boolean hasNext = hasNexts.get(size - 1);

        // print header row
        printIndentPrefix(hasNexts);
        this.stream.format("%c── ╭%s╮\n", hasNext ? '├' : '╰', line);

        // print title row
        printIndentPrefix(hasNexts);
        this.stream.format("%c   │" + padded + "│\n", hasNext ? '│' : ' ', title);

        // print separator
        printIndentPrefix(hasNexts);
        this.stream.format("%c   ├%s┤\n", hasNext ? '│' : ' ', line);

        // print content
        for (String data : content) {
            printIndentPrefix(hasNexts);
            this.stream.format("%c   │" + padded + "│\n", hasNext ? '│' : ' ', data);
        }

        // print footer
        printIndentPrefix(hasNexts);
        this.stream.format("%c   ╰%s╯\n", hasNext ? '│' : ' ', line);
    }

    void printHeapNode(BinomialHeap.HeapNode heapNode, boolean verbose) {
        BiConsumer<BinomialHeap.HeapNode, ArrayList<Boolean>> function =
            verbose ?  this::printIndentVerbose : this::printIndent;

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

            function.accept(heapNode, nexts);

            heapNode = heapNode.child;
            if (heapNode != null) {
                heapNode = heapNode.next;
                nexts.add(false);
            }
            depth++;
        }
    }

    public void print(BinomialHeap heap, boolean verbose) {
        if (heap == null) {
            this.stream.print(NULL + "\n");
            return;
        } else if (heap.empty()) {
            this.stream.print("(empty)\n");
            return;
        }

        this.stream.print("╮\n");
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(false);
        printHeapNode(heap.last == null ? null : heap.last.next, verbose);
    }
    
    public void print(BinomialHeap.HeapNode root_node, boolean verbose) {
        if (root_node == null) {
            this.stream.print("(empty)\n");
            return;
        }

        this.stream.print("╮\n");
        printHeapNode(root_node, verbose);
    }
}
