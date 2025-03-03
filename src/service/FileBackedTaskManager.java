package service;

import exceptions.ManagerSaveException;
import exceptions.SetDurationException;
import exceptions.SetLocalDateTimeException;
import tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path path;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toPath());
        try {
            String data = Files.readString(file.toPath());
            if (data.isEmpty()) {
                return fileBackedTaskManager;
            }
            String[] dataArray = data.split("\\r\\n|\\r|\\n");
            for (int i = 1; i < dataArray.length; i++) {
                Task task = fileBackedTaskManager.fromString(dataArray[i]);
                if (task == null) {
                    continue;
                }
                if (fileBackedTaskManager.taskId < task.getTaskId()) {
                    fileBackedTaskManager.taskId = task.getTaskId();
                }
                if (task.getType() == Type.TASK) {
                    fileBackedTaskManager.taskTasks.put(task.getTaskId(), task);
                } else if (task.getType() == Type.EPIC) {
                    fileBackedTaskManager.epicTasks.put(task.getTaskId(), (Epic) task);
                } else {
                    fileBackedTaskManager.subtaskTasks.put(task.getTaskId(), (Subtask) task);
                }
            }
            fileBackedTaskManager.epicTasks.values().forEach(fileBackedTaskManager::updateEpicStatusAndTime);
            fileBackedTaskManager.taskTasks.values().stream()
                    .filter(task -> task.getStartTime() != null).forEach(fileBackedTaskManager.prioritizedTasks::add);
            fileBackedTaskManager.subtaskTasks.values().stream()
                    .filter(subtask -> subtask.getStartTime() != null).forEach(fileBackedTaskManager.prioritizedTasks::add);
        } catch (IOException exception) {
            System.out.println("Ошибка при чтении файла.");
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.valueOf(path.getFileName())))) {
            bufferedWriter.write("id,type,name,status,description,epic,duration,startTime\n");
            for (Task task : getAllTasks()) {
                bufferedWriter.write(task.toString() + "\n");
            }
            for (Epic epicTask : getAllEpics()) {
                bufferedWriter.write(epicTask.toString() + "\n");
            }
            for (Subtask subtaskTask : getAllSubtasks()) {
                bufferedWriter.write(subtaskTask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла.");
        }
    }

    private Task fromString(String value) {
        String[] values = value.split(",");
        Status status = Status.valueOf(values[3]);
        Type type = Type.valueOf(values[1]);
        switch (type) {
            case TASK:
                Task task = new Task(type, values[2], values[4], Integer.parseInt(values[0]), status);
                setDurationFromString(task, values[6]);
                if (values.length == 8) {
                    setLocalDateTimeFromString(task, values[7]);
                }
                return task;
            case EPIC:
                Epic epic = new Epic(type, values[2], values[4], Integer.parseInt(values[0]));
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(type, values[2], values[4], Integer.parseInt(values[0]), status,
                        Integer.parseInt(values[5]));
                setDurationFromString(subtask, values[6]);
                if (values.length == 8) {
                    setLocalDateTimeFromString(subtask, values[7]);
                }
                Epic subEpic = epicTasks.get(subtask.getEpicId());
                subEpic.addSubtask(subtask.getTaskId());
                return subtask;
        }
        return null;
    }

    private void setDurationFromString(Task task, String durationInSeconds) {
        long seconds;
        try {
            seconds = Long.parseLong(durationInSeconds);
            task.setDuration(Duration.ofSeconds(seconds));
        } catch (NumberFormatException exception) {
            throw new SetDurationException("Продолжительность задачи указана не в секундах");
        }
    }

    private void setLocalDateTimeFromString(Task task, String localDateTimeInSeconds) {
        long seconds;
        try {
            seconds = Long.parseLong(localDateTimeInSeconds);
        } catch (NumberFormatException e) {
            throw new SetLocalDateTimeException("Время начала задачи указано не в секундах");
        }
        if (seconds != 0) {
            task.setStartTime(LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofHours(3)));
        }
    }

    @Override
    public int createNewTask(Task task) {
        int result = super.createNewTask(task);
        save();
        return result;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public int createNewEpic(Epic epic) {
        int result = super.createNewEpic(epic);
        save();
        return result;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpic(int taskId) {
        super.deleteEpic(taskId);
        save();
    }

    @Override
    public int createNewSubtask(Subtask subtask) {
        int result = super.createNewSubtask(subtask);
        save();
        return result;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteSubtask(int taskId) {
        super.deleteSubtask(taskId);
        save();
    }
}
