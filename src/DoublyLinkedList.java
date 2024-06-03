import java.util.HashMap;
import java.util.Map;

public class DoublyLinkedList<K, V> {
    class Node<E> {
        private Node<E> nextNode;
        private Node<E> prevNode;
        private E value;

        public Node(Node<E> prevNode, E value, Node<E> nextNode) {
            this.prevNode = prevNode;
            this.value = value;
            this.nextNode = nextNode;
        }

        public Node<E> getNextNode() {
            return nextNode;
        }

        public void setNextNode(Node<E> nextNode) {
            this.nextNode = nextNode;
        }

        public Node<E> getPrevNode() {
            return prevNode;
        }

        public void setPrevNode(Node<E> prevNode) {
            this.prevNode = prevNode;
        }

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
        }

        public boolean equals(Object o) {
            if ((o == null) || (this.getClass() != o.getClass())) {
                return false;
            } else {
                Node<E> transformedNode = (Node<E>) o;
                return (this.getValue().equals(transformedNode.getValue()));
            }
        }
    }

    private Node<V> head;
    private Node<V> tail;

    private final Map<K, Node<V>> innerMap;
    private int size;

    public DoublyLinkedList() {
        this.size = 0;
        this.innerMap = new HashMap<>();
    }

    public Node<V> getHead() {
        return head;
    }

    public Node<V> getTail() {
        return tail;
    }

    public int size() {
        return size;
    }

    public void addLast(K key, V value) {

        if (innerMap.containsKey(key)) {
            remove(key);
        }

        if (this.head == null) {
            this.head = new Node<>(null, value, null);
            this.tail = head;
        } else {
            this.tail.setNextNode(new Node<>(tail, value, null));
            this.tail = tail.getNextNode();
        }
        this.innerMap.put(key, tail);
        this.size++;
    }

    public void addFirst(K key, V value) {
        if (innerMap.containsKey(key)) {
            remove(key);
        }

        if (this.head == null) {
            this.head = new Node<>(null, value, null);
            this.tail = head;
        } else {
            this.head.setPrevNode(new Node<>(null, value, head));
            this.head = head.getPrevNode();
        }
        this.innerMap.put(key, head);
        this.size++;
    }

    public V remove(K key) {
        Node<V> nodeToRemove = innerMap.remove(key);

        if (nodeToRemove != null) {
            Node<V> prevNode = nodeToRemove.getPrevNode();
            Node<V> nextNode = nodeToRemove.getNextNode();

            if (prevNode != null && nextNode != null) {
                prevNode.setNextNode(nextNode);
                nextNode.setPrevNode(prevNode);
            } else if (prevNode != null) {
                prevNode.setNextNode(null);
                tail = prevNode;
            } else if (nextNode != null) {
                nextNode.setPrevNode(null);
                head = nextNode;
            } else {
                head = null;
                tail = null;
            }

            size--;
            return nodeToRemove.getValue();
        }

        return null;

    }


}
