package ru.vsu.cs.sokolov;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class TreePriorityQueue<T> {

    Node queueRoot;
    Comparator<T> comparator;
    HashSet<Node> unique;
    boolean switcher;
    Random random;

    public TreePriorityQueue(Comparator<T> comparator) {
        this.queueRoot = null;
        this.comparator = comparator;
        unique = new HashSet<>();
        switcher = false;
        random = new Random();
    }

    public void delete(T value) {
        Node finding = new Node(value);
        Node currentNode = queueRoot;

        if (finding.equals(currentNode)) {
            deleteNodeFromQueue(currentNode);
            return;
        }

        Visitor visitor = new Visitor();
        findNode(currentNode, finding, visitor);
        Node node = visitor.node;
        deleteNodeFromQueue(node);

    }

    class Visitor {
        Node node;
    }
    private void findNode(Node starting, Node finding, Visitor visitor) {
        if (starting.leftChild != null) {
            if (starting.leftChild.equals(finding)) {
                visitor.node = starting.leftChild;

                if (starting.leftChild.leftChild == null && starting.leftChild.rightChild == null) {
                    starting.setLeftChild(null);
                }
            } else {
                findNode(starting.leftChild, finding, visitor);
            }
        }

        if (starting.rightChild != null) {
            if (starting.rightChild.equals(finding)) {
                visitor.node = starting.rightChild;

                if (starting.rightChild.leftChild == null && starting.rightChild.rightChild == null) {
                    starting.setRightChild(null);
                }
            } else {
                findNode(starting.rightChild, finding, visitor);
            }
        }

        if (starting.rightChild != null) {
            findNode(starting.rightChild, finding, visitor);
        }
        if (starting.leftChild != null) {
            findNode(starting.leftChild, finding, visitor);
        }
    }

    private void deleteNodeFromQueue(Node toDelete) {
        toDelete.value = null;

        Node currentNode = toDelete;

        while (currentNode.leftChild != null || currentNode.rightChild != null) {

            if (currentNode.leftChild != null && currentNode.rightChild != null) {
                if (comparator.compare(currentNode.leftChild.value, currentNode.rightChild.value) >= 0) {
                    switchNodes(currentNode, false);

                    if (currentNode.leftChild.leftChild == null && currentNode.leftChild.rightChild == null) {
                        currentNode.setLeftChild(null);
                        System.out.println(currentNode + " left deleted \n\n");
                        break;
                    } else {
                        currentNode = currentNode.leftChild;
                    }
                } else {
                    switchNodes(currentNode, true);

                    if (currentNode.rightChild.leftChild == null && currentNode.rightChild.rightChild == null) {
                        currentNode.setRightChild(null);
                        System.out.println(currentNode + " right deleted \n\n");
                        break;
                    } else {
                        currentNode = currentNode.rightChild;
                    }
                }
            } else {

                if (currentNode.leftChild != null) {
                    switchNodes(currentNode, false);

                    if (currentNode.leftChild.leftChild == null && currentNode.leftChild.rightChild == null) {
                        currentNode.setLeftChild(null);
                        System.out.println(currentNode + " left deleted \n\n");
                        break;
                    } else {
                        currentNode = currentNode.leftChild;
                    }
                } else {
                    switchNodes(currentNode, true);

                    if (currentNode.rightChild.leftChild == null && currentNode.rightChild.rightChild == null) {
                        currentNode.setRightChild(null);
                        System.out.println(currentNode + " right deleted \n\n");
                        break;
                    } else {
                        currentNode = currentNode.rightChild;
                    }
                }

            }
        }
    }

    public String toString() {
        switcher = random.nextBoolean();
        return "graph {\n" + getEdges(queueRoot) + "}";
    }

    public T extract() {
        T toReturn = queueRoot.value;

        if (queueRoot.leftChild == null && queueRoot.rightChild == null) {
            queueRoot = null;
            return toReturn;
        }
        deleteNodeFromQueue(queueRoot);

        return toReturn;
    }

    private void switchNodes(Node node, boolean leftOrRight) {
        if (leftOrRight) {
            if (node.rightChild != null) {
                System.out.println(node.rightChild + " shifted up");
                node.value = node.rightChild.value;
                node.rightChild.value = null;
            }
        } else {
            if (node.leftChild != null) {
                System.out.println(node.leftChild + " shifted up");
                node.value = node.leftChild.value;
                node.leftChild.value = null;
            }
        }
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
                    } else {
                        toAdd.setRightChild(currentNode.rightChild);
                        currentNode.setRightChild(null);
                        toAdd.setLeftChild(currentNode);
                        queueRoot = toAdd;
                        isAdded = true;
                    }
                    switcher = random.nextBoolean();
                } else {

                    if (currentNode.leftChild == null || currentNode.rightChild == null) {
                        if (currentNode.leftChild == null) {
                            currentNode.leftChild = toAdd;
                        } else {
                            currentNode.rightChild = toAdd;
                        }
                        isAdded = true;
                        switcher = random.nextBoolean();
                    } else {

                        if (switcher) {
                            currentNode = currentNode.rightChild;
                            switcher = false;
                        } else {
                            currentNode = currentNode.leftChild;
                            switcher = true;
                        }
                        switcher = random.nextBoolean();

                    }
                }
            }
        }
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

        if (node.leftChild == null && node.rightChild == null) {
            string.append(node.value).append("\n");
        }
        Random random = new Random();

        switcher = random.nextBoolean();
        return string.toString();
    }


    class Node {
        private T value;
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


        @Override
        public String toString() {
            return "value=" + value;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }
    }
}
