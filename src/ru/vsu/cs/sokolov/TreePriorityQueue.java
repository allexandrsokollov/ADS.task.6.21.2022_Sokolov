package ru.vsu.cs.sokolov;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

public class TreePriorityQueue<T> {

    Node queueRoot;
    Comparator<T> comparator;
    HashSet<Node> unique;
    boolean switcher;

    public TreePriorityQueue(Comparator<T> comparator) {
        this.queueRoot = null;
        this.comparator = comparator;
        unique = new HashSet<>();
        switcher = false;
    }

    public String toString() {
        return "graph {\n" + getEdges(queueRoot) + "}";
    }

    private String getEdges(Node node) {
        StringBuilder string = new StringBuilder();

        if (node.leftChild != null) {
            string.append(node.value).append(" -- ");
            string.append(node.leftChild.value).append(" ").append("\n").append(getEdges(node.leftChild));
        }

        if (node.rightChild != null) {
            string.append(node.value).append(" -- ");
            string.append(node.rightChild.value).append(" ").append("\n").append(getEdges(node.rightChild));
        }
        return string.toString();
    }

    public void add(T value) {
        Node toAdd = new Node(value);

        if (!unique.contains(toAdd)) {
            unique.add(toAdd);
            if (queueRoot == null) {
                queueRoot = toAdd;
                return;
            }

            Node currentNode = queueRoot;
            boolean isAdded = false;

            while (!isAdded) {
                if (comparator.compare(toAdd.value, currentNode.value) >= 0) {
                    if (switcher) {
                        toAdd.setLeftChild(currentNode.leftChild);
                        currentNode.setLeftChild(null);
                        toAdd.setRightChild(currentNode);
                        queueRoot = toAdd;
                        isAdded = true;
                        switcher = false;
                    } else {
                        toAdd.setRightChild(currentNode.rightChild);
                        currentNode.setRightChild(null);
                        toAdd.setLeftChild(currentNode);
                        queueRoot = toAdd;
                        isAdded = true;
                        switcher = true;
                    }
                } else {

                    if (currentNode.leftChild == null || currentNode.rightChild == null) {
                        if (currentNode.leftChild == null) {
                            currentNode.leftChild = toAdd;
                        } else {
                            currentNode.rightChild = toAdd;
                        }
                        isAdded = true;
                    } else {

                        if (comparator.compare(currentNode.leftChild.value, currentNode.rightChild.value) >= 0) {
                            currentNode = currentNode.rightChild;
                        } else {
                            currentNode = currentNode.leftChild;
                        }

                    }
                }
            }
        }
    }


    class Node {
        private final T value;
        private Node leftChild;
        private Node rightChild;

        public Node(T value) {
            this.value = value;
            leftChild = null;
            rightChild = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }
    }
}
