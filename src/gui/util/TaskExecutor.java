package gui.util;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {
    public static <T> void executeTask(Task<T> task) {
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            executorService.submit(task);
        }
    }

    public static <T> void executeTasks(List<Task<T>> tasks) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(tasks.size())) {
            tasks.forEach(executorService::submit);
        }
    }
}
