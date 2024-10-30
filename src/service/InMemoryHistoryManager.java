package service;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //    private final ArrayList<Task> history = new ArrayList<>();
    private final HashMap<Integer, Node> history = new HashMap<>();
    private final TaskList taskList = new TaskList();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.containsKey(task.getTaskId())) {
                removeNode(history.get(task.getTaskId()));
            }
            taskList.linkLast(task);
            history.put(task.getTaskId(), taskList.last);
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
        return new ArrayList<>(taskList.getTasks());
    }

    void removeNode(Node node) {
        if (node != null) {
            if (node.previous != null) {
                node.previous.next = node.next;
            } else {
                taskList.first = node.next;
            }
            if (node.next != null) {
                node.next.previous = node.previous;
            } else {
                taskList.last = node.previous;
            }

        }
    }
}

class TaskList {
    Node first = null;
    Node last = null;

    void linkLast(Task lastTask) {
        if (first == null) {
            first = new Node(null, null, lastTask);
            last = first;
        } else {
            Node node = new Node(last, null, lastTask);
            last.next = node;
            last = node;
        }
    }

    List<Task> getTasks() {
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
}
