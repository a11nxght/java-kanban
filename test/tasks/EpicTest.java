package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void testEquals() {
        Epic epic1 = new Epic("1", "1", 1);
        Epic epic2 = new Epic("2", "2", 1);

        assertEquals(epic1, epic2);
    }

    @Test
    void name() {
    }
}