package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void testEquals() {
        Epic epic1 = new Epic(Type.EPIC, "1", "1", 1);
        Epic epic2 = new Epic(Type.EPIC, "2", "2", 1);

        assertEquals(epic1, epic2);
    }

    @Test
    void checkThatEpicCannotBeAddedToItselfAsASubtask() {
        Epic epic1 = new Epic(Type.EPIC, "1", "1", 1);
        epic1.addSubtask(1);

        assertEquals(0, epic1.getSubtasks().size());
    }
}