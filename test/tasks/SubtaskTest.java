package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testEquals() {
        Subtask subtask1 = new Subtask(Type.SUBTASK, "1", "1", 1, 1);
        Subtask subtask2 = new Subtask(Type.SUBTASK, "2", "2", 1, 13);

        assertEquals(subtask1, subtask2);
    }
}