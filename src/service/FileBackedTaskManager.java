package service;

import tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class FileBackedTaskManager extends InMemoryTaskManager {
    final Path path;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toPath());
        try {
            String data = Files.readString(file.toPath());
            String[] dataArray = data.split("\n");
            if (Arrays.equals(dataArray, new String[]{""})) {
                return fileBackedTaskManager;
            }
            String[] sortedDataArray = new String[dataArray.length - 1];

            for (int i = 1; i < dataArray.length; i++) {
                String[] tempDataArray = dataArray[i].split(",");
                int j = Integer.parseInt(tempDataArray[0]) - 1;
                sortedDataArray[j] = dataArray[i];
            }
            for (String string : sortedDataArray) {
                Task task = fileBackedTaskManager.fromString(string);
                if (task.getType() == Type.TASK) {
                    fileBackedTaskManager.createNewTask(task);
                } else if (task.getType() == Type.EPIC) {
                    fileBackedTaskManager.createNewEpic((Epic) task);
                } else {
                    fileBackedTaskManager.createNewSubtask((Subtask) task);
                }
            }
        } catch (IOException exception) {
            System.out.println("Ошибка при чтении файла.");
        }
        return fileBackedTaskManager;
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.valueOf(path.getFileName())))) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
            final ArrayList<Task> taskTasks = getAllTasks();
            final ArrayList<Epic> epicTasks = getAllEpics();
            final ArrayList<Subtask> subtaskTasks = getAllSubtasks();
            for (Task task : taskTasks) {
                bufferedWriter.write(task.toString() + "\n");
            }
            for (Epic epicTask : epicTasks) {
                bufferedWriter.write(epicTask.toString() + "\n");
            }
            for (Subtask subtaskTask : subtaskTasks) {
                bufferedWriter.write(subtaskTask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла.");
        }
    }

    Task fromString(String value) {
        String[] values = value.split(",");
        Status status = Status.NEW;
        if (values[3].equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else if (values[3].equals("DONE")) {
            status = Status.DONE;
        }

        if (values[1].equals("TASK")) {
            Task task = new Task(values[2], values[4], Integer.parseInt(values[0]), status);
            task.setType(Type.TASK);
            return task;
        } else if (values[1].equals("EPIC")) {
            Epic epic = new Epic(values[2], values[4], Integer.parseInt(values[0]));
            epic.setStatus(status);
            epic.setType(Type.EPIC);
            return epic;
        } else {
            Subtask subtask = new Subtask(values[2], values[4], Integer.parseInt(values[0]), status,
                    Integer.parseInt(values[5]));
            subtask.setType(Type.SUBTASK);
            try {
                Epic epic = getEpic(subtask.getEpicId());
                epic.addSubtask(subtask.getTaskId());
            } catch (Exception e) {
                System.out.println("Такого эпика нет, или я неправильно написал код");
            }
            return subtask;
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
