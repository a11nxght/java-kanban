package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testEquals() {
        Task task1 = new Task("1", "1", 1);
        Task task2 = new Task("2", "2", 1);
        
        assertEquals(task1, task2);
    }
}