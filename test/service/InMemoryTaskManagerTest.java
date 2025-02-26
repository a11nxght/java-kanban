package service;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void setUp() {
        taskManager = getTaskManager();
    }
}