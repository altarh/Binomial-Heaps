
@Override
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
                appendTree(sb, child.next, indent, child.next == node.child);
                child = child.next;
            } while (child != node.child);
        }
    }
