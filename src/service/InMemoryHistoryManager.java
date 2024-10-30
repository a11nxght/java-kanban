package service;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> history = new HashMap<>();

    Node first = null;
    Node last = null;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.containsKey(task.getTaskId())) {
                removeNode(history.get(task.getTaskId()));
            }
            history.put(task.getTaskId(), linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (first == null) {
            return new ArrayList<>();
        }
        ArrayList<Task> arrayList = new ArrayList<>();
        Node next = first;
        while (next != null) {
            arrayList.add(next.task);
            next = next.next;
        }
        return arrayList;
    }

    void removeNode(Node node) {
        if (node != null) {
            if (node.previous != null) {
                node.previous.next = node.next;
            } else {
                first = node.next;
            }
            if (node.next != null) {
                node.next.previous = node.previous;
            } else {
                last = node.previous;
            }

        }
    }

    private Node linkLast(Task lastTask) {
        if (first == null) {
            first = new Node(null, null, lastTask);
            last = first;
            return first;
        } else {
            Node node = new Node(last, null, lastTask);
            last.next = node;
            last = node;
            return node;
        }
    }
}
