package service;

import tasks.Task;

import java.util.Objects;

public class Node {
    Node previous;
    Node next;
    Task task;

    public Node(Node previous, Node next, Task task) {
        this.previous = previous;
        this.next = next;
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(task);
    }
}
