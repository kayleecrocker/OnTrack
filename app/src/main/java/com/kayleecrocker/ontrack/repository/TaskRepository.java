package com.kayleecrocker.ontrack.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.kayleecrocker.ontrack.dao.TaskDao;
import com.kayleecrocker.ontrack.database.AppDatabase;
import com.kayleecrocker.ontrack.entities.Task;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getTask(int taskId) {
        return taskDao.getTask(taskId);
    }

    public void insert(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.delete(task));
    }
}