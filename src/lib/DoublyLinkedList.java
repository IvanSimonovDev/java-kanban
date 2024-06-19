package lib;

import lib.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class DoublyLinkedList<T extends Task> {
    class Node<E extends Task> {
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

    private Node<T> head;
    private Node<T> tail;

    private final Map<Short, Node<T>> innerMap;
    private int size;

    public DoublyLinkedList() {
        this.size = 0;
        this.innerMap = new HashMap<>();
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    public int size() {
        return size;
    }

    public void addLast(T task) {

        if (innerMap.containsKey(task.id)) {
            remove(task.id);
        }

        if (this.head == null) {
            this.head = new Node<>(null, task, null);
            this.tail = head;
        } else {
            this.tail.setNextNode(new Node<>(tail, task, null));
            this.tail = tail.getNextNode();
        }
        this.innerMap.put(task.id, tail);
        this.size++;
    }

    public void addFirst(T task) {
        if (innerMap.containsKey(task.id)) {
            remove(task.id);
        }

        if (this.head == null) {
            this.head = new Node<>(null, task, null);
            this.tail = head;
        } else {
            this.head.setPrevNode(new Node<>(null, task, head));
            this.head = head.getPrevNode();
        }
        this.innerMap.put(task.id, head);
        this.size++;
    }

    public T remove(Short taskId) {
        Node<T> nodeToRemove = innerMap.remove(taskId);

        if (nodeToRemove != null) {
            Node<T> prevNode = nodeToRemove.getPrevNode();
            Node<T> nextNode = nodeToRemove.getNextNode();

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
