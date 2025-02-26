package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addAndGet() {
        Task task1 = new Task(Type.TASK, "t1", "t1", 1);
        Task task2 = new Task(Type.TASK, "t2", "t2", 2);
        Task task3 = new Task(Type.TASK, "t3", "t3", 3);
        historyManager.add(task1);
        task1.setName("t1t1");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(task1.getName(), historyManager.getHistory().getFirst().getName());
        assertEquals(3, historyManager.getHistory().size());
        assertEquals(historyManager.getHistory().getLast(), task3);
    }

    @Test
    void remove() {
        Task task1 = new Task(Type.TASK, "t1", "t1", 1);
        Task task2 = new Task(Type.TASK, "t2", "t2", 2);
        Task task3 = new Task(Type.TASK, "t3", "t3", 3);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(task1.getTaskId());
        assertEquals(0, historyManager.getHistory().size());

        historyManager.add(task1);
        task1.setName("t1t1");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.remove(task1.getTaskId());
        assertEquals(historyManager.getHistory().getFirst().getTaskId(), task2.getTaskId());
        historyManager.add(task1);
        historyManager.remove(task3.getTaskId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(historyManager.getHistory().getLast().getTaskId(), task1.getTaskId());
        historyManager.remove(task1.getTaskId());
        assertEquals(1, historyManager.getHistory().size());
    }
}