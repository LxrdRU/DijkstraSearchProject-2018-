/*
 * Min priority queue node
 */

//public class NodePriorityQueue<T> extends NodeBinaryTree<T> implements Comparable<NodePriorityQueue<T>>
public class NodePriorityQueue<T> extends NodeBinaryTree<T> {
    private double priority;  // The smaller, the higher

    public NodePriorityQueue(T content) {
        super(content);
    }

    public void setPriority(double p) {
        priority = p;
    }

    public double getPriority() {
        return priority;
    }
}