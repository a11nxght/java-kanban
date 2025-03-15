package service;

import exceptions.NotFoundException;
import exceptions.TasksOverlapException;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected int taskId = 0;
    protected final HashMap<Integer, Task> taskTasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void deleteAllTasksFromHistoryManager(HashMap<Integer, ? extends Task> tasks) {
        tasks.keySet().forEach(historyManager::remove);
    }

    //task
    @Override
    public int createNewTask(Task task) {
        if (task.getStartTime() != null) {
            if (getPrioritizedTasks().stream().noneMatch(task1 -> isTasksCross(task, task1))) {
                task.setTaskId(++taskId);
                taskTasks.put(taskId, task);
                prioritizedTasks.add(task);
                return taskId;
            } else {
                System.out.println("Задачи пересекаются");
                return 0;
            }
        }
        task.setTaskId(++taskId);
        taskTasks.put(taskId, task);
        return taskId;
    }

    @Override
    public void updateTask(Task task) {
        if (taskTasks.containsValue(task)) {
            int taskId = task.getTaskId();
            Task oldTask = taskTasks.get(taskId);
            if (oldTask.getStartTime() != null) {
                if (task.getStartTime() != null) {
                    if (oldTask.getStartTime() != task.getStartTime() || oldTask.getDuration() != task.getDuration()) {
                        if (getPrioritizedTasks().stream()
                                .filter(task1 -> task1 != oldTask)
                                .noneMatch(task1 -> isTasksCross(task1, task))) {
                            prioritizedTasks.remove(oldTask);
                            prioritizedTasks.add(task);
                            taskTasks.replace(taskId, task);
                        } else {
                            System.out.println("Нельзя обновить задачу. Задачи пересекаются");
                            throw new TasksOverlapException("Нельзя обновить задачу. Задачи пересекаются");
                        }
                    } else {
                        taskTasks.replace(taskId, task);
                    }
                } else {
                    prioritizedTasks.remove(oldTask);
                    taskTasks.replace(taskId, task);
                }
            } else {
                if (task.getStartTime() != null) {
                    if (prioritizedTasks.stream().noneMatch(task1 -> isTasksCross(task1, task))) {
                        prioritizedTasks.add(task);
                        taskTasks.replace(taskId, task);
                    } else {
                        System.out.println("Нельзя обновить задачу. Задачи пересекаются");
                        throw new TasksOverlapException("Нельзя обновить задачу. Задачи пересекаются");
                    }
                } else {
                    taskTasks.replace(taskId, task);
                }
            }
        } else {
            System.out.println("Такой задачи нет.");
            throw new NotFoundException("Такой задачи нет");
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        deleteAllTasksFromHistoryManager(taskTasks);
        taskTasks.values().forEach(prioritizedTasks::remove);
        taskTasks.clear();
    }

    @Override
    public void deleteTask(int taskId) {
        if (taskTasks.containsKey(taskId)){
            historyManager.remove(taskId);
            prioritizedTasks.remove(taskTasks.get(taskId));
            taskTasks.remove(taskId);
        } else {
            throw new NotFoundException("Задачи с таким ID нет");
        }
    }

    @Override
    public Task getTask(int taskId) {
        if (taskTasks.containsKey(taskId)) {
            Task task = taskTasks.get(taskId);
            Task taskForHistory = new Task(Type.TASK, task.getName(), task.getDescription(), task.getTaskId());
            taskForHistory.setStatus(task.getStatus());
            historyManager.add(taskForHistory);
            return taskTasks.get(taskId);
        }
        throw new NotFoundException("Задачи с таким ID нет");
    }

    //epic
    @Override
    public ArrayList<Subtask> getSubtasksFromEpic(int epicTaskId) {
        List<Subtask> subtasks;
        if (epicTasks.containsKey(epicTaskId)) {
            subtasks = epicTasks.get(epicTaskId).getSubtasks().stream()
                    .map(subtaskTasks::get).toList();
            return new ArrayList<>(subtasks);

        }
        throw new NotFoundException("Нет эпика с таким Id");
    }

    @Override
    public int createNewEpic(Epic epic) {
        epic.setTaskId(++taskId);
        epicTasks.put(taskId, epic);
        return taskId;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicTasks.containsValue(epic)) {
            int taskId = epic.getTaskId();
            epicTasks.get(taskId).setName(epic.getName());
            epicTasks.get(taskId).setDescription(epic.getDescription());
        } else {
            System.out.println("Нет задачи с таким Id.");
        }
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public void deleteAllEpics() {
        deleteAllTasksFromHistoryManager(subtaskTasks);
        for (Subtask subtask : subtaskTasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        subtaskTasks.clear();
        deleteAllTasksFromHistoryManager(epicTasks);
        epicTasks.clear();
    }

    @Override
    public void deleteEpic(int taskId) {
        if (epicTasks.containsKey(taskId)) {
            Epic epic = epicTasks.get(taskId);

            epic.getSubtasks().forEach(integer -> {
                prioritizedTasks.remove(subtaskTasks.get(integer));
                subtaskTasks.remove(integer);
                historyManager.remove(integer);
            });
            historyManager.remove(taskId);
            epicTasks.remove(taskId);
        } else {
            throw new NotFoundException("Нет эпика с таким Id");
        }
    }

    @Override
    public Epic getEpic(int taskId) {
        if (epicTasks.containsKey(taskId)) {
            Epic epic = epicTasks.get(taskId);
            Epic epicForHistory = new Epic(Type.EPIC, epic.getName(), epic.getDescription(), epic.getTaskId());
            epic.getSubtasks().forEach(epicForHistory::addSubtask);
            epicForHistory.setStatus(epic.getStatus());
            historyManager.add(epicForHistory);
            return epicTasks.get(taskId);
        }
        throw new NotFoundException("Нет эпика с таким Id");
    }

    //subtask
    @Override
    public int createNewSubtask(Subtask subtask) {
        if (epicTasks.containsKey(subtask.getEpicId())) {
            if (subtask.getStartTime() != null) {
                if (getPrioritizedTasks().stream().noneMatch(task -> isTasksCross(subtask, task))) {
                    subtask.setTaskId(++taskId);
                    subtaskTasks.put(taskId, subtask);
                    Epic epic = epicTasks.get(subtask.getEpicId());
                    epic.addSubtask(taskId);
                    updateEpicStatusAndTime(epic);
                    prioritizedTasks.add(subtask);
                    return taskId;
                } else {
                    System.out.println("Задачи пересекаются");
                    throw new TasksOverlapException("Задачи пересекаются");
                }
            }
            subtask.setTaskId(++taskId);
            subtaskTasks.put(taskId, subtask);
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.addSubtask(taskId);
            updateEpicStatusAndTime(epic);
            return taskId;
        }
        throw new NotFoundException("Нет эпика с таким Id");
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskTasks.containsValue(subtask)) {
            Subtask oldSubTask = subtaskTasks.get(subtask.getTaskId());
            if (oldSubTask.getStartTime() != null) {
                if (subtask.getStartTime() != null) {
                    if (oldSubTask.getStartTime() != subtask.getStartTime()
                            || oldSubTask.getDuration() != subtask.getDuration()) {
                        if (getPrioritizedTasks().stream()
                                .filter(task -> task != subtask)
                                .noneMatch(task -> isTasksCross(task, subtask))) {
                            prioritizedTasks.remove(oldSubTask);
                            prioritizedTasks.add(subtask);
                            replaceSubtaskAndUpdateEpic(subtask);
                        } else {
                            System.out.println("Нельзя обновить задачу. Задачи пересекаются");
                            throw new TasksOverlapException("Нельзя обновить подзадачу. Задачи пересекаются");
                        }
                    } else {
                        replaceSubtaskAndUpdateEpic(subtask);
                    }
                } else {
                    prioritizedTasks.remove(oldSubTask);
                    replaceSubtaskAndUpdateEpic(subtask);
                }
            } else {
                if (subtask.getStartTime() != null) {
                    if (prioritizedTasks.stream().noneMatch(task -> isTasksCross(task, subtask))) {
                        prioritizedTasks.add(subtask);
                        replaceSubtaskAndUpdateEpic(subtask);
                    } else {
                        System.out.println("Нельзя обновить задачу. Задачи пересекаются");
                        throw new TasksOverlapException("Нельзя обновить подзадачу. Задачи пересекаются");
                    }
                } else {
                    replaceSubtaskAndUpdateEpic(subtask);
                }
            }
        } else {
            System.out.println("Такой подзадачи нет.");
            throw new NotFoundException("Такой подзадачи нет.");
        }
    }

    private void replaceSubtaskAndUpdateEpic(Subtask subtask) {
        subtaskTasks.replace(subtask.getTaskId(), subtask);
        Epic epic = epicTasks.get(subtask.getEpicId());
        updateEpicStatusAndTime(epic);
    }

    protected void updateEpicStatusAndTime(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
            return;
        }

        List<Subtask> epicSubTasks = subtaskTasks.values().stream()
                .filter(subtask -> epic.getSubtasks().contains(subtask.getTaskId())).toList();

        long epicDurationInSeconds = epicSubTasks.stream()
                .map(Task::getDuration)
                .map(Duration::getSeconds).mapToLong(value -> value).sum();

        epic.setDuration(Duration.ofSeconds(epicDurationInSeconds));

        if (epicSubTasks.stream().allMatch(subtask -> subtask.getStartTime() == null)) {
            epic.setStartTime(null);
            epic.setEndTime(null);
        } else {
            epicSubTasks.stream().filter(subtask -> subtask.getStartTime() != null)
                    .min(Comparator.comparing(Task::getStartTime))
                    .ifPresent(subtask -> epic.setStartTime(subtask.getStartTime()));

            epicSubTasks.stream().filter(subtask -> subtask.getStartTime() != null)
                    .max(Comparator.comparing(Task::getStartTime))
                    .ifPresent(subtask -> epic.setEndTime(subtask.getEndTime()));
        }


        if (epic.getSubtasks().stream()
                .map(subtaskTasks::get)
                .allMatch(subtask -> subtask.getStatus() == Status.NEW)) {
            epic.setStatus(Status.NEW);
            return;
        }
        if (epic.getSubtasks().stream()
                .map(subtaskTasks::get)
                .allMatch(subtask -> subtask.getStatus() == Status.DONE)) {
            epic.setStatus(Status.DONE);
            return;
        }
        epic.setStatus(Status.IN_PROGRESS);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskTasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        epicTasks.values().forEach(epic -> {

            epic.deleteAllSubtasks();
            updateEpicStatusAndTime(epic);
        });
        deleteAllTasksFromHistoryManager(subtaskTasks);
        subtaskTasks.values().stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .forEach(prioritizedTasks::remove);
        subtaskTasks.clear();
    }

    @Override
    public void deleteSubtask(int taskId) {
        if (subtaskTasks.containsKey(taskId)) {
            Subtask subtask = subtaskTasks.get(taskId);
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.deleteSubtask(taskId);
            updateEpicStatusAndTime(epic);
            historyManager.remove(taskId);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }
            subtaskTasks.remove(taskId);
        } else {
            System.out.println("Подзадачи с таким Id нет.");
            throw new NotFoundException("Подзадачи с таким Id нет.");
        }
    }

    @Override
    public Subtask getSubtask(int taskId) {
        if (subtaskTasks.containsKey(taskId)) {
            Subtask subtask = subtaskTasks.get(taskId);
            Subtask subtaskForHistory = new Subtask(Type.SUBTASK, subtask.getName(), subtask.getDescription(),
                    subtask.getTaskId(), subtask.getEpicId());
            subtaskForHistory.setStatus(subtask.getStatus());
            historyManager.add(subtaskForHistory);
            return subtaskTasks.get(taskId);
        }
        throw new NotFoundException("Нет подзадачи с таким Id.");
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private boolean isTasksCross(Task task1, Task task2) {
        LocalDateTime task1Start = task1.getStartTime();
        LocalDateTime task2Start = task2.getStartTime();
        LocalDateTime task1Finish = task1.getEndTime();
        LocalDateTime task2Finish = task2.getEndTime();
        if (task1Start == null || task2Start == null) {
            return false;
        }
        if (task1Start.isBefore(task2Start) && task1Finish.isBefore(task2Start)) {
            return false;
        } else return !task1Start.isAfter(task2Finish) || !task1Finish.isAfter(task2Finish);
    }
}
