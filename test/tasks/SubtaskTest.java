package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testEquals() {
        Subtask subtask1 = new Subtask("1", "1", 1, 1);
        Subtask subtask2 = new Subtask("2", "2", 1, 13);

        assertEquals(subtask1, subtask2);
    }
}